package schach.board;

/**
 * Color represents the color
 */
public enum Color {
	White, Black;

	/**
	 * Calculates the inverted color
	 * 
	 * @return the inverted Color
	 */
	public Color getInverted() {
		if (this == Color.White) {
			return Color.Black;
		} else {
			return Color.White;
		}
	}
}
