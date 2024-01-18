package schach.board;

/**
 * BoardException is a special exception for the chess board
 */
public class BoardException extends Exception {
	private static final long serialVersionUID = 3339795060187516491L;

	/**
	 * Constructs a new exception
	 */
	public BoardException() {
		super();
	}

	/**
	 * Constructs a new exception with a message
	 * 
	 * @param errorMessage passes a string which will be the error message
	 */
	public BoardException(String errorMessage) {
		super(errorMessage);
	}
}
