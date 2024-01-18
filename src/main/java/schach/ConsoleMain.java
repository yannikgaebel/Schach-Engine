package schach;

import java.util.List;
import java.util.Scanner;

import schach.board.Color;
import schach.board.Position;
import schach.command.CommandManager;

/**
 * This class handles user i/o for the console interface
 */
public class ConsoleMain {
	/**
	 * The game object which handles piece movement
	 */
	public Game game;

	private boolean stop = false;

	private Scanner scanner;

	private GameType gameType;

	private boolean useUnicodeSymbols = false;

	private final String[] whitePiecesText = { "K", "Q", "R", "B", "N", "P" };
	private final String[] blackPiecesText = { "k", "q", "r", "b", "n", "p" };

	private final String[] whitePiecesUnicode = { "\u2654", "\u2655", "\u2656", "\u2657", "\u2658", "\u2659" };
	private final String[] blackPiecesUnicode = { "\u265A", "\u265B", "\u265C", "\u265D", "\u265E", "\u265F" };

	/**
	 * Initialize a new game using the console UI
	 * 
	 * @param useUnicodeSymbols
	 */
	ConsoleMain(boolean useUnicodeSymbols, boolean simple) {
		this.useUnicodeSymbols = useUnicodeSymbols;

		this.scanner = new Scanner(System.in);
		game = new Game();

		if (simple) {
			gameType = GameType.LocalPlayer;
		} else {
			chooseGameType();
			if (gameType == GameType.AIB) { game.playAIMove(); }
		}
		displayBoard();
	}

	private void chooseGameType() {
		Scanner scanner = this.scanner;

		String gamemode;
		String color;
		String difficulty;

		System.out.println("Welcome!");
		do {
			System.out.println("Choose your gamemode");
			System.out.println("To play against the AI write: ai");
			System.out.println("To play against a player write: player");

			gamemode = scanner.nextLine();
		} while (!isGamemode(gamemode));

		switch (gamemode) {
			case "ai":
				System.out.println("Playing against AI");
				do {
					System.out.println("Choose your difficulty");
					System.out.println("Write a number between 2 and 5");
					System.out.println("2 stands for easy. 5 stands for hard.");

					difficulty = scanner.nextLine();
				} while (!isDifficulty(difficulty));

				game.setAIDifficulty(Integer.parseInt(difficulty));

				do {
					System.out.println("Choose your color");
					System.out.println("To play as black write: black");
					System.out.println("To play as white write: white");

					color = scanner.nextLine();
				} while (!isColor(color));

				gameType = color.equals("white") ? GameType.AIW : GameType.AIB;
				break;
			case "player":
				gameType = GameType.LocalPlayer;
				break;
		}
	}

	private boolean isGamemode(String input) {
		if (!(input.equals("ai") || input.equals("player"))) {
			System.out.println("Invalid gamemode!");
			return false;
		} else {
			return true;
		}
	}

	private boolean isColor(String input) {
		if (!(input.equals("black") || input.equals("white"))) {
			System.out.println("Invalid color!");
			return false;
		} else {
			return true;
		}
	}

	private boolean isDifficulty(String input) {
		if (!(input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5"))) {
			System.out.println("Invalid difficulty!");
			return false;
		} else {
			return true;
		}
	}

	public GameType getGameType() {
		return gameType;
	}

	/**
	 * Starts the game loop
	 */
	public void startGameLoop() {
		stop = false;
		while (!stop) {
			executeOnce();
		}
	}

	/**
	 * Stops the game loop
	 */
	public void stopGameLoop() {
		stop = true;
	}

	/**
	 * Execute the game loop once
	 */
	public void executeOnce() {
		// Get user input
		if (!scanner.hasNextLine()) return;
		String input = scanner.nextLine().strip();

		// Parse and handle input
		boolean success = CommandManager.execute(this, input);
		if (!success) {
			System.out.println("!Invalid move");
			return;
		}
	}

	/**
	 * Prints the current status of the board
	 */
	public void printBoardStatus() {
		switch (game.getBoard().getStatus()) {
			case Check:
				System.out.println("Check!");
				break;
			case Checkmate:
				System.out.println("Checkmate!");
				stopGameLoop();
				break;
			case DrawByFiftyMoveRule:
				System.out.println("Draw by fifty move rule!");
				stopGameLoop();
				break;
			case DrawByInsufficientMaterial:
				System.out.println("Draw by insufficient material!");
				stopGameLoop();
				break;
			case DrawByStalemate:
				System.out.println("Draw by stalemate!");
				stopGameLoop();
				break;
			case Ongoing:
				break;
			default:
				break;
		}
	}

	/**
	 * Print each item of the list into the console
	 * 
	 * @param <T>  the generic type of list
	 * @param list the list to be printed
	 */
	public <T> void printList(List<T> list) {
		for (T item : list) {
			System.out.println(item.toString());
		}
	}

	/**
	 * Displays the board in the console in a human readable format
	 */
	public void displayBoard() {
		final String[] whiteSymbols = this.useUnicodeSymbols ? whitePiecesUnicode : whitePiecesText;
		final String[] blackSymbols = this.useUnicodeSymbols ? blackPiecesUnicode : blackPiecesText;

		String result = "\n\n";
		for (int rank = 7; rank >= 0; rank--) {
			result += rank + 1;

			for (int file = 0; file < 8; file++) {
				result += " ";
				if (game.getBoard().pieceAt(new Position(file, rank), Color.White) != null) {
					result += whiteSymbols[game.getBoard().pieceAt(new Position(file, rank), Color.White).ordinal()];
				} else if (game.getBoard().pieceAt(new Position(file, rank), Color.Black) != null) {
					result += blackSymbols[game.getBoard().pieceAt(new Position(file, rank), Color.Black).ordinal()];
				} else {
					result += " ";
				}
			}
			result += "\n";
		}
		result += "  a b c d e f g h\n\n";

		System.out.println(result);
	}
}
