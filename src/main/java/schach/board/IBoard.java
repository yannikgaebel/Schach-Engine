package schach.board;

import java.util.List;

/**
 * Interface for Board
 */
public interface IBoard {

	/**
	 * Performs a Move on a given board
	 * 
	 * @param move the move to be played
	 * @return the result including the new board and the beaten piece if any
	 * @throws BoardException throws if the move is not allowed or invalid
	 */
	BoardResult move(Move move) throws BoardException;

	/**
	 * Generates valid moves on an board
	 * 
	 * @return list of moves
	 */
	List<Move> getValidMoves();

	/**
	 * Generates valid moves on an board for a given position
	 * 
	 * @param position the position to get valid moves for
	 * @return list of moves
	 */
	List<Move> getValidMoves(Position position);

	/**
	 * Get the current status of this board
	 * 
	 * @return the status of this board
	 */
	BoardStatus getStatus();

	/**
	 * Get the side to move
	 * 
	 * @return who's turn it is
	 */
	Color getSideToMove();

	/**
	 * Tests if a piece is at the given position
	 * 
	 * @param position the position to check for a piece
	 * @return piece at the given position. null if no piece
	 */
	PieceType pieceAt(Position position);

	/**
	 * Tests if a piece of one color is at the given position
	 * 
	 * @param position the position to check for a piece
	 * @param color    the color to check for
	 * @return piece with the given color at the given position. null if no piece
	 */
	PieceType pieceAt(Position position, Color color);

	/**
	 * Get the castle rights for the given color
	 * 
	 * @param color the color
	 * @return the castle rights for color
	 */
	CastleRights getCastleRights(Color color);

	/**
	 * Get the position of the king for the given color
	 * 
	 * @param color the color to check for
	 * @return position of the king for the given color
	 */
	Position getKingPosition(Color color);
}
