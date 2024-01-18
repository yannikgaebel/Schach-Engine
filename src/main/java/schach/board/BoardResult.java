package schach.board;

/**
 * BoardResult represents the result of a move
 */
public class BoardResult {

	/**
	 * The new board after the move
	 */
	public final IBoard board;

	/**
	 * A piece type which has been beaten in the move. Will be null if no piece was
	 * beaten.
	 */
	public final PieceType beaten;

	/**
	 * Constructs a new result
	 * 
	 * @param board  the new board
	 * @param beaten the piece type which has been beaten in the move
	 */
	public BoardResult(IBoard board, PieceType beaten) {
		this.board = board;
		this.beaten = beaten;
	}

	/**
	 * Constructs a new result
	 * 
	 * @param board the new board
	 */
	public BoardResult(IBoard board) {
		this(board, null);
	}
}
