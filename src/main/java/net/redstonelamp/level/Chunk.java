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
 * Represents a 16 * 16 * 128 chunk section.
 *
 * @author RedstoneLamp team
 */
public class Chunk{

    private final ChunkPosition position;

    private byte[] blockIds;
    private byte[] blockMeta;
    private byte[] skylight;
    private byte[] blocklight;
    private byte[] heightmap;
    private byte[] biomeColors;

    public Chunk(ChunkPosition position){
        this.position = position;
    }

    public Chunk(ChunkPosition position, byte[] blockIds, byte[] blockMeta, byte[] skylight, byte[] blocklight, byte[] heightmap, byte[] biomeColors){
        this.position = position;
        this.blockIds = blockIds;
        this.blockMeta = blockMeta;
        this.skylight = skylight;
        this.blocklight = blocklight;
        this.heightmap = heightmap;
        this.biomeColors = biomeColors;
    }

    public byte[] getBiomeColors(){
        return biomeColors;
    }

    public byte[] getHeightmap(){
        return heightmap;
    }

    public byte[] getBlocklight(){
        return blocklight;
    }

    public byte[] getSkylight(){
        return skylight;
    }

    public byte[] getBlockMeta(){
        return blockMeta;
    }

    public byte[] getBlockIds(){
        return blockIds;
    }

    public void setBlockIds(byte[] blockIds){
        this.blockIds = blockIds;
    }

    public void setBlockMeta(byte[] blockMeta){
        this.blockMeta = blockMeta;
    }

    public void setSkylight(byte[] skylight){
        this.skylight = skylight;
    }
    public void setBlocklight(byte[] blocklight){
        this.blocklight = blocklight;
    }

    public void setHeightmap(byte[] heightmap){
        this.heightmap = heightmap;
    }

    public void setBiomeColors(byte[] biomeColors){
        this.biomeColors = biomeColors;
    }

    public ChunkPosition getPosition(){
        return position;
    }
}
