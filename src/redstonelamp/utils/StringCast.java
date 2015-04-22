package redstonelamp.utils;

import redstonelamp.RedstoneLamp;

public class StringCast {
	public static boolean isBoolean(String string) {
		if(string.equals("true") || string.equals("false") || string.equals("on") || string.equals("off") || string.equals("1") || string.equals("0"))
			return true;
		return false;
	}
	
	public static boolean toBoolean(String string) {
		if(string.equals("true") || string.equals("on") || string.equals("1"))
			return true;
		if(string.equals("true") || string.equals("false") || string.equals("on") || string.equals("off") || string.equals("1") || string.equals("0"))
			return false;
		RedstoneLamp.logger.fatal("Failed to cast String to boolean!");
		return false;
	}
	
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
