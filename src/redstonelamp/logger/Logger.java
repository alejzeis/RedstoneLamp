package redstonelamp.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import redstonelamp.RedstoneLamp;

public class Logger {
	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public void info(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [INFO] " + message);
	}
	
	public void debug(String message) {
		if(RedstoneLamp.DEBUG)
			System.out.println(sdf.format(cal.getTime()) + " [DEBUG] " + message);
	}
	
	public void warn(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}
	
	public void warning(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}

	public void error(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [ERROR] " + message);
	}
	
	public void fatal(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [FATAL] " + message);
		RedstoneLamp.server.stop();
	}
}
