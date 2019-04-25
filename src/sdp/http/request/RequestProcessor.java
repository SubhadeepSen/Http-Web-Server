package sdp.http.request;

import static sdp.http.constant.HttpConstant.EMPTY;
import static sdp.http.constant.HttpConstant.PATH_SEPARATOR;

import java.util.ArrayList;
import java.util.List;

import sdp.http.config.ServerConfig;
import sdp.http.response.HttpResponse;
import sdp.processor.AnnotationProcessor;
import sdp.processor.AnnotationProcessorConfig;

/**
 * RequestProcessor class uses the Annotation Processing Engine to process the
 * request and get the view name that needs to be sent to client.
 * 
 * @author Subhadeep Sen
 *
 */
public class RequestProcessor {

	/**
	 * Starts the annotation processing engine for further execution.
	 * 
	 * @param httpRequest
	 * @param httpResponse
	 * @return String
	 */
	public String processRequest(HttpRequest httpRequest, HttpResponse httpResponse) {
		String resourceName = EMPTY;
		ServerConfig config = ServerConfig.getInstance();
		/*
		 * configuring the annotaion processor by setting the packageName for scanning
		 * annotations, the handler name and the http request method
		 */
		AnnotationProcessorConfig annotationProcessorConfig = new AnnotationProcessorConfig();
		annotationProcessorConfig.setHandlerName(PATH_SEPARATOR + httpRequest.getRequestedResource());
		annotationProcessorConfig.setHttpMethod(httpRequest.getHttpMethod());
		List<Object> objects = new ArrayList<>();
		objects.add(httpRequest);
		objects.add(httpResponse);
		/*
		 * adding external objects (request and response) for injecting field dependency
		 * and method parameter
		 */
		annotationProcessorConfig.setObjects(objects);
		annotationProcessorConfig.setPackageName(config.getBasePackage());
		AnnotationProcessor annotationProcessor = new AnnotationProcessor(annotationProcessorConfig);
		/* staring the processing engine */
		Object processedResponse = annotationProcessor.process();
		if (processedResponse instanceof String) {
			resourceName = (String) processedResponse;
		}
		return resourceName;
	}

}
