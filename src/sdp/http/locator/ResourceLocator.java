package sdp.http.locator;

import static sdp.http.constant.HttpConstant.PATH_SEPARATOR;
import static sdp.http.constant.HttpStatus.INTERNAL_SERVER_ERROR;
import static sdp.http.constant.HttpStatus.OK;
import static sdp.http.constant.HttpStatus.RESOURCE_NOT_FOUND;

import java.io.File;

import sdp.http.config.ServerConfig;
import sdp.http.response.HttpResponse;

/**
 * ResourceLocator class locates for the requested view.
 * 
 * @author Subhadeep Sen
 *
 */
public class ResourceLocator {

	private static final String ERROR = "error";
	private static final String NOT_FOUND = "src/sdp/http/views/notFound.html";
	private static final String SERVER_ERROR = "src/sdp/http/views/serverError.html";

	/**
	 * Locates for the requested view. If not found then returns 404 view. Incase of
	 * exception returns 500 view.
	 * 
	 * @param viewName
	 * @param httpResponse
	 * @return File
	 */
	public static File getView(String viewName, HttpResponse httpResponse) {
		ServerConfig serverConfig = ServerConfig.getInstance();
		File view = null;
		if (viewName.isEmpty()) {
			view = new File(NOT_FOUND);
			httpResponse.setResponseCode(RESOURCE_NOT_FOUND);
		} else if (ERROR.equals(viewName)) {
			view = new File(SERVER_ERROR);
			httpResponse.setResponseCode(INTERNAL_SERVER_ERROR);
		} else {
			view = new File(serverConfig.getResourceFilePath() + PATH_SEPARATOR + viewName + ".html");
			httpResponse.setResponseCode(OK);
		}

		if (!view.exists())
			view = new File(NOT_FOUND);

		return view;

	}
}
