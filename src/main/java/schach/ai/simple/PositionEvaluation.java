package schach.ai.simple;

import schach.board.*;

/**
 * Evaluate the position of a given board
 */
class PositionEvaluation {
	private IBoard board;

	private PositionEvaluation(IBoard board) {
		this.board = board;
	}

	static int evaluate(IBoard board) {
		PositionEvaluation eval = new PositionEvaluation(board);
		return eval.eval();
	}

	private int eval() {
		int points = 0;

		// Evaluate status
		points += status();

		// Evaluate pawn structure
		// TODO Doubled pawns
		points += passedPawns(board.getSideToMove()) + connectedPawns(board.getSideToMove());
		points -= passedPawns(board.getSideToMove().getInverted()) + connectedPawns(board.getSideToMove().getInverted());

		// Evaluate king safety
		points += kingSafety(board.getSideToMove());
		points -= kingSafety(board.getSideToMove().getInverted());

		// Calculate total pieces
		int totalPieces = 0;
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (board.pieceAt(new Position(file, rank)) != null) {
					totalPieces++;
				}
			}
		}

		// Evaluate piece development if there are > 25 pieces left
		if (totalPieces >= 25) {
			points += pieceDevelopment(board.getSideToMove());
			points -= pieceDevelopment(board.getSideToMove().getInverted());
		}
		
		// Evaluate pushed pawns if there are < 20 pieces left
		if (totalPieces < 20) {
			points += pushedPawns(board.getSideToMove());
			points -= pushedPawns(board.getSideToMove().getInverted());
		}

		return points;
	}

	private int status() {
		int points = 0;

		// Check?
		if (board.getStatus() == BoardStatus.Check) {
			points -= 1;
		}

		// Influence Quantity of moves
		points += board.getValidMoves().size();

		return points;
	}

	private int pieceDevelopment(Color colorToEvaluate) {
		int points = 0;

		int rankFrom = colorToEvaluate == Color.White ? 2 : 4;
		int rankTo = colorToEvaluate == Color.White ? 3 : 5;

		for (int rank = rankFrom; rank <= rankTo; rank++) {
			for (int file = 0; file < 8; file++) {
				if (board.pieceAt(new Position(file, rank), colorToEvaluate) == PieceType.Knight
						|| board.pieceAt(new Position(file, rank), colorToEvaluate) == PieceType.Bishop
						|| board.pieceAt(new Position(file, rank), colorToEvaluate) == PieceType.Rook) {
					points += 50;
				}
			}
		}

		return points;
	}
	
	private int pushedPawns(Color colorToEvaluate) {
		int points = 0;

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (board.pieceAt(new Position(file, rank), colorToEvaluate) == PieceType.Pawn) {
					if (colorToEvaluate == Color.White) {
						points += 5 * rank;
					} else {
						points += 5 * (7 - rank);
					}
				}
			}
		}

		return points;
	}

	private int passedPawns(Color colorToEvaluate) {
		int points = 0;

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (isPassedPawnAt(colorToEvaluate, new Position(file, rank))) {
					points += 20;
				}
			}
		}

		return points;
	}

	private boolean isPassedPawnAt(Color colorToEvaluate, Position position) {
		int pawnRankDir = colorToEvaluate == Color.White ? 1 : -1;

		if (board.pieceAt(position, colorToEvaluate) != PieceType.Pawn) {
			return false;
		}

		for (int r = position.getRank() + pawnRankDir; r < 8 && r >= 0; r += pawnRankDir) {
			for (int f = position.getFile() - 1; f <= position.getFile() + 1; f++) {
				if (Position.isValid(f, r)
						&& board.pieceAt(new Position(f, r), colorToEvaluate.getInverted()) == PieceType.Pawn) {
					return false;
				}
			}
		}

		return true;
	}

	private int connectedPawns(Color colorToEvaluate) {
		int points = 0;
		int pawnRankDir = colorToEvaluate == Color.White ? 1 : -1;

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (board.pieceAt(new Position(file, rank), colorToEvaluate) == PieceType.Pawn) {
					if (Position.isValid(file - 1, rank + pawnRankDir) && board
							.pieceAt(new Position(file - 1, rank + pawnRankDir), colorToEvaluate) == PieceType.Pawn) {
						points += 15;

					}
					if (Position.isValid(file + 1, rank + pawnRankDir) && board
							.pieceAt(new Position(file + 1, rank + pawnRankDir), colorToEvaluate) == PieceType.Pawn) {
						points += 15;
					}
				}
			}
		}

		return points;
	}

	private int kingSafety(Color colorToEvaluate) {
		int points = 0;

		int pawnRankDir = colorToEvaluate == Color.White ? 1 : -1;

		// Find my king
		Position myKingPosition = kingPosition(colorToEvaluate);

		// Castling rights
		CastleRights castleRights = board.getCastleRights(colorToEvaluate);
		if (castleRights == CastleRights.Both) {
			points += 2;
		} else if (castleRights != CastleRights.NoRights) {
			points += 1;
		}

		// King protected?
		for (int fileDelta = -1; fileDelta <= 1; fileDelta++) {
			int file = myKingPosition.getFile() + fileDelta;
			int rank = myKingPosition.getRank() + pawnRankDir;

			if (Position.isValid(file, rank) && board.pieceAt(new Position(file, rank), colorToEvaluate) != null) {
				points += 40;
			}
		}

		return points;
	}

	private Position kingPosition(Color color) {
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (board.pieceAt(new Position(file, rank), color) == PieceType.King) {
					return new Position(file, rank);
				}
			}
		}

		return null;
	}
}
