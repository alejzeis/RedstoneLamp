package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * SetSpawnPositionPacket (0xa2)
 */
public class SetSpawnPositionPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.SET_SPAWN_POSITION_PACKET;

    public int x;
    public int z;
    public byte y;

    @Override
    public byte getPID() {
        return PENetworkInfo.SET_SPAWN_POSITION_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(x);
        bb.putInt(z);
        bb.putByte(y);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
