package schach.gui.game;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import schach.GameType;
import schach.board.Color;
import schach.board.IBoard;
import schach.board.PieceType;
import schach.board.Position;
import schach.gui.CanvasHelper;
import schach.gui.ImageManager;

/**
 * The view for the game interface
 */
public class GameView {
	Canvas canvas;
	double boardSize;

	GameController controller;

	/**
	 * Constructor of the game view
	 * 
	 * @param stage    The stage on which will display the view
	 * @param gameType The game type of the game
	 * @throws IOException
	 */
	public GameView(Stage stage, GameType gameType, int difficulty) throws IOException {
		this(stage, gameType, null, difficulty);
	}

	/**
	 * Constructor of the game view
	 * 
	 * @param stage    The stage on which will display the view
	 * @param gameType The game type of the game
	 * @param endpoint The host address to which to connect to when playing as
	 *                 network client
	 * @throws IOException
	 */
	public GameView(Stage stage, GameType gameType, String endpoint, int difficulty) throws IOException {
		controller = new GameController(this, gameType, endpoint, difficulty);

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/gameGUI.fxml"));
		fxmlLoader.setController(controller);
		Parent root = (Parent) fxmlLoader.load();
		Scene scene = new Scene(root);

		canvas = (Canvas) root.lookup("#boardCanvas");

		boolean scaleWidth = canvas.getWidth() > canvas.getHeight();

		if (scaleWidth) {
			boardSize = canvas.getHeight();
		} else {
			boardSize = canvas.getWidth();
		}
		canvas.setWidth(boardSize);
		canvas.setHeight(boardSize);

		stage.setScene(scene);
		stage.show();

		controller.updateView();
	}

	void drawBoard(Color viewSide) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		CanvasHelper ch = new CanvasHelper(viewSide, boardSize);

		ImageManager imgManager = ImageManager.getInstance();
		Image bgWhite = imgManager.getImage("backgroundWhite");
		Image bgBlack = imgManager.getImage("backgroundBlack");

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				boolean useBlackBg = (rank + file) % 2 == 0;
				Image background = useBlackBg ? bgBlack : bgWhite;
				gc.drawImage(background, ch.fileToPos(file), ch.rankToPos(rank), ch.getCellSize(), ch.getCellSize());
			}
		}
	}

	void highlightPosition(Color viewSide, int file, int rank, Paint color) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		CanvasHelper ch = new CanvasHelper(viewSide, boardSize);

		double inset = ch.getCellSize() / 8;
		double size = ch.getCellSize() - (2 * inset);

		gc.setFill(color);
		gc.fillRect(ch.fileToPos(file) + inset, ch.rankToPos(rank) + inset, size, size);
	}

	void drawPieces(Color viewSide, IBoard board) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		ImageManager imgManager = ImageManager.getInstance();
		CanvasHelper ch = new CanvasHelper(viewSide, boardSize);

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				PieceType whitePiece = board.pieceAt(new Position(file, rank), Color.White);
				PieceType blackPiece = board.pieceAt(new Position(file, rank), Color.Black);

				if (whitePiece == null && blackPiece == null) { continue; }

				String iconColor = whitePiece != null ? "white" : "black";

				PieceType pieceType = whitePiece != null ? whitePiece : blackPiece;
				Image pieceIcon = imgManager.getImage(iconColor + pieceType.name());
				gc.drawImage(pieceIcon, ch.fileToPos(file), ch.rankToPos(rank), ch.getCellSize(), ch.getCellSize());
			}
		}
	}

	void drawIndexes(Color viewSide) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		CanvasHelper ch = new CanvasHelper(viewSide, boardSize);

		gc.setFill(javafx.scene.paint.Color.WHITE);
		int file = viewSide == Color.White ? 7 : 0;
		int rank = viewSide == Color.White ? 0 : 7;

		for (int i = 0; i < 8; i++) {
			gc.fillText(String.valueOf(i + 1), ch.fileToPos(file) + ch.getCellSize() - 8, ch.rankToPos(i) + 12);
			String alphaIndex = String.valueOf((char) (i + 97));
			gc.fillText(alphaIndex, ch.fileToPos(i) + 1, ch.rankToPos(rank) + ch.getCellSize() - 2);
		}
	}
}
