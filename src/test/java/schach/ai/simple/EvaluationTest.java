package schach.ai.simple;

import org.junit.jupiter.api.Test;

import schach.board.*;
import schach.board.simple.Board;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #Evaluation}
 */
public class EvaluationTest {
	/**
	 * Test checkmate
	 */
	@Test
	public void testCheckmate() {
		// Position
		IBoard board = null;
		try {
			board = new Board("1k5R/8/1K6/8/8/8/8/8 b - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}

		// Assert
		int points = -Evaluation.evaluate(board);
		assertTrue(points > 0, "white should be evaluated better");
	}

	/**
	 * Test stalemate
	 */
	@Test
	public void testStalemate() {
		// Position
		IBoard board = null;
		try {
			board = new Board("k7/1R6/2K5/8/8/8/8/8 b - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}

		// Assert
		int points = -Evaluation.evaluate(board);
		assertTrue(points < 0, "white should be evaluated worse");
	}
}
