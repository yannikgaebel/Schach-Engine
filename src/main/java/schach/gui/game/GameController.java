package schach.gui.game;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import schach.Game;
import schach.GameType;
import schach.board.BoardException;
import schach.board.BoardStatus;
import schach.board.Color;
import schach.board.IBoard;
import schach.board.Move;
import schach.board.PieceType;
import schach.board.Position;
import schach.gui.CanvasHelper;
import schach.gui.ImageManager;
import schach.gui.promotion.PromotionView;
import schach.network.NetworkListener;
import schach.network.NetworkManager;

/**
 * The controller for the game interface. It handles input / output for the game
 * interface.
 */
// Suppress false positive unused methods which get called from FXML
// Suppress false positive too many methods since FXML events should not count towards method count
// Suppress false positive too many fields since FXML associated nodes should not count towards field count
@SuppressWarnings({ "PMD.UnusedPrivateMethod", "PMD.TooManyMethods", "PMD.TooManyFields" })
public class GameController implements NetworkListener {
	GameView view;
	GameModel model;

	private static Paint cSelectedPos = new javafx.scene.paint.Color(0.26d, 0.87d, 0.27d, 0.5d);
	private static Paint cPossibleMove = new javafx.scene.paint.Color(0.33d, 0.80d, 0.95d, 0.5d);
	private static Paint cCheckColor = new javafx.scene.paint.Color(1.0d, 0.2d, 0.2d, 0.75d);

	private Alert reusableAlert;

	@FXML
	private TilePane beatenPlayer;

	@FXML
	private TilePane beatenOpponent;

	@FXML
	private MenuBar menuBar;

	@FXML
	private VBox history;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Label turnBlack;

	@FXML
	private Label turnWhite;

	private boolean connected = false;

	/**
	 * The constructor of the game controller.
	 * 
	 * @param gameView The view of the interface
	 * @param gameType The game type of the game
	 * @param endpoint The host address to which to connect to when playing as
	 *                 network client
	 */
	public GameController(GameView gameView, GameType gameType, String endpoint, int difficulty) {
		this.view = gameView;
		this.model = new GameModel(gameType);
		this.model.getGame().setAIDifficulty(difficulty);

		if (gameType == GameType.AIB) { model.getGame().playAIMove(); }

		NetworkManager networkManager = null;
		if (gameType == GameType.NetworkHost) {
			try {
				networkManager = new NetworkManager();
			} catch (Exception e) {
				ShowExceptionAndQuit(e);
			}
		} else if (gameType == GameType.NetworkClient) {
			try {
				networkManager = new NetworkManager(endpoint);
				// Set play lock for if client since host plays as white and client as black
				model.setPlayLock(true);
			} catch (Exception e) {
				ShowExceptionAndQuit(e);
			}
		}

		if (networkManager != null) {
			connected = false;
			reusableAlert = new Alert(AlertType.NONE);
			reusableAlert.setTitle("Chess - Network");
			reusableAlert.setHeaderText("Waiting for a connection...");
			String text = "This window will close when a succesfull connection was made.";
			if (gameType == GameType.NetworkHost) {
				try {
					text += "\n\nYour IP: " + Inet4Address.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					text += "Unknown";
				}
			}
			reusableAlert.setContentText(text);
			reusableAlert.getDialogPane().getButtonTypes().add(ButtonType.OK);
			reusableAlert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
			reusableAlert.setOnCloseRequest(event -> {
				try {
					if (!connected) {
						model.getNetworkManager().close();
						closeToMenu();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			reusableAlert.show();

			model.setNetworkManager(networkManager);
			networkManager.addListener(this);
			networkManager.start();
		}
	}

	@FXML
	private void onMouseClicked(MouseEvent event) {
		CanvasHelper ch = new CanvasHelper(getViewSide(), view.boardSize);
		int file = ch.posToFile(event.getX());
		int rank = ch.posToRank(event.getY());
		clickCell(new Position(file, rank));
	}

	@FXML
	private void undoMove() {
		if (model.isPlayLock()) { return; }
		if (model.getGameType().isNetworkGame()) { return; }

		Game game = model.getGame();

		// Undo twice when playing against AI
		if (model.getGameType().isAIGame()) {
			if (game.getHistoryIndex() < 2) { return; }
			game.undo();
		}

		game.undo();

		model.setFrom(null);

		updateView();
		updateNotification();
	}

	@FXML
	private void redoMove() {
		if (model.isPlayLock()) { return; }
		if (model.getGameType().isNetworkGame()) { return; }

		Game game = model.getGame();

		// Redo twice when playing against AI
		if (model.getGameType().isAIGame()) {
			int avaibleMoves = game.getHistory().size() - game.getHistoryIndex() + 1;
			if (avaibleMoves < 2) { return; }
			game.redo();
		}

		game.redo();

		model.setFrom(null);

		updateView();
		updateNotification();
	}

	@FXML
	private void turnBoardAfterMove(ActionEvent event) {
		model.setTurnBoardAfterMove(((CheckMenuItem) event.getSource()).isSelected());
		drawBoard();
	}

	@FXML
	private void touchMoveRule(ActionEvent event) {
		model.setTouchMoveRule(((CheckMenuItem) event.getSource()).isSelected());
	}

	@FXML
	private void checkNotification(ActionEvent event) {
		model.setCheckNotification(((CheckMenuItem) event.getSource()).isSelected());
	}

	@FXML
	private void showPossibleMoves(ActionEvent event) {
		model.setShowPossibleMoves(((CheckMenuItem) event.getSource()).isSelected());
		drawBoard();
	}

	@FXML
	private void closeToMenu() throws IOException {
		if (model.getNetworkManager() != null && connected) {
			connected = false;
			model.getNetworkManager().sendMessage("CLOSE");
			model.getNetworkManager().close();
		}

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/mainMenuGUI.fxml"));
		Parent root = (Parent) fxmlLoader.load();

		Scene mainScene = new Scene(root);
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.setScene(mainScene);
		stage.show();
	}

	private void clickCell(Position position) {
		Position from = model.getFrom();

		if (position.equals(from)) return;

		if (!selectFrom(position) && from != null) { selectTo(position); }

		updateView();
	}

	private boolean selectFrom(Position position) {
		if (model.isPlayLock()) { return false; }

		Position from = model.getFrom();
		boolean touchMoveRule = model.isTouchMoveRule();
		if (from != null && touchMoveRule) { return false; }

		List<Move> moves = model.getGame().getBoard().getValidMoves(position);
		if (moves.isEmpty()) { return false; }

		model.setFrom(position);
		return true;
	}

	private boolean selectTo(Position position) {
		Position from = model.getFrom();
		Move move = new Move(from, position);
		Game game = model.getGame();

		if (!game.getBoard().getValidMoves().contains(move)) { return false; }

		try {
			Move correctedMove = new Move(move.getFrom(), move.getTo(), getPromotionType(move));
			game.play(correctedMove);
			updateView();
			updateNotification();

			boolean gameOver = model.getGame().getBoard().getStatus().isOver();

			GameType gameType = model.getGameType();

			// Send move to opponent if network game
			if (gameType.isNetworkGame()) {
				model.getNetworkManager().sendMessage(correctedMove.toString());
				model.setPlayLock(true);
			}

			// Play AI move if AI game
			if (!gameOver && gameType.isAIGame()) {
				model.setPlayLock(true);
				new Thread(() -> {
					game.playAIMove();
					Platform.runLater(() -> {
						updateView();
						updateNotification();
						model.setPlayLock(false);
					});
				}).start();
			}
		} catch (BoardException e) {
			e.printStackTrace();
		}

		model.setFrom(null);
		return true;
	}

	void updateView() {
		drawBoard();
		updateBeaten(Color.White);
		updateBeaten(Color.Black);
		updateHistory();
		updateSideToMove();
	}

	private void updateNotification() {
		if (model.isCheckNotification()) { notifyOnCheck(); }
		notifyOnCheckMate();
		notifyOnDraw();
	}

	private Color getViewSide() {
		Color view = Color.White;
		if (model.isTurnBoardAfterMove()) {
			view = model.getGame().getBoard().getSideToMove();
		} else {
			boolean aiBlack = model.getGameType() == GameType.AIB;
			boolean netBlack = model.getGameType() == GameType.NetworkClient;
			if (aiBlack || netBlack) { view = Color.Black; }
		}
		return view;
	}

	private void updateBeaten(Color color) {
		List<PieceType> beatenPieces = model.getGame().getBeaten(color);

		TilePane beatenPiecesBox = color == Color.White ? beatenPlayer : beatenOpponent;
		beatenPiecesBox.getChildren().clear();
		for (PieceType beatenPiece : beatenPieces) {
			String pieceColor = color == Color.White ? "white" : "black";
			Image icon = ImageManager.getInstance().getImage(pieceColor + beatenPiece.name());
			ImageView imgView = new ImageView(icon);

			CanvasHelper ch = new CanvasHelper(color, view.boardSize);
			imgView.setFitWidth(ch.getCellSize());
			imgView.setFitHeight(ch.getCellSize());
			beatenPiecesBox.getChildren().add(imgView);
		}
	}

	private void drawBoard() {
		Position from = model.getFrom();
		Color viewSide = getViewSide();
		view.drawBoard(viewSide);
		if (from != null) { view.highlightPosition(viewSide, from.getFile(), from.getRank(), cSelectedPos); }
		if (model.isShowPossibleMoves()) { highlightPossibleMoves(); }

		IBoard board = model.getGame().getBoard();
		if (board.getStatus() == BoardStatus.Check || board.getStatus() == BoardStatus.Checkmate) {
			Position kp = board.getKingPosition(board.getSideToMove());
			view.highlightPosition(getViewSide(), kp.getFile(), kp.getRank(), cCheckColor);
		}

		view.drawPieces(viewSide, model.getGame().getBoard());
		view.drawIndexes(viewSide);
	}

	private void updateHistory() {
		Game game = model.getGame();
		List<Move> historyList = game.getHistory();

		history.getChildren().clear();

		scrollPane.vvalueProperty().bind(history.heightProperty());

		for (int i = 0; i < historyList.size(); i++) {
			Move move = historyList.get(i);
			Button button = new Button(move.toString());
			button.setMaxWidth(Double.MAX_VALUE);
			button.setOnAction(onHistoryClickHandler(i));

			if (model.getGameType() == GameType.AIW && i % 2 == 0) { button.setDisable(true); }
			if (model.getGameType() == GameType.AIB && i % 2 == 1) { button.setDisable(true); }

			if (i <= game.getHistoryIndex()) {
				button.setStyle("-fx-font-weight: bold");
			} else if (i > game.getHistoryIndex() && !button.isDisabled()) { button.setStyle("-fx-text-fill: gray;"); }

			history.getChildren().add(button);
		}
	}

	private void updateSideToMove() {
		Color currentSide = model.getGame().getBoard().getSideToMove();

		if (currentSide == Color.White) {
			turnWhite.setVisible(true);
			turnBlack.setVisible(false);
		}

		if (currentSide == Color.Black) {
			turnWhite.setVisible(false);
			turnBlack.setVisible(true);
		}
	}

	private EventHandler<ActionEvent> onHistoryClickHandler(int moveIndex) {
		return event -> onHistoryClick(moveIndex);
	}

	private void onHistoryClick(int moveIndex) {
		if (model.getGameType().isNetworkGame()) { return; }

		Game game = model.getGame();

		int steps = Math.abs(game.getHistoryIndex() - moveIndex);

		boolean aiMoveBlack = model.getGameType() == GameType.AIW && moveIndex % 2 == 0;
		boolean aiMoveWhite = model.getGameType() == GameType.AIB && moveIndex % 2 == 1;

		if (aiMoveBlack || aiMoveWhite) { return; }

		for (int i = 0; i < steps; i++) {
			if (game.getHistoryIndex() > moveIndex) {
				game.undo();
			} else {
				game.redo();
			}
		}
		updateView();
		updateNotification();
	}

	private void highlightPossibleMoves() {
		Position from = model.getFrom();
		if (from == null) return;
		List<Move> moves = model.getGame().getBoard().getValidMoves(from);

		Color viewSide = getViewSide();
		for (Move move : moves) {
			Position to = move.getTo();
			view.highlightPosition(viewSide, to.getFile(), to.getRank(), cPossibleMove);
		}
	}

	private void notifyOnCheck() {
		BoardStatus boardStatus = model.getGame().getBoard().getStatus();
		if (boardStatus == BoardStatus.Check) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Chess");
			alert.setHeaderText("Check!");
			alert.setContentText("The King is in check!");
			alert.showAndWait();
		}
	}

	private void notifyOnCheckMate() {
		BoardStatus boardStatus = model.getGame().getBoard().getStatus();
		if (boardStatus == BoardStatus.Checkmate) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Chess");
			alert.setHeaderText("Checkmate!");
			alert.setContentText("The King is in checkmate!\nThe game is over.");
			alert.showAndWait();
		}
	}

	private void notifyOnDraw() {
		BoardStatus boardStatus = model.getGame().getBoard().getStatus();
		boolean drawFifty = boardStatus == BoardStatus.DrawByFiftyMoveRule;
		boolean drawMaterial = boardStatus == BoardStatus.DrawByInsufficientMaterial;
		boolean drawStalemate = boardStatus == BoardStatus.DrawByStalemate;

		if (drawFifty || drawMaterial || drawStalemate) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Chess");
			alert.setHeaderText("Draw!");

			String content = null;
			content = drawFifty ? "Draw by fifty move rule." : content;
			content = drawMaterial ? "Draw by insufficient material." : content;
			content = drawStalemate ? "Draw by stalemate." : content;

			alert.setContentText(content + "\nThe game is over.");
			alert.showAndWait();
		}
	}

	private PieceType getPromotionType(Move move) {
		Move promotionMove = new Move(move.getFrom(), move.getTo(), PieceType.Rook);
		List<Move> possibleMoves = model.getGame().getBoard().getValidMoves(move.getFrom());
		if (!possibleMoves.contains(promotionMove)) return null;

		try {
			PromotionView promotionView = new PromotionView(model.getGame().getBoard().getSideToMove());
			return promotionView.getPromotion();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return PieceType.Queen;
	}

	private void ShowExceptionAndQuit(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(e.getLocalizedMessage());
		alert.showAndWait();
		Platform.exit();
		System.exit(0);
	}

	@Override
	public void onConnected() {
		connected = true;
		Platform.runLater(() -> {
			reusableAlert.close();
		});
	}

	@Override
	public void onConnectionClosed() {
		if (!connected) { return; }
		connected = false;
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Chess - Network");
			alert.setHeaderText("Connection has been lost");
			alert.setContentText("The game will exit to the main menu");
			alert.setOnCloseRequest(event -> {
				try {
					closeToMenu();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			alert.show();
		});
	}

	@Override
	public void onMessageReceived(String message) {
		if (message == null) { return; }

		// Check if the message is something different than a move
		if (message.equals("CLOSE")) {
			model.getNetworkManager().close();
			return;
		}

		// Its a move then
		String[] parts = message.split("-");

		int fromFile = parts[0].charAt(0) - 'a';
		int fromRank = parts[0].charAt(1) - '1';
		Position fromPosition = new Position(fromFile, fromRank);

		int toFile = parts[1].charAt(0) - 'a';
		int toRank = parts[1].charAt(1) - '1';
		Position toPosition = new Position(toFile, toRank);

		Move move = null;

		if (parts[1].length() == 3) {
			char character = parts[1].charAt(2);
			final List<Character> piecesText = List.of('K', 'Q', 'R', 'B', 'N', 'P');
			PieceType promotion = PieceType.values()[piecesText.indexOf(character)];
			move = new Move(fromPosition, toPosition, promotion);
		} else {
			move = new Move(fromPosition, toPosition);
		}

		try {
			model.getGame().play(move);
		} catch (BoardException e) {
			// This should never be reached since the send move should be verified as valid
			// by the opponent
			// In the case the opponent somehow send a invalid move, we panic as we do not
			// want a invalid board
			ShowExceptionAndQuit(e);
		}

		Platform.runLater(() -> {
			updateView();
			updateNotification();
		});
		model.setPlayLock(false);
	}
}
