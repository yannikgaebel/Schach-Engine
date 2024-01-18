package schach.board.simple;

import schach.board.*;

/**
 * BoardFlags represents the sideToMove, castle rights, en passant and
 * halfmoveClock.
 */
class BoardFlags {
	private Color sideToMove;
	private CastleRights castleRightsWhite;
	private CastleRights castleRightsBlack;
	private Position enPassant;
	private int halfmoveClock;

	BoardFlags() {
		sideToMove = Color.White;
		castleRightsWhite = CastleRights.Both;
		castleRightsBlack = CastleRights.Both;
		enPassant = null;
		halfmoveClock = 0;
	}

	BoardFlags(BoardFlags boardFlags) {
		sideToMove = boardFlags.sideToMove;
		castleRightsWhite = boardFlags.castleRightsWhite;
		castleRightsBlack = boardFlags.castleRightsBlack;
		if (boardFlags.enPassant != null) {
			enPassant = new Position(boardFlags.enPassant);
		}
		halfmoveClock = boardFlags.halfmoveClock;
	}

	/**
	 * @return the sideToMove
	 */
	Color getSideToMove() {
		return sideToMove;
	}

	/**
	 * @param sideToMove the sideToMove to set
	 */
	void setSideToMove(Color sideToMove) {
		this.sideToMove = sideToMove;
	}

	/**
	 * @return the enPassant
	 */
	Position getEnPassant() {
		return enPassant;
	}

	/**
	 * @param enPassant the enPassant to set
	 */
	void setEnPassant(Position enPassant) {
		this.enPassant = enPassant;
	}

	/**
	 * Returns the number of halfmoves since the last capture or pawn advance.
	 * 
	 * @return the halfmoveClock
	 */
	int getHalfmoveClock() {
		return halfmoveClock;
	}

	/**
	 * @param halfmoveClock the halfmoveClock to set
	 */
	void setHalfmoveClock(int halfmoveClock) {
		this.halfmoveClock = halfmoveClock;
	}

	/**
	 * @param color the color
	 * @return the castleRights for color
	 */
	CastleRights getCastleRights(Color color) {
		if (color == Color.White) {
			return castleRightsWhite;
		} else {
			return castleRightsBlack;
		}
	}

	/**
	 * @param castleRights the castleRights to set for color
	 * @param color        the color
	 */
	void setCastleRights(CastleRights castleRights, Color color) {
		if (color == Color.White) {
			this.castleRightsWhite = castleRights;
		} else {
			this.castleRightsBlack = castleRights;
		}
	}

	void removeQueenSideCastleRight(Color color) {
		if (getCastleRights(color) == CastleRights.Both) {
			setCastleRights(CastleRights.KingSide, color);
		} else if (getCastleRights(color) == CastleRights.QueenSide) {
			setCastleRights(CastleRights.NoRights, color);
		}
	}

	void removeKingSideCastleRight(Color color) {
		if (getCastleRights(color) == CastleRights.Both) {
			setCastleRights(CastleRights.QueenSide, color);
		} else if (getCastleRights(color) == CastleRights.KingSide) {
			setCastleRights(CastleRights.NoRights, color);
		}
	}
}
