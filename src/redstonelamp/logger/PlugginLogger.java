package redstonelamp.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PlugginLogger {

	private static Calendar cal         = Calendar.getInstance();
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public void info(String message) {
		System.out.println(sdf.format(cal.getTime()) + " [INFO] " + message);
	}
	
}
