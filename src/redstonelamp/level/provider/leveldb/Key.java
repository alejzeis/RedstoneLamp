package redstonelamp.level.provider.leveldb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Represents a MCPE LevelDB Key.
 *
 * @author jython234
 */
public class Key {

    public static byte[] getKey(int chunkX, int chunkZ, KeyType type){
        ByteBuffer bb = ByteBuffer.allocate(9);
        bb.order(ByteOrder.LITTLE_ENDIAN); //Key format is Little-Endian
        bb.putInt(chunkX);
        bb.putInt(chunkZ);
        bb.put(type.asByte());
        return bb.array();
    }
}
