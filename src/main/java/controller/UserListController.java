package controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class UserListController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(UserListController.class);
	
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		log.debug("UserListController : doGet");
		if(!request.loginCookie) {
			log.debug("I don't have login cookie");
			response.redirect("/user/login.html");
		}
		
		Collection<User> users = DataBase.findAll();
		
		StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        response.responseWithBody(sb.toString());
	}

}
