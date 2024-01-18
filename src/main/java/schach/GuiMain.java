package schach;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import schach.gui.main.MainView;

/**
 * Application class
 */
public class GuiMain extends Application {
	/**
	 * The static initializer for the GUI application
	 * 
	 * @param args Launch arguments
	 */
	public static void init(String[] args) {
		launch(args);
	}

	/**
	 * Main entry point of the 2d implementation
	 * 
	 * @throws IOException
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		new MainView(primaryStage);
	}
}
