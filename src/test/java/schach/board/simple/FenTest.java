package schach.board.simple;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import schach.board.*;

/**
 * Test class for {@link #Fen}
 */
public class FenTest {
	/**
	 * Test castle rights parsing.
	 */
	@Test
	public void testCastleRights() {
		Fen fen = null;
		try {
			fen = Fen.parse("4k2r/5ppp/4n3/3p4/2P5/1K6/2R5/3N4 w k - 0 1");
		} catch (BoardException e) {
			e.printStackTrace();
		}

		assertEquals(CastleRights.NoRights, fen.getFlags().getCastleRights(Color.White),
				"white should have no castle rights");
		assertEquals(CastleRights.KingSide, fen.getFlags().getCastleRights(Color.Black),
				"black should have king side castle rights");
	}

	/**
	 * Test en passant parsing.
	 */
	@Test
	public void testEnPassant() {
		Fen fen = null;
		try {
			fen = Fen.parse("r1bqkbnr/ppppp1pp/2n5/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 1");
		} catch (BoardException e) {
			e.printStackTrace();
		}

		assertEquals(new Position(5, 5), fen.getFlags().getEnPassant(), "incorrect en passant");
	}
}
