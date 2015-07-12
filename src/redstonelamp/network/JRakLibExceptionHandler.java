package redstonelamp.network;

/**
 * Exception handler for the JRakLib thread.
 */
public class JRakLibExceptionHandler implements Thread.UncaughtExceptionHandler{
    private Throwable error;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.error = e;
    }

    public String getLastExceptionMessage(){
        return error.getMessage();
    }

    public void printLastExceptionTrace(){
        error.printStackTrace(System.err);
    }
}
