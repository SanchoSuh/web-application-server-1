package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserSignupController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(UserSignupController.class);
	private boolean cookie = false;
	
	public UserSignupController() {
		
	}
	
	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		log.debug("In UserSignupController : doPost()");;
		User user = new User(request.getParam("userId"), request.getParam("password"), request.getParam("name"), request.getParam("email"));
		log.debug("User instance created : " + user.toString());
		DataBase.addUser(user);
		
		response.redirect("/");
		
	}
	/*
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
	*/
	
	public boolean getCookie() {
		return this.cookie;
	}
}
