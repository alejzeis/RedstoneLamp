package redstonelamp.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import redstonelamp.RedstoneLamp;

public class ServerLogger {
	public static Calendar cal = Calendar.getInstance();
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public static void info(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [INFO] " + message);
	}
	
	public static void debug(String message) {
		if(RedstoneLamp.DEGUG)
			System.out.println(sdf.format(cal.getTime()) + " [DEBUG] " + message);
	}
	
	public static void warn(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [WARN] " + message);
	}

	public static void error(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [ERROR] " + message);
	}
	

	public static void fatal(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [FATAL] " + message);
		System.exit(1);
	}
}
