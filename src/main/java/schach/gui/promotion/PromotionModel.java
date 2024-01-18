package schach.gui.promotion;

import schach.board.PieceType;

/**
 * The model for the promotion interface
 */
class PromotionModel {
	private PieceType promotion;

	public PieceType getPromotion() {
		return promotion;
	}

	public void setPromotion(PieceType promotion) {
		this.promotion = promotion;
	}
}
