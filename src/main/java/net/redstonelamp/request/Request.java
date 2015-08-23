package net.redstonelamp.request;

/**
 * This class represents a protocol-independent request, which is then responded with a Response.
 *
 * @author RedstoneLamp Team
 */
public abstract class Request {
    public abstract void execute();
}
