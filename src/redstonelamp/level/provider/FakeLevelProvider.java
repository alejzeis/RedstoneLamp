package redstonelamp.level.provider;

import redstonelamp.item.ItemValues;
import redstonelamp.level.Chunk;
import redstonelamp.level.LevelProvider;
import redstonelamp.level.location.ChunkLocation;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A simple implementation of <code>LevelProvider</code> that does not save or store the level.
 */
@Deprecated
public class FakeLevelProvider implements LevelProvider{
    @Override
    public byte[] orderChunk(int x, int z) {
        ByteBuffer bb = ByteBuffer.allocate(83200);
        for(int blockX = 0; blockX < 16; blockX++){
            for(int blockZ = 0; blockZ < 16; blockZ++){
                bb.put((byte) ItemValues.GRASS);
                for(int blockY = 0; blockY < 127; blockY++){
                    bb.put((byte) ItemValues.AIR);
                }
            }
        }
        bb.put(new byte[16384]);
        for(int i = 0; i < 16384; i++){  //Skylight
            bb.put((byte) 0xFF);
        }
        for(int i = 0; i < 16384; i++){ //BlockLight
            bb.put((byte) 0);
        }
        for(int i = 0; i < 256; i++){ //Heightmap
            bb.put((byte) 0xFF);
        }
        for(int i = 0; i < 256; i++){ //Biome Colors
            bb.put((byte) 0x01);
            bb.put((byte) 0x85);
            bb.put((byte) 0xB2);
            bb.put((byte) 0x4A);
        }
        return bb.array();
    }

    @Override
    public Chunk getChunk(ChunkLocation chunkLocation) {
        Chunk c = new Chunk();

        ByteBuffer bb = ByteBuffer.allocate(16 * 16 * 128);
        for(int blockX = 0; blockX < 16; blockX++){
            for(int blockZ = 0; blockZ < 16; blockZ++){
                bb.put((byte) ItemValues.GRASS);
                for(int blockY = 0; blockY < 127; blockY++){
                    bb.put((byte) ItemValues.AIR);
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

    @Override
    public void shutdown() {

    }

    @Override
    public void loadLevelData(File file) throws IOException {

    }
}
