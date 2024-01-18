package schach.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * The network manager establishes a network connection and acts as a layer
 * between the application and networking
 */
public class NetworkManager extends Thread {
	private boolean isHost;
	private ServerSocket serverSocket;
	private Socket socket;
	private String ip;
	private static final int PORT = 38438; // CHE(S+S)

	private List<NetworkListener> networkListeners = new ArrayList<NetworkListener>();

	private BufferedReader reader;
	private PrintWriter writer;

	private boolean stop = false;

	/**
	 * Constructor of the NetworkManager which initializes it as host
	 * 
	 * @throws IOException
	 */
	public NetworkManager() throws IOException {
		isHost = true;
	}

	/**
	 * Constructor of the NetworkManager which initializes it as client
	 * 
	 * @param ip   the IP to connect to
	 * @param port the port to connect to
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public NetworkManager(String ip) throws UnknownHostException, IOException {
		isHost = false;
		this.ip = ip;
	}

	/**
	 * Adds a new listener to the network manager
	 * 
	 * @param networkListener the network listener to which events will be forwarded
	 */
	public void addListener(NetworkListener networkListener) {
		networkListeners.add(networkListener);
	}

	/**
	 * Runs the thread
	 */
	@Override
	public void run() {
		try {
			try {
				socket = getSocket();
			} catch (SocketException e) {
				e.printStackTrace();
				return;
			}

			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);

			String message;

			while (!stop) {
				message = reader.readLine();

				// Notify all listeners
				for (NetworkListener listener : networkListeners) {
					listener.onMessageReceived(message);
				}
			}

		} catch (Exception e) {
			close();
			return;
		}
	}

	/**
	 * Stops the network manager
	 */
	public void close() {
		// Notify all listeners
		for (NetworkListener listener : networkListeners) {
			listener.onConnectionClosed();
		}

		try {
			stop = true;
			if (isHost) { serverSocket.close(); }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a message over the network
	 * 
	 * @param message the message to be send
	 */
	public void sendMessage(String message) {
		writer.println(message);
	}

	private Socket getSocket() throws IOException {
		if (isHost) {
			serverSocket = new ServerSocket(PORT);
			socket = serverSocket.accept();
		} else {
			System.out.println(ip);
			socket = new Socket(ip, PORT);
		}

		System.out.println("Connected to: " + socket.getInetAddress());

		// Notify all listeners
		for (NetworkListener listener : networkListeners) {
			listener.onConnected();
		}
		return socket;
	}
}
