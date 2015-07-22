package redstonelamp.utils;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utility functions for compression/decompression.
 */
public class CompressionUtils {

    public static byte[] zlibDeflate(byte[] uncompressed, int level){
        Deflater deflater = new Deflater();
        deflater.setLevel(level);
        deflater.setInput(uncompressed);
        deflater.finish();

        byte[] compressed = new byte[uncompressed.length];
        int len = deflater.deflate(compressed);

        return Arrays.copyOf(compressed, len);
    }

    public static byte[] zlibInflate(byte[] compressed) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressed);
        byte[] uncompressed = new byte[64 * 64 * 64];
        int len = inflater.inflate(uncompressed);
        inflater.end();

        return Arrays.copyOf(uncompressed, len);
    }

    public static byte[] zlibInflate(byte[] compressed, int size) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressed, 0, size);
        byte[] uncompressed = new byte[64 * 64 * 64];
        int len = inflater.inflate(uncompressed);
        inflater.end();

        return Arrays.copyOf(uncompressed, len);
    }
}
