package schach.command;

import java.util.List;
import java.util.regex.Pattern;

import schach.ConsoleMain;
import schach.Game;
import schach.board.BoardException;
import schach.board.Move;
import schach.board.PieceType;
import schach.board.Position;

/**
 * The command to make a move on the board
 */
class MoveCommand extends Command {

	private Position fromPosition;
	private Position toPosition;

	private PieceType promotion;

	private static Pattern validPattern = Pattern.compile("[a-h][1-8]-[a-h][1-8][KQBNRP]?");

	/**
	 * The constructor of the command which calls the super constructor
	 */
	MoveCommand(String input) {
		super(input);
	}

	@Override
	void parse() {
		if (!isValid()) return;

		String[] parts = input.split("-");

		int fromFile = parts[0].charAt(0) - 'a';
		int fromRank = parts[0].charAt(1) - '1';
		fromPosition = new Position(fromFile, fromRank);

		int toFile = parts[1].charAt(0) - 'a';
		int toRank = parts[1].charAt(1) - '1';
		toPosition = new Position(toFile, toRank);

		if (parts[1].length() == 3) {
			char character = parts[1].charAt(2);
			final List<Character> piecesText = List.of('K', 'Q', 'R', 'B', 'N', 'P');
			promotion = PieceType.values()[piecesText.indexOf(character)];
		}
	}

	/**
	 * Validate if a given input is a move command
	 * 
	 * @param input the command
	 * @return true of the command is a move command
	 */
	@Override
	boolean isValid() {
		return validPattern.matcher(input).matches();
	}

	/**
	 * Executes the function of the command
	 */
	@Override
	void execute(ConsoleMain consoleMain) {
		Game game = consoleMain.game;

		try {
			game.play(toMove());
		} catch (BoardException e) {
			System.out.println("!Move not allowed");
			return;
		}

		// This point is only reached if the move is valid

		System.out.println("!" + toMove().toString());
		consoleMain.displayBoard();
		consoleMain.printBoardStatus();

		if (consoleMain.getGameType().isAIGame()) {
			game.playAIMove();
			consoleMain.displayBoard();
			consoleMain.printBoardStatus();
		}
	}

	/**
	 * Create a {@link #schach.board.Move} from the
	 * {@link #schach.command.MoveCommand}
	 * 
	 * @return a instance of a {@link #schach.board.Move}
	 */
	private Move toMove() {
		if (promotion == null) {
			return new Move(fromPosition, toPosition);
		} else {
			return new Move(fromPosition, toPosition, promotion);
		}
	}
}
