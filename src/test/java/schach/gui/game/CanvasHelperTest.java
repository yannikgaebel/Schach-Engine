package schach.gui.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import schach.board.Color;

/**
 * Test class for {@link #CanvasHelper}
 */
public class CanvasHelperTest {
	/**
	 * Test file to position.
	 */
	@Test
	public void testFileToPos() {
		CanvasHelper helperWhite = new CanvasHelper(Color.White, 400.0);
		CanvasHelper helperBlack = new CanvasHelper(Color.Black, 150.0);

		assertEquals(100.0, helperWhite.fileToPos(2));
		assertEquals(350.0, helperWhite.fileToPos(7));

		assertEquals(75.0, helperBlack.fileToPos(3));
		assertEquals(131.25, helperBlack.fileToPos(0));
	}

	/**
	 * Test rank to position.
	 */
	@Test
	public void testRankToPos() {
		CanvasHelper helperWhite = new CanvasHelper(Color.White, 400.0);
		CanvasHelper helperBlack = new CanvasHelper(Color.Black, 150.0);

		assertEquals(350.0, helperWhite.rankToPos(0));
		assertEquals(100.0, helperWhite.rankToPos(5));

		assertEquals(75.0, helperBlack.rankToPos(4));
		assertEquals(112.5, helperBlack.rankToPos(6));
	}

	/**
	 * Test position to file.
	 */
	@Test
	public void testPosToFile() {
		CanvasHelper helperWhite = new CanvasHelper(Color.White, 400.0);
		CanvasHelper helperBlack = new CanvasHelper(Color.Black, 150.0);

		assertEquals(7, helperWhite.posToFile(350.0));
		assertEquals(2, helperWhite.posToFile(100.0));

		assertEquals(0, helperBlack.posToFile(131.25));
		assertEquals(3, helperBlack.posToFile(75.0));
	}

	/**
	 * Test position to rank.
	 */
	@Test
	public void testPosToRank() {
		CanvasHelper helperWhite = new CanvasHelper(Color.White, 400.0);
		CanvasHelper helperBlack = new CanvasHelper(Color.Black, 150.0);

		assertEquals(5, helperWhite.posToRank(100.0));
		assertEquals(0, helperWhite.posToRank(350.0));

		assertEquals(6, helperBlack.posToRank(112.5));
		assertEquals(4, helperBlack.posToRank(75.0));
	}

	/**
	 * Test get cell size.
	 */
	@Test
	public void testGetCellSize() {
		assertEquals(50.0, new CanvasHelper(Color.White, 400.0).getCellSize());
		assertEquals(25.0, new CanvasHelper(Color.White, 200.0).getCellSize());
	}
}
