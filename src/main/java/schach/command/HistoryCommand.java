package schach.command;

import schach.ConsoleMain;
import schach.Game;

/**
 * The command to show the history of the current game
 */
class HistoryCommand extends Command {

	/**
	 * The constructor of the command which calls the super constructor
	 */
	HistoryCommand(String input) {
		super(input);
	}

	@Override
	void parse() {
		if (!isValid()) return;
	}

	/**
	 * Validate if a given input is a history command
	 * 
	 * @param input the command
	 * @return true of the command is a history command
	 */
	@Override
	boolean isValid() {
		return input.equals("history");
	}

	/**
	 * Executes the function of the command
	 */
	@Override
	void execute(ConsoleMain consoleMain) {
		Game game = consoleMain.game;

		System.out.println("--- History ---");
		consoleMain.printList(game.getHistory());
	}
}
