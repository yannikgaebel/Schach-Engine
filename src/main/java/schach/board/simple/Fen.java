package schach.board.simple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schach.board.*;

/**
 * Parser for the fen notation
 */
class Fen {
	private final static String errorMessage = "Invalid fen";

	private Map<Position, PieceType> whitePieces;
	private Map<Position, PieceType> blackPieces;

	private BoardFlags flags;

	private Fen(String fenString) throws BoardException {
		String[] parts = fenString.split(" ");
		if (parts.length != 6) {
			throw new BoardException(errorMessage);
		}

		// Parse
		parsePieces(parts[0]);
		parseFlags(parts[1], parts[2], parts[3], parts[4]);
	}

	PieceType whitePieceAt(Position position) {
		return whitePieces.get(position);
	}

	PieceType blackPieceAt(Position position) {
		return blackPieces.get(position);
	}

	BoardFlags getFlags() {
		return flags;
	}

	static Fen parse(String fenString) throws BoardException {
		return new Fen(fenString);
	}

	private void parsePieces(String fenPiecesPart) throws BoardException {
		whitePieces = new HashMap<Position, PieceType>();
		blackPieces = new HashMap<Position, PieceType>();

		String[] ranks = fenPiecesPart.split("/");
		if (ranks.length != 8) {
			throw new BoardException(errorMessage);
		}

		for (int rankIdx = 7; rankIdx >= 0; rankIdx--) {
			parsePiecesRank(ranks[rankIdx], 7 - rankIdx);
		}
	}

	private void parsePiecesRank(String rankString, int rank) throws BoardException {
		int file = 0;

		for (char c : rankString.toCharArray()) {
			file += parsePiecesRankChar(c, rank, file);
			if (file > 8) {
				throw new BoardException(errorMessage);
			}
		}

		if (file != 8) {
			throw new BoardException(errorMessage);
		}
	}

	private int parsePiecesRankChar(char c, int rank, int file) throws BoardException {
		PieceType asWhitePiece = parseAsWhitePiece(c);
		PieceType asBlackPiece = parseAsBlackPiece(c);

		if (asWhitePiece != null) {
			whitePieces.put(new Position(file, rank), asWhitePiece);
			return 1;
		} else if (asBlackPiece != null) {
			blackPieces.put(new Position(file, rank), asBlackPiece);
			return 1;
		} else {
			int nr;
			try {
				nr = Integer.parseInt(Character.toString(c));
			} catch (NumberFormatException e) {
				throw new BoardException(errorMessage);
			}
			if (nr < 1) {
				throw new BoardException(errorMessage);
			}
			return nr;
		}
	}

	private PieceType parseAsWhitePiece(char p) {
		final List<Character> whitePieceSymbols = List.of('K', 'Q', 'R', 'B', 'N', 'P');

		if (whitePieceSymbols.contains(p)) {
			return PieceType.values()[whitePieceSymbols.indexOf(p)];
		}

		return null;
	}

	private PieceType parseAsBlackPiece(char p) {
		final List<Character> blackPieceSymbols = List.of('k', 'q', 'r', 'b', 'n', 'p');

		if (blackPieceSymbols.contains(p)) {
			return PieceType.values()[blackPieceSymbols.indexOf(p)];
		}

		return null;
	}

	private void parseFlags(String fenSideToMovePart, String fenCastlePart, String fenEnPassantPart,
			String fenHalfmoveClockPart) throws BoardException {
		flags = new BoardFlags();

		parseFlagsSideToMove(fenSideToMovePart);
		parseCastleRights(fenCastlePart);
		parseEnPassant(fenEnPassantPart);
		parseHalfmoveClock(fenHalfmoveClockPart);
	}

	private void parseFlagsSideToMove(String fenSideToMovePart) throws BoardException {
		if (fenSideToMovePart.equals("w")) {
			flags.setSideToMove(Color.White);
		} else if (fenSideToMovePart.equals("b")) {
			flags.setSideToMove(Color.Black);
		} else {
			throw new BoardException(errorMessage);
		}
	}

	private void parseCastleRights(String fenCastlePart) throws BoardException {
		if (fenCastlePart.equals("-")) {
			flags.setCastleRights(CastleRights.NoRights, Color.White);
			flags.setCastleRights(CastleRights.NoRights, Color.Black);
		} else if (fenCastlePart.length() >= 1 && fenCastlePart.length() <= 4) {
			flags.setCastleRights(CastleRights.Both, Color.White);
			flags.setCastleRights(CastleRights.Both, Color.Black);

			char[] expected = { 'K', 'Q', 'k', 'q', };
			char[] actual = fenCastlePart.toCharArray();
			int currentExpected = 0;
			int currentActual = 0;

			while (currentActual < actual.length) {
				if (actual[currentActual] == expected[currentExpected]) {
					currentActual++;
				} else {
					removeCastleRights(expected[currentExpected]);
				}

				currentExpected++;
			}

			while (currentExpected < expected.length) {
				removeCastleRights(expected[currentExpected]);
				currentExpected++;
			}
		} else {
			throw new BoardException(errorMessage);
		}
	}

	private void removeCastleRights(char c) {
		switch (c) {
		case 'K':
			flags.removeKingSideCastleRight(Color.White);
			break;
		case 'Q':
			flags.removeQueenSideCastleRight(Color.White);
			break;
		case 'k':
			flags.removeKingSideCastleRight(Color.Black);
			break;
		case 'q':
			flags.removeQueenSideCastleRight(Color.Black);
			break;
		}
	}

	private void parseEnPassant(String fenEnPassantPart) throws BoardException {
		if (fenEnPassantPart.equals("-")) {
			flags.setEnPassant(null);
		} else if (fenEnPassantPart.length() == 2) {
			Character[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', };
			int file = Arrays.asList(chars).indexOf(fenEnPassantPart.charAt(0));
			int rank = (fenEnPassantPart.charAt(1) - '0') - 1;

			if (Position.isValid(file, rank)) {
				flags.setEnPassant(new Position(file, rank));
			} else {
				throw new BoardException(errorMessage);
			}
		} else {
			throw new BoardException(errorMessage);
		}
	}

	private void parseHalfmoveClock(String fenHalfmoveClockPart) throws BoardException {
		try {
			int halfmoveClock = Integer.parseInt(fenHalfmoveClockPart);
			if (halfmoveClock < 0 || halfmoveClock > 50) {
				throw new BoardException(errorMessage);
			}
			flags.setHalfmoveClock(halfmoveClock);
		} catch (NumberFormatException e) {
			throw new BoardException(errorMessage);
		}
	}
}
