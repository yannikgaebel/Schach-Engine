package schach.ai.simple;

import org.junit.jupiter.api.Test;

import schach.board.*;
import schach.board.simple.Board;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #MaterialEvaluation}
 */
public class MaterialEvaluationTest {
	/**
	 * Test the evaluated material
	 */
	@Test
	public void testEvaluate() {
		// Start position
		IBoard board = null;
		try {
			board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		int pointsExpected = 0;
		int points = MaterialEvaluation.evaluate(board);
		assertEquals(pointsExpected, points, "Start position is not evaluated correctly.");

		// Position 2
		IBoard board2 = null;
		try {
			board2 = new Board("4kbnr/4pppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQk - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		int pointsExpected2 = 2390;
		int points2 = MaterialEvaluation.evaluate(board2);
		assertEquals(pointsExpected2, points2, "Position 2 is not evaluated correctly.");

		// Position 3
		IBoard board3 = null;
		try {
			board3 = new Board("1k6/1rrbb3/1pp5/8/8/8/1RRBBNQ1/1K6 b - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		int pointsExpected3 = -1010;
		int points3 = MaterialEvaluation.evaluate(board3);
		assertEquals(pointsExpected3, points3, "Position 3 is not evaluated correctly.");
	}

}
