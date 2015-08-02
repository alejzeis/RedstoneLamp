package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * RespawnPacket (0xa4)
 */
public class RespawnPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.RESPAWN_PACKET;

    public float x;
    public float y;
    public float z;

    @Override
    public byte getPID() {
        return PENetworkInfo.RESPAWN_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putFloat(x);
        bb.putFloat(y);
        bb.putFloat(z);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        x = bb.getFloat();
        y = bb.getFloat();
        z = bb.getFloat();
    }
}
