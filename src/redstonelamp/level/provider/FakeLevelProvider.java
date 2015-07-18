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
        for(int i = 0; i < 256; i++){
            bb.putByte((byte) 0xFF);
        }
        for(int i = 0; i < 256; i++){
            bb.putByte((byte) 0x01);
            bb.putByte((byte) 0x85);
            bb.putByte((byte) 0xB2);
            bb.putByte((byte) 0x4A);
        }
        return bb.toArray();
    }
}
