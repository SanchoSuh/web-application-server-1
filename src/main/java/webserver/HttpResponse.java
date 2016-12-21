package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	
	private DataOutputStream dos = null;
	private String returnCode;
	private int contentLength;
	private String contentType;
		
	public HttpResponse(OutputStream out) {
		dos = new DataOutputStream(out);
	}
	
	public HttpResponse(String returnCode, int contentLength, String contentType) {
		this.returnCode = returnCode;
		this.contentLength = contentLength;
		this.contentType = contentType;
	}
	
	public void response(String responseUrl) {
		this.returnCode = "200";
		
		try {
			byte[] body = Files.readAllBytes(new File("./webapp"+ responseUrl).toPath());
			this.contentType = getContentType(responseUrl);
			this.contentLength = body.length;
		
			response200Header();
			responseBody(body);	
		}catch(IOException e) {}
	}
	
	public void responseWithBody(String body) {
		byte[] contents = body.getBytes();
		this.returnCode = "200";
		this.contentType = "text/html";
		this.contentLength = contents.length;
		
		response200Header();
		responseBody(contents);	
	}
	
	public void redirect(String url) {
		this.returnCode = "302";
		
		try {
			log.debug("In redirect");
			dos.writeBytes("HTTP/1.1 " + this.returnCode + " Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.flush();
		} catch(IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public void redirectLogin(String url, boolean success) {
		this.returnCode = "302";
		
		try {
			log.debug("In redirectLogin");
			dos.writeBytes("HTTP/1.1 " + this.returnCode + " Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            if(success == true)
            	dos.writeBytes("Set-Cookie: logined=true\r\n");
            else
            	dos.writeBytes("Set-Cookie: logined=false\r\n");
            dos.flush();
		} catch(IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header() {
        this.returnCode = "200";
        
		try {
        	log.debug("In response200Header()");
            dos.writeBytes("HTTP/1.1 " + this.returnCode + " OK \r\n");
            dos.writeBytes("Content-Type: " + this.contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + this.contentLength + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
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
    
    public String toString() {
    	return "Content-type : " + this.contentType + ", Content-length : " + this.contentLength;
    }
}
