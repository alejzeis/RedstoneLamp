package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * SetHealthPacket (0xa1)
 */
public class SetHealthPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.SET_HEALTH_PACKET;

    public int health;

    @Override
    public byte getPID() {
        return PENetworkInfo.SET_HEALTH_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(health);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        health = bb.getInt();
    }
}
