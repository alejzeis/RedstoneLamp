package net.redstonelamp.response;

public class ChatResponse extends Response {
	
	
	public byte type;
	public String source;
	public String message;
	public String[] parameters;
	
	public ChatResponse(byte type, String source, String message, String[] parameters) {
		this.type = type;
		this.source = source;
		this.message = message;
		this.parameters = parameters;
	}
	
	public ChatResponse(byte type, String source, String message) {
		this(type, source, message, null);
	}
	
}
