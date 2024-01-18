package schach.ai.simple;

import schach.board.*;

/**
 * Evaluate the material of a given board
 */
class MaterialEvaluation {
	private IBoard board;

	private MaterialEvaluation(IBoard board) {
		this.board = board;
	}

	static int evaluate(IBoard board) {
		MaterialEvaluation eval = new MaterialEvaluation(board);
		return eval.eval();
	}

	private int eval() {
		int points = 0;

		// Evaluate all pieces
		points += evaluatePieces(board.getSideToMove());
		points -= evaluatePieces(board.getSideToMove().getInverted());

		return points;
	}

	private int evaluatePieces(Color colorToEvaluate) {
		int points = 0;

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				points += getValueOfPieceType(board.pieceAt(new Position(file, rank), colorToEvaluate));
			}
		}

		return points;
	}

	private int getValueOfPieceType(PieceType type) {
		if (type == null)
			return 0;

		switch (type) {
		case Pawn:
			return 100;
		case Knight:
			return 310;
		case Bishop:
			return 320;
		case Rook:
			return 460;
		case Queen:
			return 900;
		default: // King
			return 0;
		}
	}
}
