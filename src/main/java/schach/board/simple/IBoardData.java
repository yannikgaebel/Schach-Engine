package schach.board.simple;

import java.util.List;

import schach.board.*;

/**
 * Interface for BoardData
 */
interface IBoardData {
	/**
	 * @return the flags
	 */
	BoardFlags getFlags();

	/**
	 * Tests if any piece is at the given position
	 * 
	 * @param position the position to check for a piece
	 * @return whether there is any piece at the given position
	 */
	boolean anyPieceAt(Position position);

	/**
	 * Tests if any piece of one color is at the given position
	 * 
	 * @param position the position to check for a piece
	 * @param color    the color to check for
	 * @return whether there is any piece with the given color at the given position
	 */
	boolean anyPieceAt(Position position, Color color);

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
	 * @return the amount of white pieces
	 */
	int whitePiecesCount();

	/**
	 * @return the amount of black pieces
	 */
	int blackPiecesCount();

	/**
	 * Get all positions of a pieceType and color.
	 * 
	 * @param pieceType the piece to check for
	 * @param color     the color to check for
	 * @return all positions of pieceType and color
	 */
	List<Position> getPositionsOf(PieceType pieceType, Color color);

	/**
	 * Applies the pseudo-valid move and update all flags on a new board. If the
	 * move is not valid, this will throw an exception. The move has to be at least
	 * pseudo-valid otherwise a valid board cannot be guaranteed.
	 * 
	 * @param move the move to be played
	 * @return the BoardResult with the new board and the piece type that was beaten
	 *         if any
	 * @throws BoardException throws if the move is pseudo valid but not valid
	 */
	BoardDataResult newFromPseudoValidMove(Move move) throws BoardException;

	/**
	 * Checks if a position is pseudo attacked by one side.
	 * 
	 * @param position the position to check for
	 * @param attacker the color of the attacker
	 * @return whether the given position is pseudo attacked by the given color
	 */
	boolean isPseudoAttacked(Position position, Color attacker);
}
