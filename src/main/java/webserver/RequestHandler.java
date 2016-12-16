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

import model.User;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    
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
			
			if(request.getMethod().equals("GET")) {
				file = request.getUrl();
			}
			
			byte[] body = Files.readAllBytes(new File("./webapp"+file).toPath());
			DataOutputStream dos = new DataOutputStream(out);
			
			HttpResponse response = new HttpResponse("200", body.length, "text/html");
			response.response(dos, body);
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	
	private String readHttpMessage(BufferedReader br) {

		String parsedLine = null;
		
		try {
			String line = br.readLine();
			log.debug(line);
			
			while(!"".equals(line)) {
				parsedLine = parseLine(line);
				log.debug(parsedLine);
				
				if(parsedLine.contains("/user/create")) {
					int index = parsedLine.indexOf("?");
					String queryString = parsedLine.substring(index + 1);
					Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
					log.debug(queryString);
					
					User user = new User(params);
					System.out.println("#### Here #####" + user.getUserId() + " " + user.getName() + " " + user.getEmail());
				}
				if(parsedLine != null) {
					return parsedLine;
				}
				line = br.readLine();
			}
		} catch(IOException e) {
			log.debug(e.getMessage());
		}		
		
		return null;
	}
	
	private String parseLine(String line) {
		String[] tokens = line.split(" ");
			
		for(int i=0 ; i < tokens.length ; i++) {
			log.debug(tokens[i]);
			
			if(tokens[i].equals("GET")) {
				return tokens[i+1];
			}
		}
		
		return null;
	}


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
