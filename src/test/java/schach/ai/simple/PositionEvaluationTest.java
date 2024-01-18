package schach.ai.simple;

import org.junit.jupiter.api.Test;

import schach.board.*;
import schach.board.simple.Board;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #PositionEvaluation}
 */
public class PositionEvaluationTest {
	/**
	 * Test the evaluated position
	 */
	@Test
	public void testEvaluate() {
		IBoard board = null;
		try {
			board = new Board("kqr5/1p1p4/2p5/8/8/2P5/1P1P4/KQR5 w - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		int points = PositionEvaluation.evaluate(board);
		assertTrue(points > 0, "Position was not evaluated correctly.");

		IBoard board2 = null;
		try {
			board2 = new Board("r1bqkbnr/pppppppp/2n5/8/8/2N5/PPPPPPPP/R1BQKBNR w KQkq - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		int points2 = PositionEvaluation.evaluate(board2);
		assertTrue(points2 > 0, "Position 2 was not evaluated correctly.");
	}
}