package schach.gui.game;

import schach.Game;
import schach.GameType;
import schach.board.Position;
import schach.network.NetworkManager;

/**
 * The model for the game interface
 */
class GameModel {
	private Game game;
	private GameType gameType;

	private NetworkManager networkManager;

	private boolean playLock;
	private Position from;

	private boolean turnBoardAfterMove;
	private boolean touchMoveRule;
	private boolean checkNotification;
	private boolean showPossibleMoves = true;

	GameModel(GameType gameType) {
		game = new Game();
		this.gameType = gameType;
	}

	Game getGame() {
		return game;
	}

	GameType getGameType() {
		return gameType;
	}

	Position getFrom() {
		return from;
	}

	void setFrom(Position from) {
		this.from = from;
	}

	boolean isTurnBoardAfterMove() {
		return turnBoardAfterMove;
	}

	void setTurnBoardAfterMove(boolean turnBoardAfterMove) {
		this.turnBoardAfterMove = turnBoardAfterMove;
	}

	boolean isTouchMoveRule() {
		return touchMoveRule;
	}

	void setTouchMoveRule(boolean touchMoveRule) {
		this.touchMoveRule = touchMoveRule;
	}

	boolean isCheckNotification() {
		return checkNotification;
	}

	void setCheckNotification(boolean checkNotification) {
		this.checkNotification = checkNotification;
	}

	boolean isShowPossibleMoves() {
		return showPossibleMoves;
	}

	void setShowPossibleMoves(boolean showPossibleMoves) {
		this.showPossibleMoves = showPossibleMoves;
	}

	boolean isPlayLock() {
		return playLock;
	}

	void setPlayLock(boolean playLock) {
		this.playLock = playLock;
	}

	public NetworkManager getNetworkManager() {
		return networkManager;
	}

	public void setNetworkManager(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}
}
