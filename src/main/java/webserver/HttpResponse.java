package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	
	private String returnCode;
	private int contentLength;
	private String contentType;
		
	public HttpResponse(String returnCode) {
		this.returnCode = returnCode;
	}
	
	public HttpResponse(String returnCode, int contentLength, String contentType) {
		this.returnCode = returnCode;
		this.contentLength = contentLength;
		this.contentType = contentType;
	}
	
	public void response(DataOutputStream dos, byte[] body) {
		switch(this.returnCode) {
		case "200":
			response200Header(dos);
			break;
		default :
			response200Header(dos);
			break;
		}
		
		responseBody(dos, body);
	}
	
	public void redirect(DataOutputStream dos, String url) {
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
	
	public void redirectLogin(DataOutputStream dos, String url, boolean success) {
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

	private void response200Header(DataOutputStream dos) {
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    public String toString() {
    	return "Content-type : " + this.contentType + ", Content-length : " + this.contentLength;
    }
}
