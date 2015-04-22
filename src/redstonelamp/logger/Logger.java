package redstonelamp.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import redstonelamp.RedstoneLamp;

public class Logger {
	public void info(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [INFO] " + message);
	}
	
	public void debug(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if(RedstoneLamp.DEBUG)
			System.out.println(sdf.format(cal.getTime()) + " [DEBUG] " + message);
	}
	
	public void warn(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}
	
	public void warning(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [WARNING] " + message);
	}
	
	public void error(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [ERROR] " + message);
	}
	
	public void fatal(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " [FATAL] " + message);
		System.exit(1);
	}
	
	public void dev(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if(RedstoneLamp.DEVELOPER)
			System.out.println(sdf.format(cal.getTime()) + " [DEVELOPER] " + message);
	}
}
