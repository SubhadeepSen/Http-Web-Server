package sdp.http.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import sdp.http.client.HttpClientThread;
import sdp.http.config.ServerConfig;

/**
 * The HttpServer class creates a server instance and listens to the incoming
 * requests. Depending on the incoming request, it creates a HTTP client thread.
 * The start() method starts the server.
 * 
 * @author Subhadeep Sen
 *
 */
public class HttpServer {
	private ServerSocket serverSocket;
	private ServerConfig serverConfig;
	private HttpClientThread httpClientThread;
	private boolean isStopped;

	public HttpServer() {
		serverConfig = ServerConfig.getInstance();
		isStopped = false;
		httpClientThread = null;
	}

	public void start() {
		try {
			InetAddress address = InetAddress.getByName(serverConfig.getHostAddress());
			/* creates a server socket with the port number, queue size and host address */
			serverSocket = new ServerSocket(serverConfig.getPortNumber(), 50, address);
			/*
			 * This loop keeps the server socket up and running to listen to the incoming
			 * http request
			 */
			while (!isStopped) {
				Socket clientSocket = serverSocket.accept();
				/* creates a client thread with the accepted request and starts it */
				httpClientThread = new HttpClientThread(clientSocket);
				httpClientThread.start();
			}
		} catch (IOException e) {
			System.out.println("Unable to accept request: " + e.getMessage());
		} finally {
			/* once it comes out of the loop, it closes the server socket */
			closeServerSocket();
		}
	}

	public void stop() {
		isStopped = true;
	}

	private void closeServerSocket() {
		if (null != serverSocket) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.out.println("Unable to close server socket: " + e.getMessage());
			}
		}
	}

}
