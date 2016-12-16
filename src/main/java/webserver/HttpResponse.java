package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private String returnCode;
	private int contentLength;
	private String contentType;
	
	public HttpResponse(String returnCode, int contentLength, String contentType) {
		this.returnCode = returnCode;
		this.contentLength = contentLength;
		this.contentType = contentType;
	}
	
	public void response(DataOutputStream dos, byte[] body) {
		responseHeader(dos);
		responseBody(dos, body);
	}
	
	private void responseHeader(DataOutputStream dos) {
        try {
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
}
