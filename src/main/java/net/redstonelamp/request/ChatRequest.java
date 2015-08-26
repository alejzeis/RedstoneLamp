package net.redstonelamp.request;


public class ChatRequest extends Request{
	
	public static final byte TYPE_RAW = 0;
	public static final byte TYPE_CHAT = 1;
	public static final byte TYPE_TRANSLATION = 2;
	public static final byte TYPE_POPUP = 3;
	public static final byte TYPE_TIP = 4;
	
	public byte type;
	public String source;
	public String message;
	public String[] parameters;
	
	public ChatRequest(byte type) {
		this.type = type;
	}
	
	@Override
	public void execute() {
		// TODO?
	}
	
}
