package redstonelamp.level.provider;

import redstonelamp.item.ItemValues;
import redstonelamp.level.LevelProvider;
import redstonelamp.utils.DynamicByteBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A simple implementation of <code>LevelProvider</code> that does not save or store the level.
 */
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
    public void shutdown() {

    }

    @Override
    public void loadLevelData(File file) throws IOException {

    }
}
