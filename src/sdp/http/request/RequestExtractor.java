package sdp.http.request;

import static sdp.http.constant.HttpConstant.CRLF;
import static sdp.http.constant.HttpConstant.CONTENT_LENGTH;
import static sdp.http.constant.HttpConstant.EMPTY;
import static sdp.http.constant.HttpConstant.PATH_SEPARATOR;
import static sdp.http.constant.HttpMethod.POST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import sdp.http.config.ServerConfig;

/**
 * The RequestExtractor class extracts information from the incoming request
 * stream and builds the request object.
 * 
 * @author Subhadeep Sen
 *
 */
public class RequestExtractor {

	private BufferedReader in;

	private HttpRequest httpRequest;

	public RequestExtractor(InputStream inputStream) {
		in = new BufferedReader(new InputStreamReader(inputStream));
		httpRequest = new HttpRequest();
	}

	/**
	 * Extracts information from the request stream and returns request object.
	 * 
	 * @return HttpRequest
	 * @throws IOException
	 */
	public HttpRequest extractRequest() throws IOException {
		int contentLength = extractHttpHeader();
		String httpHeader = httpRequest.getHttpHeader();
		/*
		 * The first line of the header contains information about the request method,
		 * base path and requested resource. Here the requested resource is the name of
		 * the handler method.
		 */
		String firstLine = httpHeader.substring(0, httpHeader.indexOf(CRLF));
		if (isValidBasePath(firstLine)) {
			extractHttpBody(contentLength);
			extractHttpMethod(firstLine);
			extractRequestedResource(firstLine);
			buildHeaderMap();
			/* For POST method sends the body for building the parameter Map */
			if (POST.equals(httpRequest.getHttpMethod()) && !httpRequest.getHttpBody().isEmpty()) {
				buildParameterMap(httpRequest.getHttpBody());
			}
		} else {
			httpRequest.setRequestedResource(EMPTY);
		}
		return httpRequest;
	}

	/**
	 * Extracts header information and adds it to request object.
	 * 
	 * @return contentLength
	 * @throws IOException
	 */
	private int extractHttpHeader() throws IOException {
		String request = EMPTY;
		StringBuilder sb = new StringBuilder();
		int contentLength = 0;
		int currentPositionInStream = 0;

		while ((request = in.readLine()) != null) {
			// checking for body content and storing the length (valid for POST request).
			if (request.contains(CONTENT_LENGTH)) {
				contentLength = Integer.parseInt(request.split(":")[1].trim());
			}
			/* end of http request header */
			if (request.equals(CRLF) || request.equals(EMPTY)) {
				/*
				 * marking the current position in the stream (ends of header). For POST,
				 * contentLength number can be read from the stream to extract the body content
				 */
				in.mark(currentPositionInStream);
				break;
			}
			currentPositionInStream++;
			/* building the request header */
			sb.append(request + CRLF);
		}
		/* setting the request header to request object */
		httpRequest.setHttpHeader(sb.toString());
		return contentLength;
	}

	/**
	 * Extracts information about the base path and matches it with the value set in
	 * ServerConfig. If matches then returns true otherwise false.
	 * 
	 * @param firstLine
	 * @return boolean
	 */
	private boolean isValidBasePath(String firstLine) {
		String basePath = ServerConfig.getInstance().getApplicationBasePath();
		boolean isValid = false;
		if (PATH_SEPARATOR.equals(basePath)) {
			isValid = true;
		} else if (firstLine.contains(basePath)) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Extracts body content in case of POST method and adds it to request object.
	 * 
	 * @param contentLength
	 * @throws IOException
	 */
	public void extractHttpBody(int contentLength) throws IOException {
		String httpBody = EMPTY;
		if (contentLength != 0) {
			char[] buf = new char[contentLength];
			int expectedDataLenght = in.read(buf, 0, contentLength);
			if (expectedDataLenght == contentLength) {
				httpBody = new String(buf);
			}
		}
		httpRequest.setHttpBody(httpBody);
	}

	/**
	 * Extracts HTTP Method and adds to request object.
	 * 
	 * @param firstLine
	 */
	private void extractHttpMethod(String firstLine) {
		// GET /index.html HTTP/1.1
		// POST /index.html HTTP/1.1
		httpRequest.setHttpMethod(firstLine.substring(0, firstLine.indexOf(PATH_SEPARATOR)).trim());
	}

	/**
	 * Extracts information about the requested resource (requested handler method)
	 * and adds to request object.
	 * 
	 * @param firstLine
	 */
	private void extractRequestedResource(String firstLine) {
		String requestedResource = EMPTY;
		// GET /hello/index.html HTTP/1.1 when basepath is present
		// GET /index.html HTTP/1.1
		// POST /index.html HTTP/1.1
		// GET /sayHello?name=x&city=y&company=z HTTP/1.1 Form parameters with GET
		String resource = EMPTY;
		String[] tokens = null;
		resource = firstLine.substring(firstLine.indexOf(PATH_SEPARATOR) + 1, firstLine.indexOf("HTTP")).trim();
		if (resource.contains(PATH_SEPARATOR)) {
			tokens = resource.split(PATH_SEPARATOR);
			resource = tokens[tokens.length - 1]; // removes the base path [contains index.html or
													// sayHello?name=x&city=y&company=z]
		}
		if (resource.contains("?") && resource.contains("=")) {
			tokens = resource.split("\\?");
			requestedResource = tokens[0]; // sayHello
			/* contains parameterized string extracting from the URL */
			resource = tokens[1]; // name=x&city=y&company=z
		} else if (resource.contains("?")) {
			tokens = resource.split("\\?");
			requestedResource = tokens[0];
			resource = EMPTY;
		} else {
			requestedResource = resource;
			resource = EMPTY;
		}

		/*
		 * building the parameter Map for GET method and the resource is not empty when
		 * it has parameterized string
		 */
		if (!resource.isEmpty()) {
			buildParameterMap(resource);
		}

		httpRequest.setRequestedResource(requestedResource);
	}

	/**
	 * Builds the parameter Map by extracting information either from the URL or the
	 * body and adds it to request object. The parameterizedString must be in
	 * name=x&city=y&company=z format.
	 * 
	 * @param parameterizedString
	 */
	private void buildParameterMap(String parameterizedString) {
		Map<String, String> parameterMap = new HashMap<>();
		if (parameterizedString.contains("&") && parameterizedString.contains("=")) {
			String[] tokens = parameterizedString.split("\\&");
			String[] temp;
			for (int i = 0; i < tokens.length; i++) {
				temp = tokens[i].split("=");
				if (temp.length == 2) {
					parameterMap.put(temp[0].trim(), temp[1].trim());
				} else if (temp.length == 1) {
					parameterMap.put(temp[0].trim(), "");
				}
			}
		}
		httpRequest.setParameterMap(parameterMap);
	}

	/**
	 * Builds the header Map by extracting information from the built header and
	 * adds it to request object.
	 */
	private void buildHeaderMap() {
		Map<String, String> headerMap = new HashMap<>();
		String[] headerParams = httpRequest.getHttpHeader().split(CRLF);
		String[] temp;
		for (int i = 1; i < headerParams.length; i++) {
			temp = headerParams[i].split(":");
			if (temp.length == 2) {
				headerMap.put(temp[0].trim(), temp[1].trim());
			} else if (temp.length == 1) {
				headerMap.put(temp[0].trim(), "");
			}
		}
		httpRequest.setHeaderMap(headerMap);
	}

}
