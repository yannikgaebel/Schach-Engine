package schach.board.simple;

import java.util.ArrayList;
import java.util.List;
import schach.board.*;

/**
 * Pseudo move generator for a given board data
 */
class BoardMoveGen {
	private IBoardData data;

	private BoardMoveGen(IBoardData data) {
		this.data = data;
	}

	/**
	 * Calculate all moves that are pseudo legal. Pseudo legal moves are all moves
	 * that are legal moves plus moves that are legal except leaving the own king in
	 * check.
	 * 
	 * @param data the board data to generate the moves for
	 * @return all pseudo valid moves for the given board data
	 */
	static List<Move> genPseudoMoves(IBoardData data) {
		BoardMoveGen moveGen = new BoardMoveGen(data);

		List<Move> moves = new ArrayList<Move>();

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				moves.addAll(moveGen.getValidMoves(new Position(file, rank)));
			}
		}

		return moves;
	}

	private List<Move> getValidMoves(Position position) {
		List<Move> moves = new ArrayList<Move>();
		PieceType type = data.pieceAt(position, data.getFlags().getSideToMove());

		if (type != null) {
			switch (type) {
			case Bishop:
				moves.addAll(BoardUtil.getDiagonalMoves(data, data.getFlags().getSideToMove(), position));
				break;
			case King:
				moves.addAll(getKingMoves(position));
				break;
			case Knight:
				moves.addAll(getKnightMoves(position));
				break;
			case Pawn:
				moves.addAll(getPawnMoves(position));
				break;
			case Queen:
				moves.addAll(BoardUtil.getStraightMoves(data, data.getFlags().getSideToMove(), position));
				moves.addAll(BoardUtil.getDiagonalMoves(data, data.getFlags().getSideToMove(), position));
				break;
			case Rook:
				moves.addAll(BoardUtil.getStraightMoves(data, data.getFlags().getSideToMove(), position));
				break;
			}
		}

		return moves;
	}

	private List<Move> getPawnMoves(Position pos) {
		List<Move> moves = new ArrayList<Move>();

		moves.addAll(getPawnMovesStraight(pos));
		moves.addAll(getPawnMovesDiagonal(pos));
		moves.addAll(getPawnMovesEnPassant(pos));

		return moves;
	}

	private List<Move> getPawnMovesStraight(Position pos) {
		List<Move> moves = new ArrayList<Move>();

		int pawnRankDir = BoardUtil.pawnRankDir(data.getFlags().getSideToMove());
		int initialPawnRank = BoardUtil.initialPawnRank(data.getFlags().getSideToMove());

		// Advance by 1
		Position advancedByOne = new Position(pos.getFile(), pos.getRank() + pawnRankDir);
		if (!data.anyPieceAt(advancedByOne)) {
			if (advancedByOne.getRank() == BoardUtil.promotionRank(data.getFlags().getSideToMove())) {
				moves.add(new Move(pos, advancedByOne, PieceType.Queen));
				moves.add(new Move(pos, advancedByOne, PieceType.Rook));
				moves.add(new Move(pos, advancedByOne, PieceType.Bishop));
				moves.add(new Move(pos, advancedByOne, PieceType.Knight));
			} else {
				moves.add(new Move(pos, advancedByOne));
			}
		}

		// Advance by 2
		if (pos.getRank() == initialPawnRank
				&& !data.anyPieceAt(new Position(pos.getFile(), pos.getRank() + pawnRankDir))
				&& !data.anyPieceAt(new Position(pos.getFile(), pos.getRank() + pawnRankDir * 2))) {
			moves.add(new Move(pos, new Position(pos.getFile(), pos.getRank() + pawnRankDir * 2)));
		}

		return moves;
	}

	private List<Move> getPawnMovesDiagonal(Position pos) {
		List<Move> moves = new ArrayList<Move>();

		// dir
		int pawnRankDir = BoardUtil.pawnRankDir(data.getFlags().getSideToMove());
		// left and right
		int[] fileDeltas = { -1, 1 };

		for (int fileDelta : fileDeltas) {
			int file = pos.getFile() + fileDelta;
			int rank = pos.getRank() + pawnRankDir;

			if (!Position.isValid(file, rank)) {
				continue;
			}

			Position to = new Position(file, rank);
			if (data.anyPieceAt(to, data.getFlags().getSideToMove().getInverted())) {
				if (rank == BoardUtil.promotionRank(data.getFlags().getSideToMove())) {
					moves.add(new Move(pos, to, PieceType.Queen));
					moves.add(new Move(pos, to, PieceType.Rook));
					moves.add(new Move(pos, to, PieceType.Bishop));
					moves.add(new Move(pos, to, PieceType.Knight));
				} else {
					moves.add(new Move(pos, to));
				}
			}
		}

		return moves;
	}

	private List<Move> getPawnMovesEnPassant(Position pos) {
		List<Move> moves = new ArrayList<Move>();

		// dir
		int pawnRankDir = BoardUtil.pawnRankDir(data.getFlags().getSideToMove());
		// left and right
		int[] fileDeltas = { -1, 1 };

		for (int fileDelta : fileDeltas) {
			int file = pos.getFile() + fileDelta;
			int rank = pos.getRank() + pawnRankDir;

			if (!Position.isValid(file, rank)) {
				continue;
			}

			if (new Position(file, rank).equals(data.getFlags().getEnPassant())) {
				moves.add(new Move(pos, data.getFlags().getEnPassant()));
			}
		}

		return moves;
	}

	private List<Move> getKnightMoves(Position pos) {
		List<Move> moves = new ArrayList<Move>();

		for (Position p : BoardUtil.knightPossiblePositions(pos)) {
			if (!data.anyPieceAt(p, data.getFlags().getSideToMove())) {
				moves.add(new Move(pos, p));
			}
		}

		return moves;
	}

	private List<Move> getKingMoves(Position pos) {
		List<Move> moves = new ArrayList<Move>();

		moves.addAll(getKingMovesStandard(pos));
		moves.addAll(getKingMovesCastle(pos));

		return moves;
	}

	private List<Move> getKingMovesStandard(Position pos) {
		List<Move> moves = new ArrayList<Move>();

		for (Position p : BoardUtil.kingPossiblePositions(pos)) {
			if (!data.anyPieceAt(p, data.getFlags().getSideToMove())) {
				moves.add(new Move(pos, p));
			}
		}

		return moves;
	}

	private List<Move> getKingMovesCastle(Position pos) {
		List<Move> moves = new ArrayList<Move>();
		CastleRights castleRights = data.getFlags().getCastleRights(data.getFlags().getSideToMove());

		int rank;
		if (data.getFlags().getSideToMove() == Color.White) {
			rank = 0;
		} else {
			rank = 7;
		}

		if (castleRights == CastleRights.KingSide || castleRights == CastleRights.Both) {
			Position[] positions = { new Position(4, rank), new Position(5, rank), new Position(6, rank) };
			if (canCastleOver(positions, data.getFlags().getSideToMove())) {
				moves.add(new Move(pos, new Position(6, rank)));
			}
		}
		if (castleRights == CastleRights.QueenSide || castleRights == CastleRights.Both) {
			Position[] positions = { new Position(4, rank), new Position(3, rank), new Position(2, rank) };
			if (canCastleOver(positions, data.getFlags().getSideToMove())) {
				moves.add(new Move(pos, new Position(2, rank)));
			}
		}

		return moves;
	}

	private boolean canCastleOver(Position[] positions, Color sideToMove) {
		for (Position pos : positions) {
			boolean occupied = data.anyPieceAt(pos) && data.pieceAt(pos, sideToMove) != PieceType.King;
			if (occupied || data.isPseudoAttacked(pos, sideToMove.getInverted())) {
				return false;
			}
		}

		return true;
	}
}
