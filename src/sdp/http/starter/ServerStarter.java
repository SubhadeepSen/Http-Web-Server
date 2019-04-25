package sdp.http.starter;

import sdp.http.config.ServerConfig;
import sdp.http.server.HttpServer;

public class ServerStarter {

	public static void main(String[] args) {
		ServerConfig newInstance = ServerConfig.getInstance();
		newInstance.setBasePackage("sdp.controller");
		newInstance.setResourceFilePath("src/pages");
		HttpServer server = new HttpServer();
		server.start();
	}
}
