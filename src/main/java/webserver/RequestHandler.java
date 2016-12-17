package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

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
			Reader reader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(reader);
			
			HttpRequest request = new HttpRequest(br);
			
			String file = "";
			String contentType = "";
			HttpResponse response;
			
			URLHandler urlHandler = new URLHandler(request);
			String domain = urlHandler.resolve();
			
			if(domain.equals("root")) {
				file = "/index.html";
				byte[] body = Files.readAllBytes(new File("./webapp"+file).toPath());
				contentType = getContentType(file);
				response = new HttpResponse("200", body.length, contentType);
				
				DataOutputStream dos = new DataOutputStream(out);
				response.response(dos, body);
			}
				
			if(domain.equals("user")) {
				UserController userController = new UserController(request);
				log.debug(userController.toString());
				
				file = userController.run();
				
				DataOutputStream dos = new DataOutputStream(out);
				
				if(!file.startsWith("redirect:")) {
					byte[] body = Files.readAllBytes(new File("./webapp"+file).toPath());
					contentType = getContentType(file);
					response = new HttpResponse("200", body.length, contentType);
					response.response(dos, body);
					
					//log.debug(response.toString());
				} else {
					int index = file.indexOf(":");
					String subFile = file.substring(index + 1);
					
					response = new HttpResponse("302");
					response.redirect(dos, subFile);
					//log.debug(response.toString());
				}
				
			}
			
			if(domain.equals("css")) {
				file = request.getUrl();
				
				byte[] body = Files.readAllBytes(new File("./webapp"+file).toPath());
				contentType = getContentType(file);
				response = new HttpResponse("200", body.length, contentType);
				
				DataOutputStream dos = new DataOutputStream(out);
				response.response(dos, body);
			}
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private String getContentType(String file) {
		if(file.startsWith("/css/"))
			return "text/css";
		if(file.startsWith("/js/"))
			return "application/javascript";
					
		return "text/html";
	}
	
}
