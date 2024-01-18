package schach;

import java.util.ArrayList;
import java.util.List;

import schach.ai.IAI;
import schach.board.BoardException;
import schach.board.BoardResult;
import schach.board.Color;
import schach.board.IBoard;
import schach.board.Move;
import schach.board.PieceType;
import schach.board.simple.Board;

/**
 * The game class manages a chess game. It holds information about the current
 * board, past moves as well as beaten pieces.
 */
public class Game {

	private int depth;
	private int historyIndex = -1;
	private List<Move> history = new ArrayList<Move>();
	private List<PieceType> beatenWhite = new ArrayList<PieceType>();
	private List<PieceType> beatenBlack = new ArrayList<PieceType>();

	private IBoard board;
	private IAI ai;

	/**
	 * Create a new instance of a game
	 */
	public Game() {
		ai = new schach.ai.simple.AI();
		this.board = (IBoard) new Board();
	}

	/**
	 * Play a move on the current board
	 * 
	 * @param move the move to be played
	 * @throws BoardException throws if the give move is not allowed or invalid
	 */
	public void play(Move move) throws BoardException {
		// Execute move on board
		BoardResult bResult = board.move(move);

		// This point is only reached if the move is valid

		// Set current board to result board
		board = bResult.board;

		// Add beaten piece to list
		Color colorBeaten = bResult.board.getSideToMove();
		if (bResult.beaten != null) {
			if (colorBeaten == Color.White) {
				beatenWhite.add(bResult.beaten);
			} else {
				beatenBlack.add(bResult.beaten);
			}
		}

		// Save the move to the history
		addToHistory(move);

	}

	private void addToHistory(Move move) {
		boolean isLive = historyIndex + 1 == history.size();
		if (isLive) {
			history.add(move);
		} else {
			if (!history.get(historyIndex + 1).equals(move)) {
				history.subList(historyIndex + 1, history.size()).clear();
				history.add(move);
			}
		}
		historyIndex++;
	}

	/**
	 * Let the AI play the next move
	 */
	public void playAIMove() {
		try {
			play(ai.getBestMove(this.board, depth));
		} catch (BoardException e) {
			e.printStackTrace();
			// TODO exit application
		}
	}

	/**
	 * Undo the last move
	 */
	public void undo() {
		// Return if there is no move to undo
		if (historyIndex < 0) { return; }

		// Clear current board and beaten
		board = (IBoard) new Board();
		beatenWhite.clear();
		beatenBlack.clear();

		// Replay all moves from history except the last move
		for (int i = 0; i < historyIndex; i++) {
			try {
				BoardResult bResult = board.move(history.get(i));

				// Set current board to result board
				board = bResult.board;

				// Add beaten piece to list
				Color colorBeaten = bResult.board.getSideToMove();
				if (bResult.beaten != null) {
					if (colorBeaten == Color.White) {
						beatenWhite.add(bResult.beaten);
					} else {
						beatenBlack.add(bResult.beaten);
					}
				}
			} catch (BoardException e) {
				// This should never be reached since all moves in the history are valid upon
				// each other
				e.printStackTrace();
			}
		}

		// Decrease history index to point to the move before
		historyIndex--;
	}

	/**
	 * Redo the next move
	 */
	public void redo() {
		// Return if there is no move to redo
		if (history.size() <= historyIndex + 1) { return; }

		try {
			Move move = history.get(historyIndex + 1);
			play(move);
		} catch (BoardException e) {
			// This should never be reached since the next move in the history has to be
			// valid
			e.printStackTrace();
		}
	}

	public IBoard getBoard() {
		return board;
	}

	/**
	 * Returns a list of beaten pieces by color
	 * 
	 * @param color the color of the beaten pieces
	 * @return the list list of beaten pieces
	 */
	public List<PieceType> getBeaten(Color color) {
		if (color == Color.White) {
			return java.util.Collections.unmodifiableList(beatenWhite);
		} else {
			return java.util.Collections.unmodifiableList(beatenBlack);
		}
	}

	public List<Move> getHistory() {
		return java.util.Collections.unmodifiableList(history);
	}
	
	public int getHistoryIndex() {
		return historyIndex;
	}
	
	/**
	 * Set the difficulty of the AI
	 * @param difficulty the new difficulty
	 */
	public void setAIDifficulty(int difficulty) {
		depth = difficulty;
	}
}
