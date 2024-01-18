package schach.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #PieceType}
 */
public class PieceTypeTest {
	/**
	 * Test the amount of piece types.
	 */
	@Test
	public void testLength() {
		assertEquals(6, PieceType.values().length, "wrong piece type count");
	}

	/**
	 * Test the ordinals of the enum values.
	 */
	@Test
	public void testOrdinals() {
		int[] expected = { 0, 1, 2, 3, 4, 5 };
		int[] actual = { PieceType.King.ordinal(), PieceType.Queen.ordinal(), PieceType.Rook.ordinal(),
				PieceType.Bishop.ordinal(), PieceType.Knight.ordinal(), PieceType.Pawn.ordinal() };

		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i], "wrong ordinals");
		}
	}
}
