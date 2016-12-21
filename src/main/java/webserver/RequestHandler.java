package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import db.DataBase;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private DataBase db;
    
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			HttpRequest request = new HttpRequest(in);
			HttpResponse response = new HttpResponse(out);
			
			URLHandler urlHandler = new URLHandler(request);
			
			if(request.getUrl().equals("/") || request.getUrl().equals("/index.html")) {
				log.debug("Root or index.html");
				response.response("/index.html");
			} else {
				Controller controller = urlHandler.resolve(request.getUrl());
				
				if(controller == null) {
					log.debug("returned controller null. URL : " + request.getUrl());
					response.response(request.getUrl());
				} else {
					log.debug("returned controller : " + controller.toString());
					controller.service(request, response);
				}
			}
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
}
