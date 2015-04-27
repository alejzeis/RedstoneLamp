package redstonelamp.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import redstonelamp.RedstoneLamp;

public class Logger {
	/**
	 * Sends an info message to the Server Console
	 * 
	 * @param String message
	 */
	public void info(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [INFO] " + message);
	}

	/**
	 * Sends a debug message to the Server Console<br><br>
	 * 
	 * Only appears on the console if DEBUG_MODE is enabled
	 * 
	 * @param String message
	 */
	public void debug(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if(RedstoneLamp.DEBUG)
			System.out.println(sdf.format(cal.getTime()) + " [DEBUG] " + message);
	}

	/**
	 * Sends a warning message to the Server Console
	 * 
	 * @param String message
	 */
	public void warn(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}

	/**
	 * Sends a warning message to the Server Console
	 * 
	 * @param String message
	 */
	public void warning(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}

	/**
	 * Sends an error message to the Server Console
	 * 
	 * @param String message
	 */
	public void error(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [ERROR] " + message);
	}

	/**
	 * Stops the server with a fatal error sent to the console
	 * 
	 * @param String message
	 */
	public void fatal(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [FATAL] " + message);
		System.exit(1);
	}

	/**
	 * Sends a developer message to the Server Console<br><br>
	 * 
	 * Only appears on the console if DEVELOPER_MODE is enabled
	 * 
	 * @param String message
	 */
	public void dev(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if(RedstoneLamp.DEVELOPER)
			System.out.println(sdf.format(cal.getTime()) + " [DEVELOPER] " + message);
	}
}
