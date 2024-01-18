package schach.board;

/**
 * BoardStatus represents the current status of the board
 */
public enum BoardStatus {
	Ongoing, Check, Checkmate, DrawByStalemate, DrawByInsufficientMaterial, DrawByFiftyMoveRule;

	/**
	 * Check if the game is neither a draw nor checkmate
	 * 
	 * @return !(Ongoing || Draw)
	 */
	public boolean isOver() {
		return !(this == BoardStatus.Ongoing || this == Check);
	}
}
