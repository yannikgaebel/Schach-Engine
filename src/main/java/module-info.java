module schach {
	requires javafx.fxml;
	requires javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.base;

	exports schach;
	exports schach.board;

	exports schach.gui.main to javafx.fxml;
	exports schach.gui.game to javafx.fxml;
	exports schach.gui.promotion to javafx.fml;

	opens schach.gui.main to javafx.fxml;
	opens schach.gui.game to javafx.fxml;
	opens schach.gui.promotion to javafx.fxml;

	opens schach;
}
