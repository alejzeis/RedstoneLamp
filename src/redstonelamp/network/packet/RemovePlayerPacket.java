package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * RemovePlayerPacket (0x89)
 *
 * @author jython234
 */
public class RemovePlayerPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.REMOVE_PLAYER_PACKET;

    public long eid;
    public long clientID;

    @Override
    public byte getPID() {
        return PENetworkInfo.REMOVE_PLAYER_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putLong(eid);
        bb.putLong(clientID);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
