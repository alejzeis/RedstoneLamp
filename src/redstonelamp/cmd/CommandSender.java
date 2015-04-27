package redstonelamp.cmd;

import redstonelamp.Server;

public interface CommandSender {
	/**
	 * Sends a message to the command issuer
	 * 
	 * @param message
	 */
	public void sendMessage(String message);
	
	/**
	 * Returns the Server class
	 * 
	 * @return Server
	 */
	public Server getServer();
	
	/**
	 * Returns the name of the command issuer
	 * 
	 * @return String
	 */
	public String getName();
}
