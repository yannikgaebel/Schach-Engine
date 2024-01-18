package schach.board;

import java.util.Objects;

/**
 * Represents a Move with a start position and a final position. A promotion
 * would be saved here too.
 */
public final class Move {
	private final Position from;
	private final Position to;
	private final PieceType promotion;

	/**
	 * Constructor for Move
	 * 
	 * @param from      start position of the move
	 * @param to        final position of the move
	 * @param promotion of a pawn
	 */
	public Move(Position from, Position to, PieceType promotion) {
		this.from = from;
		this.to = to;
		this.promotion = promotion;
	}

	/**
	 * Constructor without promotion
	 * 
	 * @param from start position of the move
	 * @param to   final position of the move
	 */
	public Move(Position from, Position to) {
		this(from, to, null);
	}

	/**
	 * @return the start position of the move
	 */
	public Position getFrom() {
		return from;
	}

	/**
	 * @return the final position of the move
	 */
	public Position getTo() {
		return to;
	}

	/**
	 * @return the promotion of the move
	 */
	public PieceType getPromotion() {
		return promotion;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof Move)) {
			return false;
		}

		Move m = (Move) o;
		boolean promotionEquals = (this.promotion != null ? this.promotion : PieceType.Queen)
				.equals(m.promotion != null ? m.promotion : PieceType.Queen);

		return this.from.equals(m.from) && this.to.equals(m.to) && promotionEquals;
	}

	@Override
	public int hashCode() {
		return Objects.hash(from, to, promotion != null ? promotion : PieceType.Queen);
	}

	@Override
	public String toString() {
		String result = "";
		result += Character.toString(from.getFile() + 'a');
		result += Character.toString(from.getRank() + '1');
		result += "-";
		result += Character.toString(to.getFile() + 'a');
		result += Character.toString(to.getRank() + '1');

		if (promotion != null) {
			char[] pieceChars = { 'K', 'Q', 'R', 'B', 'N', 'P' };
			result += pieceChars[promotion.ordinal()];
		}

		return result;
	}
}
