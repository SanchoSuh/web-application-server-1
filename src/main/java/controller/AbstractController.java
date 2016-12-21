package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstractController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(AbstractController.class);
	
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		if(request.getMethod().equals("GET")) {
			log.debug("Abstract , GET");
			doGet(request, response);
		}
		else {
			log.debug("Abstract , POST");
			doPost(request, response);
		}
			
	}
	
	protected void doGet(HttpRequest request, HttpResponse response) {}
	protected void doPost(HttpRequest request, HttpResponse response) {}
}
