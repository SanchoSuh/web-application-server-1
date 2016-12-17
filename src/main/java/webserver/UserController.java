package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	private HttpRequest request;
	//private HttpResponse response;
	private String domainString;
	private String subDomainString;
	private boolean cookie = false;
	
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
			if(this.subDomainString.equals("/list.html")) {
				log.debug("I'm in the list");
				if(!HttpRequest.loginCookie) {
					log.debug("I don't have login cookie");
					return "redirect:/user/login.html";
				}
			}
			returnURL = domainString + subDomainString;
		} 
		
		if(this.request.getMethod().equals("POST")) {
			
			if(this.subDomainString.equals("/create")) {
				returnURL = handleUserSignUp();
			}
			
			if(this.subDomainString.equals("/login")) {
				returnURL = handleUserSignIn();
				if(!returnURL.contains("/user/login_failed.html")) {
					this.cookie = true;
					log.debug("set cookie true in Controller");
				}
			}
			
		}
				
		return returnURL;
	}
	
	private String handleUserSignIn() {
		log.debug("User SignIn");
		
		User user = DataBase.findUserById(request.getParam("userId"));
		if(user == null) {
			return "redirect:/user/login_failed.html";
		}
		
		if(user.getPassword().equals(request.getParam("password"))) {
			return "redirect:/";
		}
		
		return "redirect:/user/login_failed.html";
	}

	private String handleUserSignUp() {
		log.debug("User SignUp");
		User user = new User(request.getParam("userId"), request.getParam("password"), request.getParam("name"), request.getParam("email"));
		log.debug("User instance created : " + user.toString());
		DataBase.addUser(user);
		
		return "redirect:/";
	}
	
	public boolean getCookie() {
		return this.cookie;
	}

	public String toString() {
		return "UserController : domainString : " + this.domainString + ", subDomainString : " + this.subDomainString;
	}
}
