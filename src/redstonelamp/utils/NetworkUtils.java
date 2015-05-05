package redstonelamp.utils;

import java.nio.ByteBuffer;

/**
 * Network Utility class for triads, etc.
 */
public class NetworkUtils {

    public static int readLTriad(ByteBuffer bb) {
        return (int) (bb.get() << 16 | bb.get() << 8 | bb.get());
    }

    public static void writeLTriad(int triad, ByteBuffer bb){
        byte[] buffer = new byte[3];
        int shift = (3 - 1) * 8;
        for(int i = 0; i < 3; i++){
            buffer[true ? (3 - i - 1):i] = (byte) (triad >> shift);
            shift -= 8;
        }
        bb.put(buffer);
    }

}
