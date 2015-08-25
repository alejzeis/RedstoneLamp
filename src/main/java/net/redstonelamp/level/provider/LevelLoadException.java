package net.redstonelamp.level.provider;

/**
 * This exception is thrown whenever there is an exception loading a Level
 *
 * @author RedstoneLamp Team
 */
public class LevelLoadException extends RuntimeException{

    public LevelLoadException(String message) {
        super(message);
    }

    public LevelLoadException(Exception e) {
        super(e);
    }

}
