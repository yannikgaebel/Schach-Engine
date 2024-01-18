package schach.board;

/**
 * Represents a Position on the chess board
 */
public class Position {
	private final int file;
	private final int rank;

	/**
	 * Constructor for Position
	 * 
	 * @param file
	 * @param rank
	 */
	public Position(int file, int rank) {
		if (!Position.isValid(file, rank)) {
			throw new RuntimeException("Invalid rank or file");
		}

		this.file = file;
		this.rank = rank;
	}

	/**
	 * Copy constructor
	 * 
	 * @param position
	 */
	public Position(Position position) {
		file = position.file;
		rank = position.rank;
	}

	/**
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @return the file
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Tests if the rank and file have valid values
	 * 
	 * @param file
	 * @param rank
	 * @return if the position is on the board
	 */
	public static boolean isValid(int file, int rank) {
		return file >= 0 && file < 8 && rank >= 0 && rank < 8;
	}

	/**
	 * Adds the delta to this position and return the result
	 * 
	 * @param delta
	 * @return new position with this + delta
	 */
	public Position add(PositionDelta delta) {
		return new Position(file + delta.getFileDelta(), rank + delta.getRankDelta());
	}

	/**
	 * Compares two positions
	 * 
	 * @param o the reference object with which to compare
	 * @return true if object is a position that is equal to the position it is
	 *         called from
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Position)) {
			return false;
		}

		Position p = (Position) o;
		return file == p.file && rank == p.rank;
	}

	@Override
	public int hashCode() {
		return file * 31 + rank;
	}
}
