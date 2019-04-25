package sdp.http.response;

import java.util.HashMap;
import java.util.Map;

/**
 * The HttpResponse class contains information about the response.
 * 
 * @author Subhadeep Sen
 *
 */
public class HttpResponse {
	private String httpHeader;
	private String httpBody;
	private String responseCode;
	private Map<String, String> headerMap;
	private Map<String, String> parameterMap;

	public HttpResponse() {
		parameterMap = new HashMap<>();
		headerMap = new HashMap<>();
	}

	/**
	 * @return the httpHeader
	 */
	public String getHttpHeader() {
		return httpHeader;
	}

	/**
	 * @param httpHeader
	 *            the httpHeader to set
	 */
	public void setHttpHeader(String httpHeader) {
		this.httpHeader = httpHeader;
	}

	/**
	 * @return the httpBody
	 */
	public String getHttpBody() {
		return httpBody;
	}

	/**
	 * @param httpBody
	 *            the httpBody to set
	 */
	public void setHttpBody(String httpBody) {
		this.httpBody = httpBody;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the headerMap
	 */
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	/**
	 * @param headerMap
	 *            the headerMap to set
	 */
	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	/**
	 * @return the parameterMap
	 */
	public Map<String, String> getParameterMap() {
		return parameterMap;
	}

	/**
	 * @param parameterMap
	 *            the parameterMap to set
	 */
	public void setParameterMap(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	/**
	 * Adds parameter to parameter Map.
	 * 
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, String value) {
		parameterMap.put(key, value);
	}

}
