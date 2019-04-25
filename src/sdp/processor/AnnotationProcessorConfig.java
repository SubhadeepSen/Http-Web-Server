package sdp.processor;

import java.util.List;

/**
 * The AnnotationProcessorConfig contains the required information needed by the
 * processor engine.
 * 
 * @author Subhadeep Sen
 */
public class AnnotationProcessorConfig {

	private String packageName;
	private String handlerName;
	private String httpMethod;

	private List<Object> objects;

	public AnnotationProcessorConfig() {

	}

	public AnnotationProcessorConfig(String packageName, String handlerName, String httpMethod) {
		super();
		this.packageName = packageName;
		this.handlerName = handlerName;
		this.httpMethod = httpMethod;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the handlerName
	 */
	public String getHandlerName() {
		return handlerName;
	}

	/**
	 * @param handlerName
	 *            the handlerName to set
	 */
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
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
	 * @return the objects
	 */
	public List<Object> getObjects() {
		return objects;
	}

	/**
	 * @param objects
	 *            the objects to set
	 */
	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}

}
