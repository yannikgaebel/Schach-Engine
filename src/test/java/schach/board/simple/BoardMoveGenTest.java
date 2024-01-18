package schach.board.simple;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import schach.board.*;

/**
 * Test class for {@link #BoardMoveGen}
 */
public class BoardMoveGenTest {
	private final static String missingMove = "missing move";
	private final static String unexpectedMove = "unexpected move";

	private Set<Move> whitePseudoValidMoves;
	private Set<Move> blackPseudoValidMoves;

	/**
	 * Setup moves before tests
	 */
	public BoardMoveGenTest() {
		whitePseudoValidMoves = new HashSet<Move>(BoardMoveGen.genPseudoMoves(new BoardDataStub(Color.White)));
		blackPseudoValidMoves = new HashSet<Move>(BoardMoveGen.genPseudoMoves(new BoardDataStub(Color.Black)));
	}

	/**
	 * Test white pawn moves straight.
	 */
	@Test
	public void testWhitePawnMovesStraight() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(1, 6), new Position(1, 7))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(2, 2), new Position(2, 3))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 4), new Position(4, 5))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(7, 1), new Position(7, 3))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(3, 2), new Position(3, 3))), unexpectedMove);
	}

	/**
	 * Test white pawn moves diagonal.
	 */
	@Test
	public void testWhitePawnMovesDiagonal() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(1, 6), new Position(0, 7))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(2, 2), new Position(3, 3))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(5, 1), new Position(6, 2))), unexpectedMove);
	}

	/**
	 * Test white pawn moves en passant.
	 */
	@Test
	public void testWhitePawnMovesEnPassant() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 4), new Position(5, 5))), missingMove);
	}

	/**
	 * Test white knight moves.
	 */
	@Test
	public void testWhiteKnightMoves() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(2, 5), new Position(3, 3))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(2, 5), new Position(4, 6))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(2, 5), new Position(2, 7))), unexpectedMove);
		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(2, 5), new Position(4, 4))), unexpectedMove);
	}

	/**
	 * Test white king moves standard.
	 */
	@Test
	public void testWhiteKingMovesStandard() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 0), new Position(3, 1))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 0), new Position(5, 0))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(4, 0), new Position(4, 2))), unexpectedMove);
		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(4, 0), new Position(3, 0))), unexpectedMove);
	}

	/**
	 * Test white king moves castle.
	 */
	@Test
	public void testWhiteKingMovesCastle() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 0), new Position(6, 0))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(4, 0), new Position(2, 0))), unexpectedMove);
		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(4, 0), new Position(1, 0))), unexpectedMove);
	}

	/**
	 * Test white bishop moves.
	 */
	@Test
	public void testWhiteBishopMoves() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 1), new Position(5, 0))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 1), new Position(5, 2))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(4, 1), new Position(6, 3))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(4, 1), new Position(3, 1))), unexpectedMove);
		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(4, 1), new Position(7, 4))), unexpectedMove);
	}

	/**
	 * Test white rook moves.
	 */
	@Test
	public void testWhiteRookMoves() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(7, 0), new Position(6, 0))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(7, 0), new Position(5, 0))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(7, 0), new Position(4, 0))), unexpectedMove);
		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(7, 0), new Position(7, 7))), unexpectedMove);
	}

	/**
	 * Test white queen moves.
	 */
	@Test
	public void testWhiteQueenMoves() {
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(3, 0), new Position(3, 1))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(3, 0), new Position(1, 0))), missingMove);
		assertTrue(whitePseudoValidMoves.contains(new Move(new Position(3, 0), new Position(0, 3))), missingMove);

		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(3, 0), new Position(4, 2))), unexpectedMove);
		assertFalse(whitePseudoValidMoves.contains(new Move(new Position(3, 0), new Position(4, 1))), unexpectedMove);
	}

	/**
	 * Test black pawn moves straight.
	 */
	@Test
	public void testBlackPawnMovesStraight() {
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(0, 5), new Position(0, 4))), missingMove);
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(5, 4), new Position(5, 3))), missingMove);
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(6, 6), new Position(6, 5))), missingMove);
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(7, 6), new Position(7, 4))), missingMove);

		assertFalse(blackPseudoValidMoves.contains(new Move(new Position(3, 3), new Position(3, 2))), unexpectedMove);
	}

	/**
	 * Test black pawn moves diagonal.
	 */
	@Test
	public void testBlackPawnMovesDiagonal() {
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(3, 3), new Position(2, 2))), missingMove);

		assertFalse(blackPseudoValidMoves.contains(new Move(new Position(5, 4), new Position(4, 3))), unexpectedMove);
	}

	/**
	 * Test black knight moves.
	 */
	@Test
	public void testBlackKnightMoves() {
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(6, 3), new Position(7, 5))), missingMove);
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(6, 3), new Position(4, 4))), missingMove);

		assertFalse(blackPseudoValidMoves.contains(new Move(new Position(6, 3), new Position(4, 1))), unexpectedMove);
	}

	/**
	 * Test black king moves standard.
	 */
	@Test
	public void testBlackKingMovesStandard() {
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(6, 7), new Position(7, 7))), missingMove);
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(6, 7), new Position(5, 6))), missingMove);

		assertFalse(blackPseudoValidMoves.contains(new Move(new Position(6, 7), new Position(5, 7))), unexpectedMove);
		assertFalse(blackPseudoValidMoves.contains(new Move(new Position(6, 7), new Position(4, 7))), unexpectedMove);
	}

	/**
	 * Test black rook moves.
	 */
	@Test
	public void testBlackRookMoves() {
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(5, 7), new Position(5, 5))), missingMove);
		assertTrue(blackPseudoValidMoves.contains(new Move(new Position(5, 7), new Position(3, 7))), missingMove);

		assertFalse(blackPseudoValidMoves.contains(new Move(new Position(5, 7), new Position(0, 7))), unexpectedMove);
		assertFalse(blackPseudoValidMoves.contains(new Move(new Position(5, 7), new Position(5, 1))), unexpectedMove);
	}
}
