package schach.network;

/**
 * The listener interface for network events
 */
public interface NetworkListener {
	/**
	 * The event which gets called when a message is received
	 * 
	 * @param message the received message
	 */
	void onMessageReceived(String message);

	/**
	 * The event which gets called when a connection is established
	 */
	void onConnected();

	/**
	 * The event which gets called when the connection is closed or lost
	 */
	void onConnectionClosed();
}
