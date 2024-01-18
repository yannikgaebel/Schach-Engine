package schach.gui.promotion;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import schach.board.Color;
import schach.board.PieceType;
import schach.gui.ImageManager;

/**
 * The view for the promotion interface
 */
public class PromotionView {

	PromotionController controller;

	/**
	 * Constructor of the promotion view
	 * 
	 * @throws IOException
	 */
	public PromotionView(Color promotionColor) throws IOException {
		controller = new PromotionController();

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/promotionGUI.fxml"));
		fxmlLoader.setController(controller);
		Parent root = (Parent) fxmlLoader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();

		ImageView queenImage = (ImageView) root.lookup("#queenPromotion");
		ImageView rookImage = (ImageView) root.lookup("#rookPromotion");
		ImageView bishopImage = (ImageView) root.lookup("#bishopPromotion");
		ImageView knightImage = (ImageView) root.lookup("#knightPromotion");

		String iconColor = promotionColor == Color.White ? "white" : "black";
		ImageManager imgManager = ImageManager.getInstance();
		queenImage.setImage(imgManager.getImage(iconColor + "Queen"));
		rookImage.setImage(imgManager.getImage(iconColor + "Rook"));
		bishopImage.setImage(imgManager.getImage(iconColor + "Bishop"));
		knightImage.setImage(imgManager.getImage(iconColor + "Knight"));

		stage.setScene(scene);
		stage.setResizable(false);
		stage.showAndWait();
	}

	/**
	 * Getter for the promotion type result.
	 * 
	 * @return The selected promotion type 
	 */
	public PieceType getPromotion() {
		return controller.getPromotion();
	}
}
