package sdp.http.response;

import static sdp.http.constant.HttpConstant.CRLF;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * ResponseSender sends the response taking from the response object.
 * 
 * @author Subhadeep Sen
 *
 */
public class ResponseSender {

	private PrintWriter out;

	public ResponseSender(OutputStream outputStream) {
		out = new PrintWriter(outputStream, true);
	}

	/**
	 * Sends the response.
	 * 
	 * @param httpResponse
	 */
	public void sendResponse(HttpResponse httpResponse) {
		/* sending the response header */
		out.print(httpResponse.getHttpHeader());
		/* sending the body line by line so that it does not exceed the buffer size */
		String[] responeBody = httpResponse.getHttpBody().split(CRLF);
		for (int i = 0; i < responeBody.length; i++) {
			out.println(responeBody[i]);
		}
		// out.print(httpResponse.getHttpBody());
		out.close();
	}
}
