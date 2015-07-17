package redstonelamp.level.provider;

import redstonelamp.level.LevelProvider;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * A simple implementation of <code>LevelProvider</code> that does not save or store the level.
 */
public class FakeLevelProvider implements LevelProvider{
    @Override
    public byte[] orderChunk(int x, int z) {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance();
        bb.put(new byte[16 * 16 * 128]); //Block Ids
        bb.put(new byte[(16 * 16 * 128) / 2]); //Half-Byte block metadata
        bb.put(new byte[(16 * 16 * 128) / 2]); //Half-Byte skylight
        bb.put(new byte[(16 * 16 * 128) / 2]); //Half-Byte blocklight
        bb.put(new byte[256]); //Heightmap
        bb.put(new byte[1024]); //BiomeColors
        return bb.toArray();
    }
}
