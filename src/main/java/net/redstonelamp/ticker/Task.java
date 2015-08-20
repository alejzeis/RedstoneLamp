package net.redstonelamp.ticker;

/**
 * Represents a Task that can be ran.
 *
 * @author RedstoneLamp Team
 */
public interface Task {
    void onRun(long tick);
    default void onFinalize(){}
}
