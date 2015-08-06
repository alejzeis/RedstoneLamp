package redstonelamp.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;

public class MainLogger {
	private PrintWriter writer;
	private File log = new File("latest.log");
	private Collection<String> lines = new ArrayList<String>();
	
	public MainLogger() {
		log.delete();
	}
	
	/**
	 * A normal Server Message
	 * 
	 * @param String
	 */
	public void info(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		message = sdf.format(cal.getTime()) + " [INFO] " + message;
		writeToLog(message);
		System.out.println(message);
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
		message = sdf.format(cal.getTime()) + " [DEBUG] " + message;
		if(Boolean.parseBoolean(RedstoneLamp.properties.getProperty("debug", "false")))
			System.out.println(message);
		writeToLog(message);
	}
	
	/**
	 * Warns the server of a minor issue
	 * 
	 * @param String
	 */
	public void warn(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		message = sdf.format(cal.getTime()) + " [WARNING] " + message;
		System.out.println(message);
		writeToLog(message);
	}
	
	/**
	 * Warns the server of a minor issue
	 * 
	 * @param String
	 */
	public void warning(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		message = sdf.format(cal.getTime()) + " [WARNING] " + message;
		System.out.println(message);
		writeToLog(message);
	}
	
	/**
	 * Shows an issue to the server that may cause problems
	 * 
	 * @param String
	 */
	public void error(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		message = sdf.format(cal.getTime()) + " [ERROR] " + message;
		System.out.println(message);
		writeToLog(message);
	}
	
	/**
	 * Shows a message right before a server crash
	 * 
	 * @param String
	 */
	public void fatal(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		message = sdf.format(cal.getTime()) + " [FATAL] " + message;
		System.out.println(message);
		writeToLog(message);
		if(RedstoneLamp.getServerInstance() instanceof Server)
			RedstoneLamp.getServerInstance().stop();
		else
			System.exit(1);
	}

	public void noTag(String message) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(cal.getTime()) + " " + message);
	}
	
	public void writeToLog(String string) {
		lines.add(string);
	}
	
	public void close() {
		try {
			FileUtils.writeLines(log, lines);
		} catch (IOException e) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			System.out.println(sdf.format(cal.getTime()) + " [ERROR] Unable to write log!");
		}
	}
}