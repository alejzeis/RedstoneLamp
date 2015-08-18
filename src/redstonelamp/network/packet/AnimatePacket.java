package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * AnimatePacket (0xA3)
 *
 * @author jython234
 */
public class AnimatePacket extends DataPacket{
    public final static byte ID = PENetworkInfo.ANIMATE_PACKET;

    public byte action;
    public long eid;

    @Override
    public byte getPID() {
        return PENetworkInfo.ANIMATE_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putByte(action);
        bb.putLong(eid);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        action = bb.getByte();
        eid = bb.getLong();
    }
}
