package webserver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.UserListController;
import controller.UserSigninController;
import controller.UserSignupController;

public class URLHandler {
	private static final Logger log = LoggerFactory.getLogger(URLHandler.class);
	
	private static Map<String, Controller> mappedController = new HashMap<String, Controller>();
	private String url;
		
	static {
		mappedController.put("/user/create", new UserSignupController());
		mappedController.put("/user/login", new UserSigninController());
		mappedController.put("/user/list", new UserListController());
	}
	
	
	public URLHandler(HttpRequest request) {
		this.url = request.getUrl();
	}
	
	public static Controller resolve(String requestUrl) {
		log.debug("In URL resolve returning Controller : " + requestUrl);
		return mappedController.get(requestUrl);
	}
	
}
