package net.redstonelamp.network.pc.serializer;

import org.json.simple.JSONObject;

public class ChatSerializer {
	
	private JSONObject chat;
	
	@SuppressWarnings("unchecked")
	public ChatSerializer() {
		this.chat = new JSONObject();
		this.chat.put("text", "");
	}
	
	public static String toChat(String text) {
		ChatSerializer chat = new ChatSerializer();
		chat.setText(text);
		return chat.toString();
	}
	
	@SuppressWarnings("unchecked")
	public void append(String text) {
		String old = this.chat.get("text").toString();
		this.chat.remove("text");
		this.chat.put("text", old + text);
	}
	
	@SuppressWarnings("unchecked")
	public void setText(String text) {
		this.chat.remove("text");
		this.chat.put("text", text);
	}
	
	@Override
	public String toString() {
		return chat.toJSONString();
	}
	
}
