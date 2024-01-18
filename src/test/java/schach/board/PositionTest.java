package schach.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #Position}
 */
public class PositionTest {
	private final static String positionsShouldBeEquals = "positions should be equals";
	private final static String positionShouldBeInvalid = "position should be invalid";

	/**
	 * Test all valid positions.
	 */
	@Test
	public void testValidPositions() {
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Position valid = new Position(file, rank);
				assertEquals(rank, valid.getRank(), "position get rank failed");
				assertEquals(file, valid.getFile(), "position get file failed");
			}
		}
	}

	/**
	 * Test copy constructor.
	 */
	@Test
	public void testCopyConstructor() {
		Position pos = new Position(2, 5);
		Position posCopy = new Position(pos);

		assertEquals(pos, posCopy, positionsShouldBeEquals);
		assertNotSame(pos, posCopy, "positions should not be the same");
	}

	/**
	 * Test some invalid positions. Should throw {@link #RuntimeException}.
	 */
	@Test
	public void testSomeInvalidPositions() {
		assertThrows(RuntimeException.class, () -> {
			new Position(5, -1);
		}, positionShouldBeInvalid);

		assertThrows(RuntimeException.class, () -> {
			new Position(50, 1);
		}, positionShouldBeInvalid);

		assertThrows(RuntimeException.class, () -> {
			new Position(2, 8);
		}, positionShouldBeInvalid);

		assertThrows(RuntimeException.class, () -> {
			new Position(10, 10);
		}, positionShouldBeInvalid);
	}

	/**
	 * Test {@link Position#isValid(int, int) isValid} method.
	 */
	@Test
	public void testIsValid() {
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				assertTrue(Position.isValid(file, rank), "position should be valid");
			}
		}

		assertFalse(Position.isValid(5, -2), positionShouldBeInvalid);
		assertFalse(Position.isValid(-2, 3), positionShouldBeInvalid);
		assertFalse(Position.isValid(8, 8), positionShouldBeInvalid);
	}

	/**
	 * Test {@link Position#add(PositionDelta) add} method.
	 */
	@Test
	public void testAdd() {
		assertEquals(new Position(3, 4), new Position(0, 5).add(new PositionDelta(3, -1)), positionsShouldBeEquals);
		assertEquals(new Position(5, 7), new Position(5, 2).add(new PositionDelta(0, 5)), positionsShouldBeEquals);

		assertThrows(RuntimeException.class, () -> {
			new Position(3, 5).add(new PositionDelta(2, 4));
		}, positionShouldBeInvalid);
	}

	/**
	 * Test equality of two positions.
	 */
	@Test
	public void testEquals() {
		assertTrue(new Position(2, 2).equals(new Position(2, 2)), positionsShouldBeEquals);
		assertTrue(new Position(7, 1).equals(new Position(7, 1)), positionsShouldBeEquals);

		assertFalse(new Position(3, 3).equals(new Position(2, 2)), "positions should be not equals");
		assertFalse(new Position(4, 2).equals(new Position(2, 4)), "positions should be not equals");
	}
}
