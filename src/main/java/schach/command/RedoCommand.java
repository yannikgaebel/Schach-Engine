package schach.command;

import schach.ConsoleMain;
import schach.Game;
import schach.GameType;

/**
 * The command to redo a move
 */
public class RedoCommand extends Command {

	/**
	* The constructor of the command which calls the super constructor
	*/
	RedoCommand(String input) {
		super(input);
	}

	@Override
	void parse() {
		if (!isValid()) return;
	}

	/**
	* Validate if a given input is a redo command
	* 
	* @param input the command
	* @return true of the command is a redo command
	*/
	@Override
	boolean isValid() {
		return input.equals("redo");
	}

	/**
	* Executes the function of the command
	*/
	@Override
	void execute(ConsoleMain consoleMain) {
		Game game = consoleMain.game;
		
		game.redo();

		// Redo twice when playing against AI
		if (consoleMain.getGameType() == GameType.AIB || consoleMain.getGameType()  == GameType.AIW) { game.redo(); }
		
		consoleMain.displayBoard();
	}
}
