package schach.gui.main;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import schach.GameType;
import schach.gui.game.GameView;
import javafx.event.ActionEvent;

/**
 * The controller for the main menu
 */
// Suppress false positive unused methods which get called from FXML
// Suppress false positive too many methods since FXML events should not count towards method count
@SuppressWarnings({ "PMD.UnusedPrivateMethod", "PMD.TooManyMethods" })
public class MainController {

	@FXML
	private VBox selectColor;

	@FXML
	private VBox selectGamemode;

	@FXML
	private VBox selectNetworkType;

	@FXML
	private VBox selectEndpoint;

	@FXML
	private VBox selectDifficulty;

	@FXML
	private Slider difficultySlider;

	@FXML
	private TextField hostAddress;

	private void showGameView(ActionEvent event, GameType gameType, int difficulty) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		try {
			String endpoint = hostAddress.getText();
			new GameView(stage, gameType, endpoint, difficulty);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void chooseGamemode() {
		selectGamemode.setVisible(true);
		selectColor.setVisible(false);
		selectNetworkType.setVisible(false);
		selectEndpoint.setVisible(false);
		selectDifficulty.setVisible(false);
	}

	@FXML
	private void chooseColor() {
		selectGamemode.setVisible(false);
		selectColor.setVisible(true);
		selectNetworkType.setVisible(false);
		selectEndpoint.setVisible(false);
		selectDifficulty.setVisible(false);
	}

	@FXML
	private void chooseNetworkType() {
		selectGamemode.setVisible(false);
		selectColor.setVisible(false);
		selectNetworkType.setVisible(true);
		selectEndpoint.setVisible(false);
		selectDifficulty.setVisible(false);
	}

	@FXML
	private void chooseEndpoint() {
		selectGamemode.setVisible(false);
		selectColor.setVisible(false);
		selectNetworkType.setVisible(false);
		selectEndpoint.setVisible(true);
		selectDifficulty.setVisible(false);
	}

	@FXML
	private void chooseDifficulty() {
		selectGamemode.setVisible(false);
		selectColor.setVisible(false);
		selectNetworkType.setVisible(false);
		selectEndpoint.setVisible(false);
		selectDifficulty.setVisible(true);
	}

	@FXML
	private void startAIGameAsWhite(ActionEvent event) {
		showGameView(event, GameType.AIW, (int) difficultySlider.getValue());
	}

	@FXML
	private void startAIGameAsBlack(ActionEvent event) {
		showGameView(event, GameType.AIB, (int) difficultySlider.getValue());
	}

	@FXML
	private void startPlayerGame(ActionEvent event) {
		showGameView(event, GameType.LocalPlayer, 0);
	}

	@FXML
	private void startNetworkGameAsHost(ActionEvent event) {
		showGameView(event, GameType.NetworkHost, 0);
	}

	@FXML
	private void startNetworkGameAsClient(ActionEvent event) {
		showGameView(event, GameType.NetworkClient, 0);
	}
}
