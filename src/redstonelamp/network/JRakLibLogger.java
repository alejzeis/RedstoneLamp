package redstonelamp.network;

import net.beaconpe.jraklib.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * JRakLib's Logger.
 */
public class JRakLibLogger implements Logger {
    @Override
    public void notice(String message) {
        System.out.println(getTime()+" [NOTICE]: "+message);
    }

    @Override
    public void critical(String message) {
        System.err.println(getTime()+" [CRITICAL]: "+message);
    }

    @Override
    public void emergency(String message) {
        System.err.println(getTime()+" [EMERGENCY]: "+message);
    }

    private String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
