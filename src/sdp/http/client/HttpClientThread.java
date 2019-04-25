package sdp.http.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import sdp.http.locator.ResourceLocator;
import sdp.http.request.HttpRequest;
import sdp.http.request.RequestExtractor;
import sdp.http.request.RequestProcessor;
import sdp.http.response.HttpResponse;
import sdp.http.response.ResponseProcessor;
import sdp.http.response.ResponseSender;

/**
 * The HttpClientThread is a thread which gets created upon every accepted
 * request. The class is responsible for processing the request and response.
 * 
 * @author Subhadeep Sen
 *
 */
public class HttpClientThread extends Thread {

	private Socket socket;
	private RequestExtractor requestExtractor;
	private RequestProcessor requestProcessor;
	private ResponseProcessor responseProcessor;
	private ResponseSender responseSender;
	private InputStream inputStream;
	private OutputStream outputStream;

	public HttpClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		HttpResponse httpResponse = new HttpResponse();
		try {
			initialize();
			HttpRequest httpRequest = requestExtractor.extractRequest();
			String viewName = requestProcessor.processRequest(httpRequest, httpResponse);
			responseHandler(viewName, httpResponse);
		} catch (IOException e) {
			System.out.println("Unable to process your request: " + e.getMessage());
			/*
			 * In case of exception, building the internal server error view and sending it
			 * as response
			 */
			responseHandler("error", httpResponse);
		} finally {
			closeConnection();
		}
	}

	/**
	 * Creates and initializes the requires objects.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		inputStream = socket.getInputStream();
		outputStream = socket.getOutputStream();
		requestExtractor = new RequestExtractor(inputStream);
		requestProcessor = new RequestProcessor();
		responseProcessor = new ResponseProcessor();
		responseSender = new ResponseSender(outputStream);
	}

	/**
	 * Handles response processing.
	 * 
	 * @param viewName
	 * @param httpResponse
	 */
	private void responseHandler(String viewName, HttpResponse httpResponse) {
		File view = ResourceLocator.getView(viewName, httpResponse);
		responseProcessor.processView(view, httpResponse);
		responseProcessor.buildHeader(httpResponse);
		responseSender.sendResponse(httpResponse);
	}

	/**
	 * Closes all the open connections after serving the client's request.
	 */
	private void closeConnection() {
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Problem in closing connection: " + e.getMessage());
		}
	}

}