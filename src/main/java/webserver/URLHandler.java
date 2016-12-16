package webserver;

public class URLHandler {
	private String url;
		
	public URLHandler(HttpRequest request) {
		this.url = request.getUrl();
	}
	
	public String resolve() {
		if(this.url.equals("/"))
			return "root";
		
		if(this.url.startsWith("/user/create"))
			return "user";
		
		return "";
	}
	
}
