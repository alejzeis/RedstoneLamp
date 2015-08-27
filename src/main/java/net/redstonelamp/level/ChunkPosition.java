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
package net.redstonelamp.level;

/**
 * A Position of a chunk, two integers x and z
 *
 * @author RedstoneLamp Team
 */
public class ChunkPosition{
    private int x;
    private int z;

    /**
     * Create a new ChunkPosition with the specified coordinates x and z
     *
     * @param x The X coordinate of the chunk
     * @param z The Z coordinate of the chunk
     */
    public ChunkPosition(int x, int z){
        this.x = x;
        this.z = z;
    }

    /**
     * Get the X coordinate of this ChunkPosition
     *
     * @return The X coordinate of the ChunkPosition
     */
    public int getX(){
        return x;
    }

    /**
     * Set the X coordinate of this ChunkPosition
     *
     * @param x The X coordinate of the ChunkPosition
     */
    public void setX(int x){
        this.x = x;
    }

    /**
     * Get the Z coordinate of this ChunkPosition
     *
     * @return The Z coordinate of the ChunkPosition
     */
    public int getZ(){
        return z;
    }

    /**
     * Set the Z coordinate of this ChunkPosition
     *
     * @param z The Z coordinate of the ChunkPosition
     */
    public void setZ(int z){
        this.z = z;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof ChunkPosition){
            ChunkPosition pos = (ChunkPosition) obj;
            return pos.getX() == getX() && pos.getZ() == getZ();
        }
        return obj.equals(this);
    }

    @Override
    public String toString(){
        return "ChunkPosition: {x: " + x + ", z:" + z + "}";
    }
}
