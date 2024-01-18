package schach.board;

/**
 * Represents a PositionDelta
 *
 */
public class PositionDelta {
	private final int rankDelta;
	private final int fileDelta;

	/**
	 * Constructor for PositionDelta
	 * 
	 * @param fileDelta
	 * @param rankDelta
	 */
	public PositionDelta(int fileDelta, int rankDelta) {
		if (!PositionDelta.isValid(fileDelta, rankDelta)) {
			throw new RuntimeException("Invalid rank or file delta");
		}

		this.rankDelta = rankDelta;
		this.fileDelta = fileDelta;
	}

	/**
	 * Copy constructor
	 * 
	 * @param positionDelta
	 */
	public PositionDelta(PositionDelta positionDelta) {
		rankDelta = positionDelta.rankDelta;
		fileDelta = positionDelta.fileDelta;
	}

	/**
	 * @return the rankDelta
	 */
	public int getRankDelta() {
		return rankDelta;
	}

	/**
	 * @return the fileDelta
	 */
	public int getFileDelta() {
		return fileDelta;
	}

	/**
	 * Tests if the rank and file delta have valid values
	 * 
	 * @param fileDelta
	 * @param rankDelta
	 * @return if the PositionDelta is valid
	 */
	public static boolean isValid(int fileDelta, int rankDelta) {
		return rankDelta > -8 && rankDelta < 8 && fileDelta > -8 && fileDelta < 8;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof PositionDelta)) {
			return false;
		}

		PositionDelta p = (PositionDelta) o;
		return rankDelta == p.rankDelta && fileDelta == p.fileDelta;
	}

	@Override
	public int hashCode() {
		return rankDelta * 31 + fileDelta;
	}
}
