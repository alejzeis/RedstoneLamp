package net.redstonelamp.level.position;

import net.redstonelamp.level.Level;

/**
 * Stores a position using Integers. This class DOES NOT
 * store yaw or pitch.
 *
 * @author RedstoneLamp Team
 */
public class PositionInteger {
    private int x;
    private int y;
    private int z;

    private final Level level;

    /**
     * Create a new PositionInteger associated with the specified <code>level</code>
     *
     * @param level The Level this position is in.
     */
    public PositionInteger(Level level){
        this.level = level;

        x = 0;
        y = 0;
        z = 0;
    }

    public PositionInteger(int x, int y, int z, Level level){
        this.level = level;

        this.x = x;
        this.y = y;
        this.z = z;
    }


    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getZ(){
        return z;
    }

    public void setZ(int z){
        this.z = z;
    }

    public Level getLevel(){
        return level;
    }
}
