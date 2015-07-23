package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * DisconnectPacket (0x84)
 */
public class DisconnectPacket extends DataPacket{
    public static byte ID = PENetworkInfo.DISCONNECT_PACKET;

    public String message;

    @Override
    public byte getPID() {
        return PENetworkInfo.DISCONNECT_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putString(message);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        message = bb.getString();
    }
}
