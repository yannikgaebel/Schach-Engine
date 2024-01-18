package schach.ai.simple;

import org.junit.jupiter.api.Test;

import schach.board.*;
import schach.board.simple.Board;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #AI}
 */
public class AITest {
	/**
	 * Test get best move of AI
	 */
	@Test
	public void testGetBestMove() {
		IBoard board = null;
		try {
			board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		} catch (BoardException e) {
			fail(e);
		}

		Move move = null;
		try {
			move = new AI().getBestMove(board, 1);
		} catch (BoardException e) {
			fail(e);
		}
		assertNotNull(move, "Error in Class AI for Startposition");
	}

}
