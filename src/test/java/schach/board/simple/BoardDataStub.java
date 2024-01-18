package schach.board.simple;

import java.util.*;

import schach.board.*;

/**
 * Stub class for {@link #BoardData}
 */
public class BoardDataStub implements IBoardData {
	// Color.White: Fen: r4rk1/1P4pp/p1N5/3pPp2/3p2n1/P1PP4/4BPPP/R2QK2R w KQ f6 0 1
	// Color.Black: Fen: r4rk1/1P4pp/p1N5/3pPp2/3p2n1/P1PP4/4BPPP/R2QK2R b KQ f6 0 1

	private Color sideToMove;

	/**
	 * Creates a new stub for BoardData
	 * 
	 * @param sideToMove the side to move
	 */
	public BoardDataStub(Color sideToMove) {
		this.sideToMove = sideToMove;
	}

	private static Map<Position, PieceType> whitePieces = new HashMap<Position, PieceType>() {
		private static final long serialVersionUID = -4069554657573464730L;

		{
			put(new Position(0, 0), PieceType.Rook);
			put(new Position(3, 0), PieceType.Queen);
			put(new Position(4, 0), PieceType.King);
			put(new Position(7, 0), PieceType.Rook);

			put(new Position(4, 1), PieceType.Bishop);
			put(new Position(5, 1), PieceType.Pawn);
			put(new Position(6, 1), PieceType.Pawn);
			put(new Position(7, 1), PieceType.Pawn);

			put(new Position(0, 2), PieceType.Pawn);
			put(new Position(2, 2), PieceType.Pawn);
			put(new Position(3, 2), PieceType.Pawn);

			put(new Position(4, 4), PieceType.Pawn);

			put(new Position(2, 5), PieceType.Knight);

			put(new Position(1, 6), PieceType.Pawn);
		}
	};

	private static Map<Position, PieceType> blackPieces = new HashMap<Position, PieceType>() {
		private static final long serialVersionUID = -5220636202291397362L;

		{
			put(new Position(3, 3), PieceType.Pawn);
			put(new Position(6, 3), PieceType.Knight);

			put(new Position(3, 4), PieceType.Pawn);
			put(new Position(5, 4), PieceType.Pawn);

			put(new Position(0, 5), PieceType.Pawn);

			put(new Position(6, 6), PieceType.Pawn);
			put(new Position(7, 6), PieceType.Pawn);

			put(new Position(0, 7), PieceType.Rook);
			put(new Position(5, 7), PieceType.Rook);
			put(new Position(6, 7), PieceType.King);
		}
	};

	private static Set<Position> pseudoAttackedByWhite = new HashSet<Position>() {
		private static final long serialVersionUID = 3453974247343015654L;

		{
			add(new Position(1, 0));
			add(new Position(2, 0));
			add(new Position(5, 0));
			add(new Position(6, 0));

			add(new Position(0, 1));
			add(new Position(2, 1));
			add(new Position(3, 1));

			add(new Position(1, 2));
			add(new Position(4, 2));
			add(new Position(5, 2));
			add(new Position(6, 2));
			add(new Position(7, 2));

			add(new Position(0, 3));
			add(new Position(1, 3));
			add(new Position(2, 3));
			add(new Position(3, 3));
			add(new Position(4, 3));
			add(new Position(6, 3));

			add(new Position(0, 4));

			add(new Position(3, 5));
			add(new Position(4, 5));
			add(new Position(5, 5));

			add(new Position(0, 6));
			add(new Position(4, 6));

			add(new Position(0, 7));
			add(new Position(1, 7));
			add(new Position(2, 7));
			add(new Position(3, 7));
		}
	};

	private static Set<Position> pseudoAttackedByBlack = new HashSet<Position>() {
		private static final long serialVersionUID = 5832441153441314922L;

		{
			add(new Position(1, 7));
			add(new Position(2, 7));
			add(new Position(3, 7));
			add(new Position(4, 7));
			add(new Position(7, 7));

			add(new Position(0, 6));
			add(new Position(5, 6));

			add(new Position(5, 5));
			add(new Position(7, 5));

			add(new Position(1, 4));
			add(new Position(4, 4));

			add(new Position(2, 3));
			add(new Position(4, 3));

			add(new Position(2, 2));
			add(new Position(4, 2));

			add(new Position(5, 1));
			add(new Position(7, 1));
		}
	};

	@Override
	public BoardFlags getFlags() {
		BoardFlags flags = new BoardFlags();
		flags.setSideToMove(sideToMove);
		flags.setCastleRights(CastleRights.Both, Color.White);
		flags.setCastleRights(CastleRights.NoRights, Color.Black);
		flags.setEnPassant(new Position(5, 5));
		flags.setHalfmoveClock(5);
		return flags;
	}

	@Override
	public boolean anyPieceAt(Position position) {
		return pieceAt(position) != null;
	}

	@Override
	public boolean anyPieceAt(Position position, Color color) {
		return pieceAt(position, color) != null;
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
		if (color == Color.White) {
			return whitePieces.get(position);
		} else {
			return blackPieces.get(position);
		}
	}

	@Override
	public int whitePiecesCount() {
		return whitePieces.size();
	}

	@Override
	public int blackPiecesCount() {
		return blackPieces.size();
	}

	@Override
	public List<Position> getPositionsOf(PieceType pieceType, Color color) {
		List<Position> positions = new ArrayList<Position>();

		if (color == Color.White) {
			for (Map.Entry<Position, PieceType> entry : whitePieces.entrySet()) {
				if (entry.getValue() == pieceType) {
					positions.add(entry.getKey());
				}
			}
		} else {
			for (Map.Entry<Position, PieceType> entry : blackPieces.entrySet()) {
				if (entry.getValue() == pieceType) {
					positions.add(entry.getKey());
				}
			}
		}

		return positions;
	}

	@Override
	public BoardDataResult newFromPseudoValidMove(Move move) throws BoardException {
		throw new RuntimeException("not implemented in stub");
	}

	@Override
	public boolean isPseudoAttacked(Position pos, Color attacker) {
		if (attacker == Color.White) {
			return pseudoAttackedByWhite.contains(pos);
		} else {
			return pseudoAttackedByBlack.contains(pos);
		}
	}
}
