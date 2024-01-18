package schach;

/**
 * Represents the type of the game
 */
public enum GameType {
	AIW, // Play versus AI as white
	AIB, // Play versus AI as black
	LocalPlayer, // Play versus a local player
	NetworkHost, // Play versus a network player as host
	NetworkClient; // Play versus a network player as client

	/**
	 * Check if this is an AI game
	 * 
	 * @return whether this is an AI game
	 */
	public boolean isAIGame() {
		return this == GameType.AIW || this == GameType.AIB;
	}

	/**
	 * Check if this is a network game
	 * 
	 * @return whether this is a network game
	 */
	public boolean isNetworkGame() {
		return this == GameType.NetworkHost || this == GameType.NetworkClient;
	}
}
