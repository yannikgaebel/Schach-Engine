package schach.board.simple;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import schach.board.*;

/**
 * Test class for {@link #BoardUtil}
 */
public class BoardUtilTest {
	private final static String missingPosition = "missing position";
	private final static String incorrectPositionsAmount = "incorrect positions amount";

	private IBoardData boardDataStub;

	/**
	 * Setup board data stub before tests
	 */
	public BoardUtilTest() {
		boardDataStub = new BoardDataStub(Color.White);
	}

	/**
	 * Test get straight moves.
	 */
	@Test
	public void testGetStraightMoves() {
		List<Move> expected = Arrays.asList(new Move(new Position(0, 0), new Position(0, 1)),
				new Move(new Position(0, 0), new Position(1, 0)), new Move(new Position(0, 0), new Position(2, 0)));
		List<Move> actual = BoardUtil.getStraightMoves(boardDataStub, Color.White, new Position(0, 0));
		assertMoves(expected, actual);

		List<Move> expected2 = Arrays.asList(new Move(new Position(4, 7), new Position(3, 7)),
				new Move(new Position(4, 7), new Position(2, 7)), new Move(new Position(4, 7), new Position(1, 7)),
				new Move(new Position(4, 7), new Position(4, 6)), new Move(new Position(4, 7), new Position(4, 5)),
				new Move(new Position(4, 7), new Position(4, 4)));
		List<Move> actual2 = BoardUtil.getStraightMoves(boardDataStub, Color.Black, new Position(4, 7));
		assertMoves(expected2, actual2);
	}

	/**
	 * Test get diagonal moves.
	 */
	@Test
	public void testGetDiagonalMoves() {
		List<Move> expected = Arrays.asList(new Move(new Position(4, 1), new Position(5, 2)),
				new Move(new Position(4, 1), new Position(6, 3)), new Move(new Position(4, 1), new Position(5, 0)));
		List<Move> actual = BoardUtil.getDiagonalMoves(boardDataStub, Color.White, new Position(4, 1));
		assertMoves(expected, actual);

		List<Move> expected2 = Arrays.asList(new Move(new Position(7, 2), new Position(6, 1)));
		List<Move> actual2 = BoardUtil.getDiagonalMoves(boardDataStub, Color.Black, new Position(7, 2));
		assertMoves(expected2, actual2);
	}

	/**
	 * Test king possible positions.
	 */
	@Test
	public void testKingPossiblePositions() {
		List<Position> corner = BoardUtil.kingPossiblePositions(new Position(0, 0));
		assertTrue(corner.contains(new Position(1, 0)), missingPosition);
		assertTrue(corner.contains(new Position(0, 1)), missingPosition);
		assertTrue(corner.contains(new Position(1, 1)), missingPosition);

		List<Position> edge = BoardUtil.kingPossiblePositions(new Position(4, 0));
		assertEquals(5, edge.size(), incorrectPositionsAmount);

		List<Position> center = BoardUtil.kingPossiblePositions(new Position(4, 4));
		assertEquals(8, center.size(), incorrectPositionsAmount);
	}

	/**
	 * Test knight possible positions.
	 */
	@Test
	public void testKnightPossiblePositions() {
		List<Position> corner = BoardUtil.knightPossiblePositions(new Position(7, 7));
		assertTrue(corner.contains(new Position(5, 6)), missingPosition);
		assertTrue(corner.contains(new Position(6, 5)), missingPosition);
		assertEquals(2, corner.size(), incorrectPositionsAmount);

		List<Position> edge = BoardUtil.knightPossiblePositions(new Position(4, 0));
		assertEquals(4, edge.size(), incorrectPositionsAmount);

		List<Position> center = BoardUtil.knightPossiblePositions(new Position(4, 4));
		assertEquals(8, center.size(), incorrectPositionsAmount);
	}

	private void assertMoves(List<Move> expected, List<Move> actual) {
		assertEquals(expected.size(), actual.size(), "incorrect moves amount");

		Set<Move> expectedSet = new HashSet<Move>(expected);
		for (Move move : actual) {
			assertTrue(expectedSet.contains(move), "unexpected move");
		}
	}
}
