package schach.command;

import schach.ConsoleMain;

/**
 * The command manager class is used as a helper class for interacting with all
 * command types
 */
public final class CommandManager {

	/**
	 * Finds the command type of a give string
	 * 
	 * @param input the input string
	 * @return true if the command was executed successfully
	 */
	public static boolean execute(ConsoleMain consoleMain, String input) {
		if (executeCommand(new MoveCommand(input), consoleMain)) return true;
		if (executeCommand(new BeatenCommand(input), consoleMain)) return true;
		if (executeCommand(new HistoryCommand(input), consoleMain)) return true;
		if (executeCommand(new UndoCommand(input), consoleMain)) return true;
		if (executeCommand(new RedoCommand(input), consoleMain)) return true;

		boolean success = false;
		return success;
	}

	private static <T extends Command> boolean executeCommand(T command, ConsoleMain consoleMain) {
		if (!command.isValid()) return false;
		command.execute(consoleMain);
		return true;
	}
}
