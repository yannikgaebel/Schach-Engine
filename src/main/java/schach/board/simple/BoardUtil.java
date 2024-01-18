package schach.board.simple;

import java.util.ArrayList;
import java.util.List;

import schach.board.*;

/**
 * BoardUtil provides utility functions for Board/Move related problems
 */
class BoardUtil {
	static List<Move> getStraightMoves(IBoardData data, Color sideToMove, Position pos) {
		List<Move> moves = new ArrayList<Move>();

		// up
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(0, 1)));
		// down
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(0, -1)));
		// right
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(1, 0)));
		// left
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(-1, 0)));

		return moves;
	}

	static List<Move> getDiagonalMoves(IBoardData data, Color sideToMove, Position pos) {
		List<Move> moves = new ArrayList<Move>();

		// up right
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(1, 1)));
		// down right
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(1, -1)));
		// up left
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(-1, 1)));
		// bottom left
		moves.addAll(stepper(data, sideToMove, pos, new PositionDelta(-1, -1)));

		return moves;
	}

	private static List<Move> stepper(IBoardData data, Color sideToMove, Position pos, PositionDelta delta) {
		List<Move> moves = new ArrayList<Move>();

		int rank = pos.getRank() + delta.getRankDelta();
		int file = pos.getFile() + delta.getFileDelta();
		while (true) {
			if (!Position.isValid(file, rank)) {
				break;
			}

			if (!data.anyPieceAt(new Position(file, rank), sideToMove)) {
				moves.add(new Move(pos, new Position(file, rank)));
				if (data.anyPieceAt(new Position(file, rank), sideToMove.getInverted())) {
					break;
				}
			} else {
				break;
			}

			rank += delta.getRankDelta();
			file += delta.getFileDelta();
		}

		return moves;
	}

	static List<Position> kingPossiblePositions(Position pos) {
		List<Position> positions = new ArrayList<Position>();

		int[] rankPositions = { 1, 1, 0, -1, -1, -1, 0, 1 };
		int[] filePositions = { 0, 1, 1, 1, 0, -1, -1, -1 };
		for (int i = 0; i < 8; i++) {
			if (Position.isValid(pos.getFile() + filePositions[i], pos.getRank() + rankPositions[i])) {
				positions.add(new Position(pos.getFile() + filePositions[i], pos.getRank() + rankPositions[i]));
			}
		}

		return positions;
	}

	static List<Position> knightPossiblePositions(Position pos) {
		List<Position> positions = new ArrayList<Position>();

		int[] rankPositions = { 2, 1, -1, -2, -2, -1, 1, 2 };
		int[] filePositions = { 1, 2, 2, 1, -1, -2, -2, -1 };
		for (int i = 0; i < 8; i++) {
			if (Position.isValid(pos.getFile() + filePositions[i], pos.getRank() + rankPositions[i])) {
				positions.add(new Position(pos.getFile() + filePositions[i], pos.getRank() + rankPositions[i]));
			}
		}

		return positions;
	}

	static int initialPawnRank(Color color) {
		if (color == Color.White) {
			return 1;
		} else {
			return 6;
		}
	}

	static int pawnRankDir(Color color) {
		if (color == Color.White) {
			return 1;
		} else {
			return -1;
		}
	}

	static int promotionRank(Color color) {
		if (color == Color.White) {
			return 7;
		} else {
			return 0;
		}
	}

	static PieceType asPromotion(PieceType piece) throws BoardException {
		if (piece == null) {
			return PieceType.Queen;
		}

		if (piece == PieceType.King || piece == PieceType.Pawn) {
			throw new BoardException("Invalid promotion");
		}

		return piece;
	}
}
