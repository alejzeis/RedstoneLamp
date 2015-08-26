import net.redstonelamp.nio.BinaryBuffer;

import java.nio.ByteOrder;

/**
 * Created by jython234 on 8/26/2015.
 *
 * @author RedstoneLamp Team
 */
public class BinaryBufferTest {

    public static void main(String[] args) {
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.BIG_ENDIAN);
        bb.putByte((byte) 1);
        System.out.println(bb.singleLineHexDump());
    }

}
