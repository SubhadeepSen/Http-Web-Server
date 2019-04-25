package sdp.http.request;

import java.util.HashMap;
import java.util.Map;

/**
 * The HttpRequest class contains information about the request.
 * 
 * @author Subhadeep Sen
 *
 */
public class HttpRequest {

	private String httpMethod;
	private String httpHeader;
	private String requestedResource;
	private String httpBody;
	private Map<String, String> headerMap;
	private Map<String, String> parameterMap;

	public HttpRequest() {
		parameterMap = new HashMap<>();
		headerMap = new HashMap<>();
	}

	/**
	 * @return the httpMethod
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod
	 *            the httpMethod to set
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
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
	 * @return the requestedResource
	 */
	public String getRequestedResource() {
		return requestedResource;
	}

	/**
	 * @param requestedResource
	 *            the requestedResource to set
	 */
	public void setRequestedResource(String requestedResource) {
		this.requestedResource = requestedResource;
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
	 * Returns the parameter value.
	 * 
	 * @param name
	 * @return value
	 */
	public String getParameter(String name) {
		return parameterMap.get(name);
	}

}
