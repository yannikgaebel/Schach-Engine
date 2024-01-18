package schach.board.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import schach.board.*;

/**
 * BoardData saves the current state of a Board, including pieces, flags,
 * halfmoveClock
 */
class BoardData implements IBoardData {
	private long[] whitePieces;
	private long[] blackPieces;
	private long whitePiecesCombined;
	private long blackPiecesCombined;
	private long piecesCombined;

	private BoardFlags flags;

	BoardData() {
		whitePieces = Consts.initialWhitePieces.clone();
		blackPieces = Consts.initialBlackPieces.clone();
		updateCombined();

		flags = new BoardFlags();
	}

	BoardData(Fen fen) throws BoardException {
		long[] zeros = { 0, 0, 0, 0, 0, 0, 0, 0 };
		whitePieces = zeros.clone();
		blackPieces = zeros.clone();
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Position position = new Position(file, rank);

				PieceType whitePiece = fen.whitePieceAt(position);
				if (whitePiece != null) {
					setPieceAt(position, whitePiece, Color.White);
				} else {
					PieceType blackPiece = fen.blackPieceAt(position);
					if (blackPiece != null) {
						setPieceAt(position, blackPiece, Color.Black);
					}
				}
			}
		}
		updateCombined();

		flags = new BoardFlags(fen.getFlags());
	}

	private BoardData(BoardData boardData) {
		whitePieces = boardData.whitePieces.clone();
		blackPieces = boardData.blackPieces.clone();
		whitePiecesCombined = boardData.whitePiecesCombined;
		blackPiecesCombined = boardData.blackPiecesCombined;
		piecesCombined = boardData.piecesCombined;

		flags = new BoardFlags(boardData.flags);
	}

	@Override
	public BoardFlags getFlags() {
		return flags;
	}

	@Override
	public boolean anyPieceAt(Position position) {
		long pos = 1L << (position.getFile() + position.getRank() * 8);

		return (piecesCombined & pos) != 0;
	}

	@Override
	public boolean anyPieceAt(Position position, Color color) {
		long pos = 1L << (position.getFile() + position.getRank() * 8);

		if (color == Color.White) {
			return (whitePiecesCombined & pos) != 0;
		} else {
			return (blackPiecesCombined & pos) != 0;
		}
	}

	@Override
	public PieceType pieceAt(Position position) {
		PieceType whiteAt = pieceAt(position, Color.White);
		if (whiteAt != null) {
			return whiteAt;
		}

		return pieceAt(position, Color.Black);
	}

	@Override
	public PieceType pieceAt(Position position, Color color) {
		long pos = 1L << (position.getFile() + position.getRank() * 8);

		if (color == Color.White) {
			for (int i = 0; i < PieceType.values().length; i++) {
				if ((whitePieces[i] & pos) != 0) {
					return PieceType.values()[i];
				}
			}
		} else {
			for (int i = 0; i < PieceType.values().length; i++) {
				if ((blackPieces[i] & pos) != 0) {
					return PieceType.values()[i];
				}
			}
		}

		return null;
	}

	@Override
	public int whitePiecesCount() {
		return Long.bitCount(whitePiecesCombined);
	}

	@Override
	public int blackPiecesCount() {
		return Long.bitCount(blackPiecesCombined);
	}

	@Override
	public List<Position> getPositionsOf(PieceType pieceType, Color color) {
		// Fast path for king
		if (pieceType == PieceType.King) {
			long bits;
			if (color == Color.White) {
				bits = whitePieces[PieceType.King.ordinal()];
			} else {
				bits = blackPieces[PieceType.King.ordinal()];
			}
			int bitsNr = 63 - Long.numberOfLeadingZeros(bits);
			int rank = bitsNr / 8;
			int file = bitsNr % 8;
			return Arrays.asList(new Position(file, rank));
		}

		List<Position> positions = new ArrayList<Position>();

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Position position = new Position(file, rank);
				long pos = 1L << (position.getFile() + position.getRank() * 8);

				if (color == Color.White) {
					if ((whitePieces[pieceType.ordinal()] & pos) != 0) {
						positions.add(position);
					}
				} else {
					if ((blackPieces[pieceType.ordinal()] & pos) != 0) {
						positions.add(position);
					}
				}
			}
		}

		return positions;
	}

	@Override
	public BoardDataResult newFromPseudoValidMove(Move move) throws BoardException {
		// Copy data
		BoardData dataNew = new BoardData(this);

		// Apply move and get the beaten piece
		PieceType beaten = dataNew.applyPseudoValidMove(move);

		// Check if move is okay = Check if the king of the non-active player is left in
		// chess
		Position kingPosition = dataNew.getPositionsOf(PieceType.King, dataNew.getFlags().getSideToMove().getInverted())
				.get(0);
		if (dataNew.isPseudoAttacked(kingPosition, dataNew.getFlags().getSideToMove())) {
			throw new BoardException("Invalid move");
		}

		// Return the result
		return new BoardDataResult(dataNew, beaten);
	}

	/**
	 * Applies the pseudo-valid move without further checking and update all flags.
	 * 
	 * @param move the move
	 * @return the piece type that was beaten if any
	 * @throws BoardException throws if the given move is not allowed or invalid
	 */
	private PieceType applyPseudoValidMove(Move move) throws BoardException {
		// Get piece
		PieceType piece = pieceAt(move.getFrom(), flags.getSideToMove());

		// Get beaten piece
		PieceType beaten = pieceAt(move.getTo(), flags.getSideToMove().getInverted());

		// Remove from and set to
		removePieceAt(move.getFrom());
		setPieceAt(move.getTo(), piece, flags.getSideToMove());

		// Pawn ...
		if (piece == PieceType.Pawn) {
			// Take enpassant?
			if (move.getFrom().getFile() != move.getTo().getFile()) {
				int otherRankInitial = BoardUtil.initialPawnRank(flags.getSideToMove().getInverted());
				int otherRankDir = BoardUtil.pawnRankDir(flags.getSideToMove().getInverted());

				if (move.getTo().getRank() == otherRankInitial + otherRankDir && beaten == null) {
					removePieceAt(new Position(move.getTo().getFile(), otherRankInitial + otherRankDir * 2));
					beaten = PieceType.Pawn;
				}
			}

			// Apply promotion?
			if (move.getTo().getRank() == BoardUtil.promotionRank(flags.getSideToMove())) {
				setPieceAt(move.getTo(), BoardUtil.asPromotion(move.getPromotion()), flags.getSideToMove());
			}
		}

		// Move rook if move is castle
		if (piece == PieceType.King) {
			if (move.getTo().getFile() - move.getFrom().getFile() == 2) {
				removePieceAt(new Position(7, move.getFrom().getRank()));
				setPieceAt(new Position(5, move.getFrom().getRank()), PieceType.Rook, flags.getSideToMove());
			} else if (move.getTo().getFile() - move.getFrom().getFile() == -2) {
				removePieceAt(new Position(0, move.getFrom().getRank()));
				setPieceAt(new Position(3, move.getFrom().getRank()), PieceType.Rook, flags.getSideToMove());
			}
		}

		// Update en passant
		updateEnPassant(piece, move);

		// Update castle rights
		updateCastleRights(piece, beaten, move);

		// Update halfmoveClock
		updateHalfmoveClock(piece, beaten);

		// Update combined
		updateCombined();

		// Update side to move
		flags.setSideToMove(flags.getSideToMove().getInverted());

		return beaten;
	}

	private void updateEnPassant(PieceType pieceMoved, Move move) {
		if (pieceMoved == PieceType.Pawn && Math.abs(move.getTo().getRank() - move.getFrom().getRank()) == 2) {
			int rankInitial = BoardUtil.initialPawnRank(flags.getSideToMove());
			int rankDir = BoardUtil.pawnRankDir(flags.getSideToMove());

			flags.setEnPassant(new Position(move.getFrom().getFile(), rankInitial + rankDir));
		} else {
			flags.setEnPassant(null);
		}
	}

	private void updateCastleRights(PieceType pieceMoved, PieceType pieceBeaten, Move move) {
		if (pieceBeaten == PieceType.Rook) {
			int expectedRank = BoardUtil.promotionRank(flags.getSideToMove());

			if (move.getTo().equals(new Position(0, expectedRank))) {
				flags.removeQueenSideCastleRight(flags.getSideToMove().getInverted());
			} else if (move.getTo().equals(new Position(7, expectedRank))) {
				flags.removeKingSideCastleRight(flags.getSideToMove().getInverted());
			}
		} else if (pieceMoved == PieceType.King) {
			flags.setCastleRights(CastleRights.NoRights, flags.getSideToMove());
		} else if (pieceMoved == PieceType.Rook) {
			int expectedRank = BoardUtil.promotionRank(flags.getSideToMove().getInverted());

			if (move.getFrom().equals(new Position(0, expectedRank))) {
				flags.removeQueenSideCastleRight(flags.getSideToMove());
			} else if (move.getFrom().equals(new Position(7, expectedRank))) {
				flags.removeKingSideCastleRight(flags.getSideToMove());
			}
		}
	}

	private void updateHalfmoveClock(PieceType pieceMoved, PieceType pieceBeaten) {
		if (pieceMoved == PieceType.Pawn || pieceBeaten != null) {
			flags.setHalfmoveClock(0);
		} else {
			flags.setHalfmoveClock(flags.getHalfmoveClock() + 1);
		}
	}

	private void updateCombined() {
		whitePiecesCombined = whitePieces[0] | whitePieces[1] | whitePieces[2] | whitePieces[3] | whitePieces[4]
				| whitePieces[5];
		blackPiecesCombined = blackPieces[0] | blackPieces[1] | blackPieces[2] | blackPieces[3] | blackPieces[4]
				| blackPieces[5];
		piecesCombined = whitePiecesCombined | blackPiecesCombined;
	}

	private void setPieceAt(Position position, PieceType pieceType, Color color) {
		removePieceAt(position);

		long pos = 1L << (position.getFile() + position.getRank() * 8);

		if (color == Color.White) {
			whitePieces[pieceType.ordinal()] |= pos;
		} else {
			blackPieces[pieceType.ordinal()] |= pos;
		}
	}

	private void removePieceAt(Position position) {
		long pos = 1L << (position.getFile() + position.getRank() * 8);

		for (int i = 0; i < PieceType.values().length; i++) {
			whitePieces[i] &= ~pos;
			blackPieces[i] &= ~pos;
		}
	}

	@Override
	public boolean isPseudoAttacked(Position pos, Color attacker) {
		return isPseudoAttackedByKnight(pos, attacker) || isPseudoAttackedByKing(pos, attacker)
				|| isPseudoAttackedByPawn(pos, attacker) || isPseudoAttackedByStraightStepper(pos, attacker)
				|| isPseudoAttackedByDiagonalStepper(pos, attacker);
	}

	private boolean isPseudoAttackedByKnight(Position pos, Color attacker) {
		for (Position p : BoardUtil.knightPossiblePositions(pos)) {
			if (pieceAt(p, attacker) == PieceType.Knight) {
				return true;
			}
		}

		return false;
	}

	private boolean isPseudoAttackedByKing(Position pos, Color attacker) {
		for (Position p : BoardUtil.kingPossiblePositions(pos)) {
			if (pieceAt(p, attacker) == PieceType.King) {
				return true;
			}
		}

		return false;
	}

	private boolean isPseudoAttackedByPawn(Position pos, Color attacker) {
		int pawnRankDir = BoardUtil.pawnRankDir(attacker);

		boolean right = Position.isValid(pos.getFile() + 1, pos.getRank() - pawnRankDir)
				&& pieceAt(new Position(pos.getFile() + 1, pos.getRank() - pawnRankDir), attacker) == PieceType.Pawn;
		boolean left = Position.isValid(pos.getFile() - 1, pos.getRank() - pawnRankDir)
				&& pieceAt(new Position(pos.getFile() - 1, pos.getRank() - pawnRankDir), attacker) == PieceType.Pawn;

		return right || left;
	}

	private boolean isPseudoAttackedByStraightStepper(Position pos, Color attacker) {
		return isPseudoAttackedByStepper(pos, attacker, true, new PositionDelta(1, 0))
				|| isPseudoAttackedByStepper(pos, attacker, true, new PositionDelta(-1, 0))
				|| isPseudoAttackedByStepper(pos, attacker, true, new PositionDelta(0, 1))
				|| isPseudoAttackedByStepper(pos, attacker, true, new PositionDelta(0, -1));
	}

	private boolean isPseudoAttackedByDiagonalStepper(Position pos, Color attacker) {
		return isPseudoAttackedByStepper(pos, attacker, false, new PositionDelta(1, 1))
				|| isPseudoAttackedByStepper(pos, attacker, false, new PositionDelta(-1, 1))
				|| isPseudoAttackedByStepper(pos, attacker, false, new PositionDelta(1, -1))
				|| isPseudoAttackedByStepper(pos, attacker, false, new PositionDelta(-1, -1));
	}

	private boolean isPseudoAttackedByStepper(Position pos, Color attacker, boolean straight, PositionDelta delta) {
		// Initial step
		int rank = pos.getRank() + delta.getRankDelta();
		int file = pos.getFile() + delta.getFileDelta();

		// Loop
		while (true) {
			// Check invalid position
			if (!Position.isValid(file, rank)) {
				return false;
			}

			// Check piece of opponent
			if (anyPieceAt(new Position(file, rank), attacker.getInverted())) {
				return false;
			}

			// Check my piece
			PieceType myPiece = pieceAt(new Position(file, rank), attacker);

			if (myPiece != null) {
				switch (myPiece) {
				case Queen:
					return true;
				case Rook:
					return straight;
				case Bishop:
					return !straight;
				default:
					return false;
				}
			}

			// Next step
			rank += delta.getRankDelta();
			file += delta.getFileDelta();
		}
	}
}
