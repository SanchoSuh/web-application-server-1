package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.HttpRequestUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private String method;
	private String url;
	private int returnCode;
	private Map<String, String> params;
	private int contentLength;
	
	public HttpRequest(String method, String url, int returnCode, Map<String, String> params, int contentLength) {
		this.method = method;
		this.url = url;
		this.returnCode = returnCode;
		this.params = params;
		this.contentLength = contentLength;
	}
	
	public HttpRequest(BufferedReader br) {
		
		try {
			String readLine = br.readLine();
			
			while(!"".equals(readLine)) {
				String[] tokens = readLine.split(" ");
				
				if(tokens[0].equals("GET")) {					
					this.method = tokens[0];					
					parseGetParam(tokens[1]);
				}
				
				if(tokens[0].equals("POST")) {
					this.method = tokens[0];
					this.url = tokens[1];
				}
				
				if(tokens[0].startsWith("Content-Length")) {
					this.contentLength = Integer.parseInt(tokens[1]);
				}
				
				readLine = br.readLine();
			}
		}catch (IOException e) {
			log.debug("InHttpRequest : readLine IO exception");
		}		
	}
	
	private void parseGetParam(String httpGetUrl) {
		log.debug(httpGetUrl);
				
		int index = httpGetUrl.indexOf("?");
		
		if( index < 0 ) {
			this.url = httpGetUrl;
		}
		else{
			String urlString = httpGetUrl.substring(0, index);
			String queryString = httpGetUrl.substring(index+1);
			
			log.debug("urlString : " + queryString + "/queryString : " + queryString);
			
			this.url = urlString;
			this.params = HttpRequestUtils.parseQueryString(queryString);
		}
	}

	public String getParam(String key) {
		return this.params.get(key);
	}
	
	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public int getReturnCode() {
		return returnCode;
	}
}
