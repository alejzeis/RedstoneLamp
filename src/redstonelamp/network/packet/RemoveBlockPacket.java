package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * RemoveBlockPacket (0x90)
 *
 * @author jython234
 */
public class RemoveBlockPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.REMOVE_BLOCK_PACKET;

    public long eid;
    public int x;
    public int z;
    public byte y;

    @Override
    public byte getPID() {
        return PENetworkInfo.REMOVE_BLOCK_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {

    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        eid = bb.getLong();
        x = bb.getInt();
        z = bb.getInt();
        y = bb.getByte();
    }
}
