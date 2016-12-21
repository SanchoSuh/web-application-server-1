package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserSigninController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(UserSigninController.class);
	
	public void doPost(HttpRequest request, HttpResponse response) {
		log.debug("UserSigninController : doPost");
		
		User user = DataBase.findUserById(request.getParam("userId"));
		if(user == null) {
			log.debug("no user");
			response.redirect("/user/login_failed.html");
		}
		
		if(user.getPassword().equals(request.getParam("password"))) {
			log.debug("password correct. Login success");
			log.debug("set cookie true in Controller");
			response.redirectLogin("/index.html", true);
		} else {
			response.redirect("/user/login_failed.html");
		}
	}
}
