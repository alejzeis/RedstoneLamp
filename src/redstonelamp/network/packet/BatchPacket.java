package redstonelamp.network.packet;


import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * MC_BATCH_PACKET (0xb1)
 */
public class BatchPacket extends DataPacket{
    public static byte ID = PENetworkInfo.BATCH_PACKET;

    public byte[] payload;

    @Override
    public byte getPID() {
        return ID;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(payload.length);
        bb.put(payload);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        int size = bb.getInt();
        payload = bb.get(size);
    }
}
