package schach.ai.simple;

import java.util.ArrayList;
import java.util.List;

import schach.ai.IAI;
import schach.board.*;

/**
 * Simple implementation of IAI
 */
public class AI implements IAI {
	private Move bestMove;
	private int bestValue;
	private int alpha;

	@Override
	public Move getBestMove(IBoard board, int depth) throws BoardException {
		// Check if board has any moves
		if (board.getValidMoves().isEmpty()) {
			throw new BoardException("No valid Moves?!");
		}

		// Run min max threaded
		minmaxThreaded(board, depth);

		// Return best move
		return this.bestMove;
	}

	private void minmaxThreaded(IBoard board, int depth) throws BoardException {
		// Reset
		bestMove = null;
		bestValue = Integer.MIN_VALUE;
		alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

		// Get valid moves
		List<Move> validMoves = board.getValidMoves();

		// Evaluate moves in groups of 3, to make use of multi cores AND
		// alpha-beta-pruning
		for (int moveOffset = 0; moveOffset < validMoves.size(); moveOffset += 3) {
			// Start all evaluations in parallel
			List<Thread> threads = new ArrayList<Thread>();
			for (int i = moveOffset; i < validMoves.size() && i < moveOffset + 3; i++) {
				Move move = validMoves.get(i);
				Thread t = new Thread(() -> {
					int value = 0;
					try {
						value = -minmax(board.move(move).board, depth - 1, false, alpha, beta);
					} catch (BoardException e) {
						e.printStackTrace();
					}

					synchronized (AI.class) {
						if (value > bestValue) {
							bestValue = value;
							bestMove = move;
						}

						alpha = value > alpha ? value : alpha;
					}
				});
				t.start();
				threads.add(t);
			}

			// Join all threads
			for (Thread t : threads) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// AvoidReassigningParameters: Reassigning alpha/beta is idiomatic
	// ExcessiveParameterList: Extra class for (maximizingPlayer, alpha, beta) would
	// just complicate copying and changing variables
	@SuppressWarnings({ "PMD.AvoidReassigningParameters", "PMD.ExcessiveParameterList" })
	private int minmax(IBoard board, int depth, boolean maximizingPlayer, int alpha, int beta) throws BoardException {
		// Evaluate if depth is zero or the game is over
		if (depth == 0 || board.getStatus().isOver()) {
			int eval = Evaluation.evaluate(board);
			if (board.getStatus() == BoardStatus.Checkmate) {
				eval -= depth;
			}

			return eval;
		}

		// Get valid moves
		List<Move> validMoves = board.getValidMoves();
		int bestValue = Integer.MIN_VALUE;

		// Evaluate all moves with minmax + alpha-beta-pruning
		for (Move move : validMoves) {
			int value = -minmax(board.move(move).board, depth - 1, !maximizingPlayer, alpha, beta);

			bestValue = Math.max(bestValue, value);

			if (maximizingPlayer) {
				alpha = Math.max(alpha, value);
				if (beta <= alpha)
					break;
			} else {
				beta = Math.min(beta, -value);
				if (beta <= alpha)
					break;
			}
		}

		// Return the best possible value for this board and the maximizing player
		return bestValue;
	}
}
