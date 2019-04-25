package sdp.http.response;

import static sdp.http.constant.HttpConstant.CRLF;
import static sdp.http.constant.HttpConstant.EMPTY;
import static sdp.http.constant.HttpHeaderConstant.CONNECTION;
import static sdp.http.constant.HttpHeaderConstant.CLOSE;
import static sdp.http.constant.HttpHeaderConstant.CONTENT_LENGTH;
import static sdp.http.constant.HttpHeaderConstant.CONTENT_TYPE;
import static sdp.http.constant.HttpHeaderConstant.DATE;
import static sdp.http.constant.HttpHeaderConstant.HTTP_200_OK;
import static sdp.http.constant.HttpHeaderConstant.HTTP_404_NOT_FOUND;
import static sdp.http.constant.HttpHeaderConstant.HTTP_500_INTERNAL_SERVER_ERROR;
import static sdp.http.constant.HttpHeaderConstant.SERVER;
import static sdp.http.constant.HttpHeaderConstant.SERVER_NAME;
import static sdp.http.constant.HttpHeaderConstant.TEXT_HTML;
import static sdp.http.constant.HttpStatus.INTERNAL_SERVER_ERROR;
import static sdp.http.constant.HttpStatus.OK;
import static sdp.http.constant.HttpStatus.RESOURCE_NOT_FOUND;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * ResponseProcessor is a preprocessor class.
 * 
 * @author Subhadeep Sen
 *
 */
public class ResponseProcessor {

	/**
	 * Processes through the view and replaces all the variables, ${variableName}
	 * with its respective value taking from the parameter Map. After processing,
	 * sets the final view to response body.
	 * 
	 * @param resource
	 * @param httpResponse
	 */
	public void processView(File resource, HttpResponse httpResponse) {
		Map<String, String> parameterMap = httpResponse.getParameterMap();
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(resource));
			String content = EMPTY;

			boolean isAvailable = false;
			if (parameterMap != null && parameterMap.size() != 0) {
				isAvailable = true;
			}
			int start = 0;
			int end = 0;
			String variable = EMPTY;
			String key = EMPTY;

			while ((content = fileReader.readLine()) != null) {
				if (isAvailable && content.contains("${")) {
					start = content.indexOf("${");
					end = content.indexOf("}");
					variable = content.substring(start, end + 1);
					key = content.substring(start + 2, end);
					content = content.replace(variable, parameterMap.get(key));
				}
				sb.append(content + CRLF);
			}
			fileReader.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		httpResponse.setHttpBody(sb.toString());
	}

	/**
	 * Builds the appropriate response header and sets to response header.
	 * 
	 * @param httpResponse
	 */
	public void buildHeader(HttpResponse httpResponse) {
		String statusCode = httpResponse.getResponseCode();
		StringBuilder sb = new StringBuilder();
		if (OK.equals(statusCode)) {
			sb.append(HTTP_200_OK + CRLF);
		} else if (RESOURCE_NOT_FOUND.equals(statusCode)) {
			sb.append(HTTP_404_NOT_FOUND + CRLF);
		} else if (INTERNAL_SERVER_ERROR.equals(statusCode)) {
			sb.append(HTTP_500_INTERNAL_SERVER_ERROR + CRLF);
		}
		sb.append(DATE + LocalDateTime.now().toString() + CRLF);
		sb.append(SERVER + SERVER_NAME + CRLF);
		sb.append(CONTENT_TYPE + TEXT_HTML + CRLF);
		sb.append(CONTENT_LENGTH + httpResponse.getHttpBody().length() + CRLF);
		sb.append(CONNECTION + CLOSE + CRLF);
		sb.append(CRLF);
		httpResponse.setHttpHeader(sb.toString());
	}
}
