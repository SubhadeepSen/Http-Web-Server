package sdp.http.constant;

public final class HttpHeaderConstant {

	private HttpHeaderConstant() {

	}

	public static final String HTTP_200_OK = "HTTP/1.1 200 OK";
	public static final String HTTP_404_NOT_FOUND = "HTTP/1.1 404 Not Found";
	public static final String HTTP_500_INTERNAL_SERVER_ERROR = "HTTP/1.1 500 Internal Server Error";
	
	public static final String DATE = "Date: ";
	public static final String SERVER = "Server: ";
	public static final String CONTENT_TYPE = "Content-Type: ";
	public static final String CONTENT_LENGTH = "Content-Length: ";
	public static final String CONNECTION = "Connection: ";
	
	public static final String CLOSE = "close";
	public static final String TEXT_HTML = "text/html";
	public static final String SERVER_NAME = "Java Web Server";
	
}
