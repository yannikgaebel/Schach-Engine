package schach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Test class for ConsoleMain
 */
public class ConsoleMainTest {
	private ConsoleMain cliMain;

	/**
	 * Test console output for various inputs
	 */
	@Test
	public void testConsoleText() {
		// Test input
		String inputText = "player\ne2-e4\nd7-d5Q\nundo\nredo\ne4-e6\ne4-e20\ne4-d5\nbeaten\nhistory";

		// Preparation
		byte[] inputBytes = inputText.getBytes(StandardCharsets.UTF_8);
		InputStream inputStream = new ByteArrayInputStream(inputBytes);
		System.setIn(inputStream);

		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		cliMain = new ConsoleMain(false, false);

		testOutputStartsWith(outContent, "!e2-e4"); // e2-e4
		testOutputStartsWith(outContent, "!d7-d5Q"); // d7-d5Q
		cliMain.executeOnce(); // undo
		cliMain.executeOnce(); // redo
		testOutputStartsWith(outContent, "!Move not allowed"); // e4-e6
		testOutputStartsWith(outContent, "!Invalid move"); // e4-e20
		testOutputStartsWith(outContent, "!e4-d5"); // e4-d5

		outContent.reset();
		cliMain.executeOnce(); // beaten
		assertEquals("--- Beaten pieces ---" + "Black:" + "Pawn", prepareMultilineString(outContent.toString()));

		outContent.reset();
		cliMain.executeOnce(); // history
		assertEquals("--- History ---" + "e2-e4" + "d7-d5Q" + "e4-d5", prepareMultilineString(outContent.toString()));

		cliMain.stopGameLoop();

		// Cleanup
		System.setIn(System.in);
		System.setOut(System.out);
	}

	private void testOutputStartsWith(ByteArrayOutputStream outContent, String expected) {
		outContent.reset();
		cliMain.executeOnce();
		assertTrue(outContent.toString().startsWith(expected));
	}

	private String prepareMultilineString(String input) {
		return input.replace("\n", "").replace("\r", "");
	}
}
