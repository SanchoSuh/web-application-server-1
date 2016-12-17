package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLHandler {
	private static final Logger log = LoggerFactory.getLogger(URLHandler.class);
	private String url;
		
	public URLHandler(HttpRequest request) {
		this.url = request.getUrl();
	}
	
	public String resolve() {
		if(this.url.equals("/"))
			return "root";
		
		if(this.url.startsWith("/user"))
			return "user";
		
		if(this.url.startsWith("/css"))
			return "css";
		
		return "";
	}
	
}
