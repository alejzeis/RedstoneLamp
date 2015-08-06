package redstonelamp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;

public class MainLogger {
	/**
	 * A normal Server Message
	 * 
	 * @param String
	 */
	public void info(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [INFO] " + message);
	}
	
	/**
	 * Used for debuging things<br>
	 * This will only appear if debug mode is enabled in the server.properties
	 * 
	 * @param String
	 */
	public void debug(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if(Boolean.parseBoolean(RedstoneLamp.properties.getProperty("debug", "false")))
			System.out.println(sdf.format(cal.getTime()) + " [DEBUG] " + message);
	}
	
	/**
	 * Warns the server of a minor issue
	 * 
	 * @param String
	 */
	public void warn(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}
	
	/**
	 * Warns the server of a minor issue
	 * 
	 * @param String
	 */
	public void warning(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}
	
	/**
	 * Shows an issue to the server that may cause problems
	 * 
	 * @param String
	 */
	public void error(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [ERROR] " + message);
	}
	
	/**
	 * Shows a message right before a server crash
	 * 
	 * @param String
	 */
	public void fatal(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [FATAL] " + message);
		if(RedstoneLamp.getServerInstance() instanceof Server)
			RedstoneLamp.getServerInstance().stop();
		else
			RedstoneLamp.getServerInstance().stop();
	}

	public void noTag(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " " + message);
	}
}
