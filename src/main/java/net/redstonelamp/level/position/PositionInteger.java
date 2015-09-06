/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
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
