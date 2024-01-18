package schach.command;

import schach.ConsoleMain;
import schach.Game;
import schach.GameType;

/**
 * The command to undo a move
 */
public class UndoCommand extends Command {

	/**
	* The constructor of the command which calls the super constructor
	*/
	UndoCommand(String input) {
		super(input);
	}

	@Override
	void parse() {
		if (!isValid()) return;
	}

	/**
	* Validate if a given input is a undo command
	* 
	* @param input the command
	* @return true of the command is a undo command
	*/
	@Override
	boolean isValid() {
		return input.equals("undo");
	}

	/**
	* Executes the function of the command
	*/
	@Override
	void execute(ConsoleMain consoleMain) {
		Game game = consoleMain.game;
		
		game.undo();

		// Undo twice when playing against AI
		if (consoleMain.getGameType() == GameType.AIB || consoleMain.getGameType()  == GameType.AIW) { game.undo(); }
		
		consoleMain.displayBoard();
	}
}
