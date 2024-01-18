package schach.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #Move}
 */
public class MoveTest {
	private final static String errorMovesShouldBeEquals = "moves should be equals";
	private final static String errorMovesShouldNotBeEquals = "moves should not be equals";
	private final static String errorToStringFailed = "toString method failed";
	
	/**
	 * Test the constructor, including from, to and promotion.
	 */
	@Test
	public void testConstructor() {
		Move move1 = new Move(new Position(3, 2), new Position(4, 3));
		assertEquals(new Position(3, 2), move1.getFrom(), "from failed");
		assertEquals(new Position(4, 3), move1.getTo(), "to failed");
		assertNull(move1.getPromotion(), "promotion failed");

		Move move2 = new Move(new Position(0, 0), new Position(1, 7), PieceType.Rook);
		assertEquals(PieceType.Rook, move2.getPromotion(), "promotion failed");
	}

	/**
	 * Test equality of equal two moves.
	 */
	@Test
	public void testEquals() {
		Move move1 = new Move(new Position(3, 2), new Position(4, 3));
		Move move2 = new Move(new Position(3, 2), new Position(4, 3), PieceType.Queen);

		assertEquals(new Move(new Position(3, 2), new Position(4, 3)), move1, errorMovesShouldBeEquals);
		assertEquals(new Move(new Position(3, 2), new Position(4, 3), null), move1, errorMovesShouldBeEquals);
		assertEquals(new Move(new Position(3, 2), new Position(4, 3), PieceType.Queen), move2,
				errorMovesShouldBeEquals);
		assertEquals(move1, move2, errorMovesShouldBeEquals);
	}
	
	/**
	 * Test equality of different two moves.
	 */
	@Test
	public void testEquals2() {
		Move move1 = new Move(new Position(3, 2), new Position(4, 3));
		Move move2 = new Move(new Position(3, 2), new Position(4, 3), PieceType.Rook);
		Move move3 = new Move(new Position(4, 1), new Position(6, 5));
		
		assertNotEquals(move2, move3, errorMovesShouldNotBeEquals);
		assertNotEquals(move1, move2, errorMovesShouldNotBeEquals);
		assertNotEquals(new Move(new Position(3, 2), new Position(4, 3)), move2, errorMovesShouldNotBeEquals);
	}
	
	/**
	 * Test the toString method.
	 */
	@Test
	public void testToString() {
		Move move1 = new Move(new Position(3, 2), new Position(4, 3));
		Move move2 = new Move(new Position(3, 2), new Position(4, 3), PieceType.Rook);
		Move move3 = new Move(new Position(4, 1), new Position(6, 5));
		
		assertEquals(move1.toString(), "d3-e4", errorToStringFailed);
		assertEquals(move2.toString(), "d3-e4R", errorToStringFailed);
		assertEquals(move3.toString(), "e2-g6", errorToStringFailed);
	}
}
