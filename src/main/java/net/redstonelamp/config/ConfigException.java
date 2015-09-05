package net.redstonelamp.config;

/**
 * An Exception thrown when reading/writing values from a config.
 *
 * @author RedstoneLamp Team
 */
public class ConfigException extends Exception{

    public ConfigException(String message) {
        super(message);
    }
}
