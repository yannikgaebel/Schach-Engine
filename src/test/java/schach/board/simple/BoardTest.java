package schach.board.simple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import schach.board.*;

/**
 * Test class for {@link #Board}
 */
public class BoardTest {
	/**
	 * Test start position of the board.
	 */
	@Test
	public void testStartposition() {
		IBoard board = new Board();

		assertEquals(PieceType.Rook, board.pieceAt(new Position(0, 0), Color.White), "expected white rook");
		assertEquals(PieceType.King, board.pieceAt(new Position(4, 0), Color.White), "expected white king");

		assertEquals(PieceType.Bishop, board.pieceAt(new Position(2, 7), Color.Black), "expected black bishop");
		assertEquals(PieceType.Pawn, board.pieceAt(new Position(4, 6), Color.Black), "expected black pawn");

		assertNull(board.pieceAt(new Position(3, 5)), "expected null");
	}

	/**
	 * Test fen parsing.
	 */
	@Test
	public void testFenParsing() {
		try {
			new Board("rnbqkb1r/pp3ppp/4pn2/3p4/2PP4/2N5/PP3PPP/R1BQKBNR w KQkq - 1 6");
			new Board("rnbqk2r/pppp1ppp/4pn2/8/1bPP4/5N2/PP2PPPP/RNBQKB1R w KQkq - 3 4");
			new Board("rnbqkbnr/pp2pppp/2p5/3p4/2PP4/8/PP2PPPP/RNBQKBNR w KQkq - 0 3");
			new Board("r4rk1/1P4pp/p1N5/3pPp2/3p2n1/P1PP4/4BPPP/R2QK2R b KQ f6 0 1");
		} catch (BoardException e) {
			fail(e);
		}
	}

	/**
	 * Test pawn moves.
	 */
	@Test
	public void testPawnMoves() {
		IBoard board = new Board();

		List<Move> expected = Arrays.asList(new Move(new Position(0, 1), new Position(0, 2)),
				new Move(new Position(0, 1), new Position(0, 3)));
		List<Move> actual = board.getValidMoves(new Position(0, 1));
		assertMoves(expected, actual);
	}

	/**
	 * Test pawn beat moves.
	 */
	@Test
	public void testPawnBeat() {
		// Create position
		IBoard board = null;
		try {
			board = new Board().move(new Move(new Position(0, 1), new Position(0, 3))).board
					.move(new Move(new Position(0, 6), new Position(0, 5))).board
							.move(new Move(new Position(1, 1), new Position(1, 2))).board
									.move(new Move(new Position(1, 6), new Position(1, 4))).board;
		} catch (BoardException e) {
			fail(e);
		}

		// Check moves
		List<Move> expected = Arrays.asList(new Move(new Position(0, 3), new Position(0, 4)),
				new Move(new Position(0, 3), new Position(1, 4)));
		List<Move> actual = board.getValidMoves(new Position(0, 3));
		assertMoves(expected, actual);

		// Check beaten
		BoardResult boardResult = null;
		try {
			boardResult = board.move(new Move(new Position(0, 3), new Position(1, 4)));
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(boardResult.beaten, PieceType.Pawn, "incorrect beaten piece type");
	}

	/**
	 * Test en passant moves.
	 */
	@Test
	public void testEnPassant() {
		// Create position
		IBoard board = null;
		try {
			board = new Board().move(new Move(new Position(0, 1), new Position(0, 3))).board
					.move(new Move(new Position(0, 6), new Position(0, 5))).board
							.move(new Move(new Position(0, 3), new Position(0, 4))).board
									.move(new Move(new Position(1, 6), new Position(1, 4))).board;
		} catch (BoardException e) {
			fail(e);
		}

		// En passant move
		Move enPassantMove = new Move(new Position(0, 4), new Position(1, 5));

		// Check moves
		List<Move> expected = Arrays.asList(enPassantMove);
		List<Move> actual = board.getValidMoves(new Position(0, 4));
		assertMoves(expected, actual);

		// Check beaten
		BoardResult boardResult = null;
		try {
			boardResult = board.move(enPassantMove);
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(boardResult.beaten, PieceType.Pawn, "incorrect beaten piece type");
	}

	/**
	 * Test knight moves.
	 */
	@Test
	public void testKnightMoves() {
		IBoard board = new Board();

		// Check moves
		List<Move> expected = Arrays.asList(new Move(new Position(1, 0), new Position(2, 2)),
				new Move(new Position(1, 0), new Position(0, 2)));
		List<Move> actual = board.getValidMoves(new Position(1, 0));
		assertMoves(expected, actual);
	}

	/**
	 * Test castling.
	 */
	@Test
	public void testCastling() {
		// Legal
		IBoard board = null;
		try {
			board = new Board().move(new Move(new Position(1, 1), new Position(1, 3))).board
					.move(new Move(new Position(3, 6), new Position(3, 5))).board
							.move(new Move(new Position(1, 0), new Position(0, 2))).board
									.move(new Move(new Position(2, 7), new Position(5, 4))).board
											.move(new Move(new Position(2, 0), new Position(1, 1))).board
													.move(new Move(new Position(0, 6), new Position(0, 5))).board.move(
															new Move(new Position(2, 1), new Position(2, 2))).board
																	.move(new Move(new Position(0, 5),
																			new Position(0, 4))).board.move(new Move(
																					new Position(3, 0),
																					new Position(1, 2))).board.move(
																							new Move(new Position(0, 4),
																									new Position(0,
																											3))).board;
		} catch (BoardException e) {
			fail(e);
		}

		// Check moves
		List<Move> expected = Arrays.asList(new Move(new Position(4, 0), new Position(3, 0)),
				new Move(new Position(4, 0), new Position(2, 0)));
		List<Move> actual = board.getValidMoves(new Position(4, 0));
		assertMoves(expected, actual);

		// Illegal
		IBoard board2 = null;
		try {
			board2 = board.move(new Move(new Position(4, 1), new Position(4, 2))).board
					.move(new Move(new Position(5, 4), new Position(6, 3))).board;
		} catch (BoardException e) {
			fail(e);
		}

		// Check moves
		assertEquals(0, board2.getValidMoves(new Position(4, 0)).size(), "incorrect move found for king");
	}

	/**
	 * Test castling over attacked field.
	 */
	@Test
	public void testCastlingOverAttackedField() {
		// Parse board
		IBoard board = null;
		try {
			board = new Board("rn1qkbnr/p1pp1ppp/bp6/4p3/4P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1");
		} catch (BoardException e) {
			fail(e);
		}

		// Check moves
		assertEquals(0, board.getValidMoves(new Position(4, 0)).size(), "incorrect move found for king");
	}

	/**
	 * Test pawn promotion.
	 */
	@Test
	public void testPromotion() {
		IBoard board = null;
		try {
			board = new Board("8/2PPP3/5bn1/8/K7/5BN1/8/k7 w - - 0 1")
					.move(new Move(new Position(2, 6), new Position(2, 7))).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board.pieceAt(new Position(2, 7), Color.White), PieceType.Queen,
				"Promotion to Queen did not work");

		IBoard board2 = null;
		try {
			board2 = new Board("8/2PPP3/5bn1/8/K7/5BN1/8/k7 w - - 0 1")
					.move(new Move(new Position(2, 6), new Position(2, 7), PieceType.Rook)).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board2.pieceAt(new Position(2, 7), Color.White), PieceType.Rook, "Promotion to Rook did not work");

		IBoard board3 = null;
		try {
			board3 = new Board("k3bn2/8/8/8/8/8/K2p4/8 b - - 0 1")
					.move(new Move(new Position(3, 1), new Position(3, 0))).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board3.pieceAt(new Position(3, 0), Color.Black), PieceType.Queen,
				"Promotion to Queen for Black did not work");
	}

	/**
	 * Test checkmate
	 */
	@Test
	public void testCheckmate() {
		IBoard board = null;
		try {
			board = new Board("8/2PPP3/5bn1/8/K7/5BN1/3R3R/k7 w - - 0 1")
					.move(new Move(new Position(3, 1), new Position(3, 0))).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board.getStatus(), BoardStatus.Checkmate, "Checkmate Position not registered");

		IBoard board2 = null;
		try {
			board2 = new Board("8/8/5n2/8/8/2k5/8/2K3r1 w - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board2.getStatus(), BoardStatus.Checkmate, "Checkmate Position not registered");

		IBoard board3 = null;
		try {
			board3 = new Board("8/r6R/1pkp1n2/1ppp4/3N4/8/4K3/2R5 b - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board3.getStatus(), BoardStatus.Checkmate, "Checkmate Position not registered");
	}

	/**
	 * Test draw by stalemate.
	 */
	@Test
	public void testDrawByStalemate() {
		IBoard board = null;
		try {
			board = new Board("8/8/8/8/8/1q6/2k5/K7 w - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board.getStatus(), BoardStatus.DrawByStalemate, "Stalemate Position not registered");

		IBoard board2 = null;
		try {
			board2 = new Board("5k2/R4p1K/p4N2/P5P1/8/8/8/8 b - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board2.getStatus(), BoardStatus.DrawByStalemate, "Stalemate Position not registered");

		IBoard board3 = null;
		try {
			board3 = new Board("kBn4K/P1P5/8/7n/8/8/b7/1b6 w - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board3.getStatus(), BoardStatus.DrawByStalemate, "Stalemate Position not registered");
	}

	/**
	 * Test draw by insufficient material.
	 */
	@Test
	public void testDrawByInsufficientMaterial() {
		IBoard board = null;
		try {
			board = new Board("8/8/8/8/K7/8/8/k7 w - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board.getStatus(), BoardStatus.DrawByInsufficientMaterial,
				"Insufficient material Position not registered (King vs King)");

		IBoard board2 = null;
		try {
			board2 = new Board("k3b3/8/8/8/8/8/8/K3B3 w - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board2.getStatus(), BoardStatus.DrawByInsufficientMaterial,
				"Insufficient material Position not registered (King+Bishop vs King+Bishop)");

		IBoard board3 = null;
		try {
			board3 = new Board("k3b3/8/8/8/8/8/8/K7 w - - 0 1");
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board3.getStatus(), BoardStatus.DrawByInsufficientMaterial,
				"Insufficient material Position not registered (King vs King+Bishop)");
	}

	/**
	 * Test draw by fifty move rule.
	 */
	@Test
	public void testDrawByFiftyMoveRule() {
		// 50. move without pawn advance or capture --> DrawByFiftyMoveRule
		IBoard board = null;
		try {
			board = new Board("1k6/2R2n2/8/1K5p/7P/8/1N6/2B5 w - - 49 120")
					.move(new Move(new Position(1, 1), new Position(3, 2))).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board.getStatus(), BoardStatus.DrawByFiftyMoveRule, "Draw by fifty move rule not registered");

		// 50. move = capture --> Ongoing
		IBoard board2 = null;
		try {
			board2 = new Board("1k6/2R2n2/8/1K5p/7P/8/1N6/2B5 w - - 49 120")
					.move(new Move(new Position(2, 6), new Position(5, 6))).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(board2.getStatus(), BoardStatus.Ongoing, "Ongoing Position not registered");
	}

	/**
	 * Test some simple checkmates.
	 */
	@Test
	public void testSimpleCheckmates() {
		// Scholar's mate
		IBoard boardScholarsMate = null;
		try {
			boardScholarsMate = new Board().move(new Move(new Position(4, 1), new Position(4, 3))).board
					.move(new Move(new Position(4, 6), new Position(4, 4))).board
							.move(new Move(new Position(5, 0), new Position(2, 3))).board
									.move(new Move(new Position(1, 7), new Position(2, 5))).board
											.move(new Move(new Position(3, 0), new Position(7, 4))).board
													.move(new Move(new Position(6, 7), new Position(5, 5))).board.move(
															new Move(new Position(7, 4), new Position(5, 6))).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(boardScholarsMate.getStatus(), BoardStatus.Checkmate, "Scholar's mate not registered");

		// Fool's mate
		IBoard boardFoolsMate = null;
		try {
			boardFoolsMate = new Board().move(new Move(new Position(5, 1), new Position(5, 2))).board
					.move(new Move(new Position(4, 6), new Position(4, 4))).board
							.move(new Move(new Position(6, 1), new Position(6, 3))).board
									.move(new Move(new Position(3, 7), new Position(7, 3))).board;
		} catch (BoardException e) {
			fail(e);
		}
		assertEquals(boardFoolsMate.getStatus(), BoardStatus.Checkmate, "Fool's mate not registered");
	}

	private void assertMoves(List<Move> expected, List<Move> actual) {
		assertEquals(expected.size(), actual.size(), "incorrect moves amount");

		Set<Move> expectedSet = new HashSet<Move>(expected);
		for (Move move : actual) {
			assertTrue(expectedSet.contains(move), "unexpected move");
		}
	}
}