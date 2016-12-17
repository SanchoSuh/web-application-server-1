package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;

public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	private HttpRequest request;
	//private HttpResponse response;
	
	private String domainString;
	private String subDomainString;
	
	public UserController(HttpRequest request) {
		this.request = request;
		
		this.domainString = "/user";
		this.subDomainString = splitDomainURL(this.domainString, request.getUrl());
	}
	
	private String splitDomainURL(String domainURL, String fullURL) {
		int index = domainURL.length();
		return fullURL.substring(index);
	}
	
	public String run() {
		String returnURL = "";
		
		if(this.request.getMethod().equals("GET")) {
			returnURL = domainString + subDomainString;
		} 
		
		if(this.request.getMethod().equals("POST")) {
			
			if(this.subDomainString.startsWith("/create")) {
				returnURL = userSignUp();
			}
		}
				
		return returnURL;
	}
	
	private String userSignUp() {
		log.debug("User SignUp");
		User user = new User(request.getParam("userId"), request.getParam("password"), request.getParam("name"), request.getParam("email"));
		log.debug("User instance created : " + user.toString());
		
		return "redirect:/";
	}

	public String toString() {
		return "UserController : domainString : " + this.domainString + ", subDomainString : " + this.subDomainString;
	}
}
