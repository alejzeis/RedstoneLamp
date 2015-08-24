/**
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
package net.redstonelamp.utils;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Utility methods for compression/decompression
 *
 * @author RedstoneLamp Team
 */
public class CompressionUtils {

    public static byte[] zlibDeflate(byte[] uncompressed, int level){
        Deflater deflater = new Deflater(level, false);
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
