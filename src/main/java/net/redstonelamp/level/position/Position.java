package net.redstonelamp.level.position;

import net.redstonelamp.level.Level;

/**
 * A Position that stores it's x, y, and z coordinates in doubles and the yaw and pitch in floats.
 *
 * @author RedstoneLamp Team
 */
public class Position {
    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;

    private final Level level;

    /**
     * Create a new Position associated with the specified <code>level</code>
     * @param level The Level this position is in.
     */
    public Position(Level level) {
        this.level = level;

        x = 0;
        y = 0;
        z = 0;

        yaw = 0;
        pitch = 0;
    }

    public Position(double x, double y, double z, Level level) {
        this.level = level;

        this.x = x;
        this.y = y;
        this.z = z;

        yaw = 0;
        pitch = 0;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Level getLevel() {
        return level;
    }
}
