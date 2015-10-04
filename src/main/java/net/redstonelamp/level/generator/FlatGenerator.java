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
package net.redstonelamp.level.generator;

import java.nio.ByteBuffer;

import net.redstonelamp.item.Items;
import net.redstonelamp.level.Chunk;
import net.redstonelamp.level.ChunkPosition;
import net.redstonelamp.level.Level;

/**
 * This Simple FlatGenerator generates an infinite world of grass.
 *
 * @author RedstoneLamp Team
 */
public class FlatGenerator implements Generator{
    private final Level level;

    public FlatGenerator(Level level, Level.LevelParameters params){
        this.level = level;
    }

    @Override
    public Chunk generateChunk(ChunkPosition position){
        Chunk c = new Chunk(position);

        ByteBuffer bb = ByteBuffer.allocate(16 * 16 * 128);
        for(int blockX = 0; blockX < 16; blockX++){
            for(int blockZ = 0; blockZ < 16; blockZ++){
                //Put a bedrock block on the lowest block in the column, and then loop through the rest of the
                //127 blocks and put grass and finally air
                bb.put((byte) Items.BEDROCK);
                bb.put((byte) Items.DIRT);
                bb.put((byte) Items.DIRT);
                bb.put((byte) Items.DIRT);
                bb.put((byte) Items.GRASS);
                for(int blockY = 0; blockY < 123; blockY++){
                    bb.put((byte) Items.AIR);
                }
            }
        }
        c.setBlockIds(bb.array());

        byte[] meta = new byte[16384];
        for(int i = 0; i < 16384; i++){
            meta[i] = 0x00;
        }
        c.setBlockMeta(meta);

        byte[] skylight = new byte[16384];
        for(int i = 0; i < 16384; i++){
            skylight[i] = (byte) 0xFF;
        }
        c.setSkylight(skylight);

        byte[] blocklight = new byte[16384];
        for(int i = 0; i < 16384; i++){
            blocklight[i] = (byte) 0;
        }
        c.setBlocklight(blocklight);

        byte[] heightmap = new byte[256];
        for(int i = 0; i < 256; i++){
            heightmap[i] = (byte) 0xFF;
        }
        c.setHeightmap(heightmap);

        ByteBuffer colors = ByteBuffer.allocate(1024);
        for(int i = 0; i < 256; i++){ //Biome Colors
            colors.put((byte) 0x01);
            colors.put((byte) 0x85);
            colors.put((byte) 0xB2);
            colors.put((byte) 0x4A);
        }
        c.setBiomeColors(colors.array());

        return c;
    }
}
