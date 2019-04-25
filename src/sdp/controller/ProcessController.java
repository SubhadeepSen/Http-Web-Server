package sdp.controller;

import sdp.annotation.DefineController;
import sdp.annotation.HandlerMethod;
import sdp.http.constant.HttpMethod;
import sdp.http.request.HttpRequest;
import sdp.http.response.HttpResponse;

@DefineController
public class ProcessController {

	@HandlerMethod(value = "/index")
	public String getIndex() {
		return "index";
	}
	
	@HandlerMethod(value = "/sayHello", method = HttpMethod.GET)
	public String getSayHelloGet(HttpRequest request, HttpResponse response) {
		response.addParameter("name", request.getParameter("name"));
		response.addParameter("city", request.getParameter("city"));
		response.addParameter("company", request.getParameter("company"));
		return "sayHello";
	}
	
	@HandlerMethod(value = "/sayHello", method = HttpMethod.POST)
	public String getSayHelloPost(HttpRequest request, HttpResponse response) {
		response.addParameter("name", request.getParameter("name"));
		response.addParameter("city", request.getParameter("city"));
		response.addParameter("company", request.getParameter("company"));
		return "sayHello";
	}
}
