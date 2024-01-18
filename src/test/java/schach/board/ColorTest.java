package schach.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link #Color}
 */
public class ColorTest {
	/**
	 * Test the inverted color.
	 */
	@Test
	public void testInverted() {
		assertEquals(Color.White.getInverted(), Color.Black, "wrong inverted color");
		assertEquals(Color.Black.getInverted(), Color.White, "wrong inverted color");
	}
}
