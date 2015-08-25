package net.redstonelamp.level.generator;

import net.redstonelamp.level.Chunk;
import net.redstonelamp.level.ChunkPosition;

import java.nio.ByteBuffer;

/**
 * This Simple FlatGenerator generates an infinite world of grass.
 *
 * @author RedstoneLamp Team
 */
public class FlatGenerator implements Generator{

    @Override
    public Chunk generateChunk(ChunkPosition position) {
        Chunk c = new Chunk(position);

        ByteBuffer bb = ByteBuffer.allocate(16 * 16 * 128);
        for(int blockX = 0; blockX < 16; blockX++){
            for(int blockZ = 0; blockZ < 16; blockZ++){
                //bb.put((byte) ItemValues.GRASS);
                bb.put((byte) 0x02); //GRASS Block
                for(int blockY = 0; blockY < 127; blockY++){
                    //bb.put((byte) ItemValues.AIR);
                    bb.put((byte) 0x00); //AIR
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