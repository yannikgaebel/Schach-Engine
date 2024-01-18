package schach.ai;

import schach.board.*;

/**
 * Interface for an AI
 */
public interface IAI {
	/**
	 * Get the best move found by the AI
	 * 
	 * @param board the board to find the move for
	 * @param depth the depth of the alpha beta tree
	 * @return the best move
	 * @throws BoardException throws if there is no move possible (e.g. checkmate)
	 */
	Move getBestMove(IBoard board, int depth) throws BoardException;
}
