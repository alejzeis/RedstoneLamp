package net.redstonelamp.response;

public class ChatResponse extends Response {

	public String source = "";
	public String message;
	
	public ChatResponse(String message) {
		this.message = message;
	}
	
}
