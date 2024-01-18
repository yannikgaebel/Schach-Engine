package schach.board.simple;

import schach.board.*;

/**
 * This is the return value if you apply a move to BoardData. It contains the
 * new board data and the beaten piece if any.
 */
class BoardDataResult {
	final BoardData boardData;
	final PieceType beaten;

	BoardDataResult(BoardData boardData, PieceType beaten) {
		this.boardData = boardData;
		this.beaten = beaten;
	}

	BoardDataResult(BoardData boardData) {
		this(boardData, null);
	}
}
