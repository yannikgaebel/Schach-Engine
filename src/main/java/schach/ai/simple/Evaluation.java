package schach.ai.simple;

import schach.board.*;

/**
 * Evaluate a given board
 */
class Evaluation {
	private IBoard board;

	private Evaluation(IBoard board) {
		this.board = board;
	}

	static int evaluate(IBoard board) {
		Evaluation eval = new Evaluation(board);
		return eval.eval();
	}

	private int eval() {
		// Checkmate?
		if (board.getStatus() == BoardStatus.Checkmate) {
			return -1_000_000;
		}

		// Calc points
		int points = 0;
		points += MaterialEvaluation.evaluate(board);
		points += PositionEvaluation.evaluate(board);

		// Draw?
		if (board.getStatus() == BoardStatus.DrawByStalemate
				|| board.getStatus() == BoardStatus.DrawByInsufficientMaterial
				|| board.getStatus() == BoardStatus.DrawByFiftyMoveRule) {
			return -points;
		}

		// Return points
		return points;
	}
}
