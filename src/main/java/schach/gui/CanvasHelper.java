package schach.gui;

import schach.board.Color;

/**
 * The canvas helper allows to transform from rank / file space to screen space.
 * It also provides other utility functions to simplify canvas interaction.
 */
public class CanvasHelper {
	private Color viewSide;
	private double cellSize;

	/**
	 * Constructor of a canvas helper.
	 * 
	 * @param viewSide  the color from which to view the board
	 * @param boardSize the board size in pixel
	 */
	public CanvasHelper(Color viewSide, double boardSize) {
		this.viewSide = viewSide;
		this.cellSize = boardSize / 8;
	}

	/**
	 * Convert file to position on the canvas
	 * 
	 * @param file the file
	 * @return the position on the canvas
	 */
	public double fileToPos(int file) {
		if (viewSide == Color.White) { return file * cellSize; }
		return (7 - file) * cellSize;
	}

	/**
	 * Convert rank to position on the canvas
	 * 
	 * @param rank the rank
	 * @return the position on the canvas
	 */
	public double rankToPos(int rank) {
		if (viewSide == Color.White) { return (7 - rank) * cellSize; }
		return rank * cellSize;
	}

	/**
	 * Convert position on the canvas to file
	 * 
	 * @param pos the position on the canvas
	 * @return the file
	 */
	public int posToFile(double pos) {
		if (viewSide == Color.White) { return (int) (pos / cellSize); }
		return 7 - (int) (pos / cellSize);
	}

	/**
	 * Convert position on the canvas to rank
	 * 
	 * @param pos the position on the canvas
	 * @return the rank
	 */
	public int posToRank(double pos) {
		if (viewSide == Color.White) { return 7 - (int) (pos / cellSize); }
		return (int) (pos / cellSize);
	}

	/**
	 * Getter for cell size
	 * 
	 * @return the cell size of the chessboard
	 */
	public double getCellSize() {
		return cellSize;
	}
}
