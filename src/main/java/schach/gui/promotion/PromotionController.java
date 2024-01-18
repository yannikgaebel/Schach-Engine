package schach.gui.promotion;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import schach.board.PieceType;

/**
 * The controller for the promotion interface. It handles input / output for the
 * promotion interface.
 */
// Suppress false positive unused methods which get called from FXML
// Suppress false positive too many methods since FXML events should not count towards method count
@SuppressWarnings({ "PMD.UnusedPrivateMethod", "PMD.TooManyMethods" })
class PromotionController {
	private PromotionModel model;

	/**
	 * The constructor of the promotion controller.
	 */
	public PromotionController() {
		model = new PromotionModel();
	}

	@FXML
	private void OnQueenPromotion(MouseEvent event) {
		model.setPromotion(PieceType.Queen);
		CloseWindow(event);
	}

	@FXML
	private void OnRookPromotion(MouseEvent event) {
		model.setPromotion(PieceType.Rook);
		CloseWindow(event);
	}

	@FXML
	private void OnBishopPromotion(MouseEvent event) {
		model.setPromotion(PieceType.Bishop);
		CloseWindow(event);
	}

	@FXML
	private void OnKnightPromotion(MouseEvent event) {
		model.setPromotion(PieceType.Knight);
		CloseWindow(event);
	}

	private void CloseWindow(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}

	PieceType getPromotion() {
		return model.getPromotion();
	}
}
