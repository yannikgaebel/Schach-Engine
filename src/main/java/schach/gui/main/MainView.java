package schach.gui.main;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The view of the main menu
 */
public class MainView {

	/**
	 * Constructor of the main menu view
	 * 
	 * @throws IOException
	 */
	public MainView(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/mainMenuGUI.fxml"));
		Parent root = (Parent) fxmlLoader.load();

		Scene scene = new Scene(root);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
}
