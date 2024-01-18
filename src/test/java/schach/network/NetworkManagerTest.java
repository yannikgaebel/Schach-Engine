package schach.network;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

/**
 * Test class for {@link #NetworkManager}
 */
public class NetworkManagerTest {
	/**
	 * Test host/client connection.
	 */
	@Test
	public void testHostClient() {
		// Setup
		NetworkManager host = null;
		SimpleListener hostListener = new SimpleListener();

		NetworkManager client = null;
		SimpleListener clientListener = new SimpleListener();

		try {
			host = new NetworkManager();
			host.addListener(hostListener);
			host.start();

			client = new NetworkManager("127.0.0.1");
			client.addListener(clientListener);
			client.start();
		} catch (IOException e) {
			fail(e);
		}

		// Sleep
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			fail(e);
		}

		// Send messages
		host.sendMessage("Hello from host");
		client.sendMessage("Hello from client");

		// Sleep
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			fail(e);
		}

		// Assert
		assertTrue(hostListener.connected, "Host should be connected");
		assertEquals(hostListener.received.get(0), "Hello from client");

		assertTrue(clientListener.connected, "Client should be connected");
		assertEquals(clientListener.received.get(0), "Hello from host");

		// Close
		host.close();
		client.close();
	}

	/**
	 * SimpleListener which stores received messages and connected state
	 */
	private class SimpleListener implements NetworkListener {
		List<String> received = new ArrayList<String>();
		boolean connected = false;

		@Override
		public void onMessageReceived(String message) {
			received.add(message);
		}

		@Override
		public void onConnected() {
			connected = true;
		}

		@Override
		public void onConnectionClosed() {
			connected = false;
		}
	}
}
