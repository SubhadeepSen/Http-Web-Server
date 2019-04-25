package sdp.http.config;

/**
 * ServerConfig is a singleton configuration class for the HTTP Web server. The
 * server uses the details to configure and run the server. If no configuration
 * is provided, the server will use default configurations. The static method
 * getInstance() can be called to get the instance with default configurations.
 * 
 * @author Subhadeep Sen
 *
 */
public final class ServerConfig {

	private static ServerConfig config;

	private String resourceFilePath;
	private String hostAddress;
	private int portNumber;
	private String applicationBasePath;
	private String defaultIndexPage;
	private String basePackage;

	private ServerConfig() {

	}

	public static ServerConfig getInstance() {
		if (config == null) {
			/*
			 * If there is no instance, creates an instance and initializes with the below
			 * default configurations.
			 */
			config = new ServerConfig();
			config.resourceFilePath = "src/pages";
			config.hostAddress = "localhost";
			config.portNumber = 8080;
			config.applicationBasePath = "/";
			config.defaultIndexPage = "index.html";
			config.basePackage = "src.controller";
		}
		return config;
	}

	/**
	 * @return the resourceFilePath
	 */
	public String getResourceFilePath() {
		return resourceFilePath;
	}

	/**
	 * @param resourceFilePath
	 *            The path of the HTML pages (views)
	 * 
	 */
	public void setResourceFilePath(String resourceFilePath) {
		this.resourceFilePath = resourceFilePath;
	}

	/**
	 * @return the hostAddress
	 */
	public String getHostAddress() {
		return hostAddress;
	}

	/**
	 * @param hostAddress
	 *            the hostAddress to set
	 */
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	/**
	 * @return the portNumber
	 */
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * @param portNumber
	 *            the portNumber to set
	 */
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * @return the applicationBasePath
	 */
	public String getApplicationBasePath() {
		return applicationBasePath;
	}

	/**
	 * @param applicationBasePath
	 *            The base path of the application (context path)
	 */
	public void setApplicationBasePath(String applicationBasePath) {
		this.applicationBasePath = applicationBasePath;
	}

	/**
	 * @return the defaultIndexPage
	 */
	public String getDefaultIndexPage() {
		return defaultIndexPage;
	}

	/**
	 * @param defaultIndexPage
	 *            A default HTML page
	 */
	public void setDefaultIndexPage(String defaultIndexPage) {
		this.defaultIndexPage = defaultIndexPage;
	}

	/**
	 * @return the basePackage
	 */
	public String getBasePackage() {
		return basePackage;
	}

	/**
	 * @param basePackage
	 *            The base package for the annotated classes
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

}
