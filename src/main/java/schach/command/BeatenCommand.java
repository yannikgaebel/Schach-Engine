package schach.command;

import java.util.List;

import schach.ConsoleMain;
import schach.Game;
import schach.board.Color;
import schach.board.PieceType;

/**
 * The command to show a list of beaten pieces
 */
class BeatenCommand extends Command {

	/**
	 * The constructor of the command which calls the super constructor
	 */
	BeatenCommand(String input) {
		super(input);
	}

	@Override
	void parse() {
		if (!isValid()) return;
	}

	/**
	 * Validate if a given input is a move command
	 * 
	 * @param input the command
	 * @return true of the command is a move command
	 */
	@Override
	boolean isValid() {
		return input.equals("beaten");
	}

	/**
	 * Executes the function of the command
	 */
	@Override
	void execute(ConsoleMain consoleMain) {
		Game game = consoleMain.game;

		System.out.println("--- Beaten pieces ---");

		List<PieceType> beatenWhite = game.getBeaten(Color.White);
		if (!beatenWhite.isEmpty()) {
			System.out.println("White:");
			consoleMain.printList(beatenWhite);
		}

		List<PieceType> beatenBlack = game.getBeaten(Color.Black);
		if (!beatenBlack.isEmpty()) {
			System.out.println("Black:");
			consoleMain.printList(game.getBeaten(Color.Black));
		}
	}
}
