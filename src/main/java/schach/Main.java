package schach;

import java.util.Arrays;
import java.util.List;

/**
 * Main class of the application
 */
public class Main {
	/**
	 * Main entry point of the application
	 * 
	 * @param args the launch arguments for the application
	 */
	public static void main(String[] args) {
		List<String> argList = Arrays.asList(args);
		boolean noGui = argList.contains("--no-gui");
		boolean useUnicodeSymbols = argList.contains("--unicode");
		boolean simple = argList.contains("--simple");

		if (noGui) {
			new ConsoleMain(useUnicodeSymbols, simple).startGameLoop();
			System.exit(0);
		} else {
			GuiMain.init(args);
		}
	}
}
