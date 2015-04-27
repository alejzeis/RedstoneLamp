package redstonelamp.utils;

import redstonelamp.RedstoneLamp;

public class StringCast {
	/**
	 * Returns true if the String is a boolean
	 * 
	 * @param String string
	 * @return boolean
	 */
	public static boolean isBoolean(String string) {
		if(string.equals("true") || string.equals("false") || string.equals("on") || string.equals("off") || string.equals("1") || string.equals("0"))
			return true;
		return false;
	}
	
	/**
	 * Returns String->boolean
	 * 
	 * @param String string
	 * @return boolean
	 */
	public static boolean toBoolean(String string) {
		if(string.equals("true") || string.equals("on") || string.equals("1"))
			return true;
		if(string.equals("true") || string.equals("false") || string.equals("on") || string.equals("off") || string.equals("1") || string.equals("0"))
			return false;
		RedstoneLamp.logger.fatal("Failed to cast String to boolean!");
		return false;
	}
	
	/**
	 * Returns true if the String is an int
	 * 
	 * @param String string
	 * @return boolean
	 */
	public static boolean isInt(String string) {
		try {
			Integer.parseInt(string);
		} catch(NumberFormatException e) {
			if(RedstoneLamp.DEVELOPER)
				e.printStackTrace();
			RedstoneLamp.logger.error("There was a NumberFormatException!");
			return false;
		} catch(NullPointerException e) {
			if(RedstoneLamp.DEVELOPER)
				e.printStackTrace();
			RedstoneLamp.logger.error("There was a NullPointerException!");
			return false;
		}
		return true;
	}
	
	/**
	 * Returns String->int
	 * 
	 * @param String string
	 * @return int
	 */
	public static int toInt(String string) {
		int number = 827492917;
		try {
			number = Integer.parseInt(string);
		} catch(NumberFormatException e) {
			if(RedstoneLamp.DEVELOPER)
				e.printStackTrace();
			RedstoneLamp.logger.fatal("Failed to cast String to int!");
		} catch(NullPointerException e) {
			if(RedstoneLamp.DEVELOPER)
				e.printStackTrace();
			RedstoneLamp.logger.fatal("Failed to cast String to int!");
		}
		if(number == 827492917)
			RedstoneLamp.logger.fatal("Failed to cast String to int!");
		return number;
	}
}
