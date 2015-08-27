package net.redstonelamp.plugin.exception;

public class AsyncAPICallException extends Exception {

	/**
	 * A simple UID for this exception
	 */
	private static final long serialVersionUID = 4889993682435238971L;
	
	public AsyncAPICallException(String msg){
		super(msg);
	}
}
