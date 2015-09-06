package net.redstonelamp.io;

import net.redstonelamp.nio.BinaryBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;

/**
 * Created by jython234 on 9/5/2015.
 *
 * @author RedstoneLamp Team
 */
public class BinaryBufferInputStream extends InputStream {
    private BinaryBuffer bb;

    public BinaryBufferInputStream(BinaryBuffer bb) {
        this.bb = bb;
    }

    @Override
    public int read() throws IOException {
        try {
            int i = bb.getUnsignedByte();
            return i;
        } catch(BufferUnderflowException e) {
            return -1;
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        bb = null;
    }
}
