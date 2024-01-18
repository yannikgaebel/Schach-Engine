package schach.board.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import schach.board.*;

/**
 * Simple implementation of IBoard
 */
public class Board implements IBoard {
	private BoardData data;

	private List<Move> validMoves;
	private BoardStatus status;

	/**
	 * Creates a new board in the initial position
	 */
	public Board() {
		data = new BoardData();

		validMoves = calcValidMoves();
		status = BoardStatus.Ongoing;
	}

	/**
	 * Creates a new board from the given fenString
	 * 
	 * @param fenString the fen to create the board
	 * @throws BoardException throws if the fen is invalid
	 */
	public Board(String fenString) throws BoardException {
		Fen fen = Fen.parse(fenString);

		data = new BoardData(fen);

		validMoves = calcValidMoves();
		status = calcStatus();

		// TODO Check if board is sane
	}

	private Board(BoardData data) {
		this.data = data;

		validMoves = calcValidMoves();
		this.status = calcStatus();
	}

	@Override
	public BoardResult move(Move move) throws BoardException {
		// Check move is in validMoves
		if (!validMoves.contains(move)) {
			throw new BoardException("Invalid move");
		}

		BoardDataResult boardDataResult = this.data.newFromPseudoValidMove(move);
		Board board = new Board(boardDataResult.boardData);

		return new BoardResult(board, boardDataResult.beaten);
	}

	@Override
	public List<Move> getValidMoves() {
		return Collections.unmodifiableList(validMoves);
	}

	@Override
	public List<Move> getValidMoves(Position position) {
		ArrayList<Move> moves = new ArrayList<Move>();

		for (Move validMove : validMoves) {
			if (validMove.getFrom().equals(position)) {
				moves.add(validMove);
			}
		}

		return Collections.unmodifiableList(moves);
	}

	@Override
	public BoardStatus getStatus() {
		return status;
	}

	@Override
	public Color getSideToMove() {
		return data.getFlags().getSideToMove();
	}

	@Override
	public PieceType pieceAt(Position position) {
		return data.pieceAt(position);
	}

	@Override
	public PieceType pieceAt(Position position, Color color) {
		return data.pieceAt(position, color);
	}

	@Override
	public CastleRights getCastleRights(Color color) {
		return data.getFlags().getCastleRights(color);
	}
	
	@Override
	public Position getKingPosition(Color color) {
		return data.getPositionsOf(PieceType.King, color).get(0);
	}

	private List<Move> calcValidMoves() {
		List<Move> pseudoValidMoves = BoardMoveGen.genPseudoMoves(data);
		ArrayList<Move> moves = new ArrayList<Move>();

		for (Move pseudoValidMove : pseudoValidMoves) {
			if (checkPseudoValidMove(pseudoValidMove)) {
				moves.add(pseudoValidMove);
			}
		}

		return moves;
	}

	private boolean checkPseudoValidMove(Move move) {
		// Try to create new board data from move
		// Okay -> move is valid -> return true
		// Err -> move is invalid -> return false
		try {
			// Ignore the result, we are only interested if this throws
			this.data.newFromPseudoValidMove(move);
		} catch (BoardException e) {
			return false;
		}

		return true;
	}

	/**
	 * Calculate the status of this board
	 */
	private BoardStatus calcStatus() {
		if (data.getFlags().getHalfmoveClock() >= 50) {
			// Reset valid moves
			validMoves.clear();
			return BoardStatus.DrawByFiftyMoveRule;
		}

		// Calc Check
		Position kingPosition = data.getPositionsOf(PieceType.King, data.getFlags().getSideToMove()).get(0);
		boolean isCheck = data.isPseudoAttacked(kingPosition, data.getFlags().getSideToMove().getInverted());

		// Set Status
		if (isCheck) {
			if (validMoves.size() == 0) {
				return BoardStatus.Checkmate;
			} else {
				return BoardStatus.Check;
			}
		} else {
			if (validMoves.size() == 0) {
				return BoardStatus.DrawByStalemate;
			} else {
				boolean sufficientMaterial = isSufficientMaterial();
				if (sufficientMaterial) {
					return BoardStatus.Ongoing;
				} else {
					// Reset valid moves
					validMoves.clear();
					return BoardStatus.DrawByInsufficientMaterial;
				}
			}
		}
	}

	private boolean isSufficientMaterial() {
		int whitePiecesCount = data.whitePiecesCount();
		int blackPiecesCount = data.blackPiecesCount();

		if (isKingVsKing(whitePiecesCount, blackPiecesCount)) {
			return false;
		}

		if (isOneBishopOrKnight(whitePiecesCount, blackPiecesCount)) {
			return false;
		}

		if (isKingsAndBishops(whitePiecesCount, blackPiecesCount)) {
			return false;
		}

		final boolean sufficientMaterial = true;
		return sufficientMaterial;
	}

	private boolean isKingVsKing(int whitePiecesCount, int blackPiecesCount) {
		// King vs King
		return whitePiecesCount == 1 && blackPiecesCount == 1;
	}

	private boolean isOneBishopOrKnight(int whitePiecesCount, int blackPiecesCount) {
		// King+(Bishop/Knight) vs King
		if (whitePiecesCount == 2 && blackPiecesCount == 1
				&& (data.getPositionsOf(PieceType.Bishop, Color.White).size() == 1
						|| data.getPositionsOf(PieceType.Knight, Color.White).size() == 1)) {
			return true;
		}

		// King vs King+(Bishop/Knight)
		if (whitePiecesCount == 1 && blackPiecesCount == 2
				&& (data.getPositionsOf(PieceType.Bishop, Color.Black).size() == 1
						|| data.getPositionsOf(PieceType.Knight, Color.Black).size() == 1)) {
			return true;
		}

		final boolean oneBishopOrKnight = false;
		return oneBishopOrKnight;
	}

	private boolean isKingsAndBishops(int whitePiecesCount, int blackPiecesCount) {
		// King+Bishop vs King+Bishop (Bishops on same color)
		if (whitePiecesCount == 2 && blackPiecesCount == 2) {
			List<Position> positionsOfWhiteBishops = data.getPositionsOf(PieceType.Bishop, Color.White);
			List<Position> positionsOfBlackBishops = data.getPositionsOf(PieceType.Bishop, Color.Black);
			if (positionsOfWhiteBishops.size() == 1 && positionsOfBlackBishops.size() == 1) {
				Position white = positionsOfWhiteBishops.get(0);
				Position black = positionsOfWhiteBishops.get(0);
				boolean onSameColor = (white.getRank() + white.getFile()) % 2 == (black.getRank() + black.getFile())
						% 2;
				if (onSameColor) {
					return true;
				}
			}
		}

		return false;
	}
}
