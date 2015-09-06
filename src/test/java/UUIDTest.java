import net.redstonelamp.nio.BinaryBuffer;

import java.nio.ByteOrder;
import java.util.UUID;

/**
 * Created by jython234 on 9/5/2015.
 *
 * @author RedstoneLamp Team
 */
public class UUIDTest {

    public static void main(String[] args) {
        UUID id = UUID.nameUUIDFromBytes("jython234".getBytes());
        BinaryBuffer bb = BinaryBuffer.newInstance(16, ByteOrder.LITTLE_ENDIAN);
        bb.putUUID(id);
        byte[] data = bb.toArray();
        BinaryBuffer b2 = BinaryBuffer.wrapBytes(data, ByteOrder.LITTLE_ENDIAN);
        UUID id2 = b2.getUUID();
        System.out.println(id2.toString()+", "+id.toString());
        System.out.println(id2.toString().equals(id.toString()));
    }
}
