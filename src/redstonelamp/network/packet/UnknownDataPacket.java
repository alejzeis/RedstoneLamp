package redstonelamp.network.packet;

import redstonelamp.utils.DynamicByteBuffer;

/**
 * Represents an unimplemented/unknown data packet.
 */
public class UnknownDataPacket extends DataPacket{

    public byte[] unknownBuffer;

    @Override
    public byte getPID() {
        return -1;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.put(unknownBuffer);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        unknownBuffer = bb.get(bb.getByteBuffer().remaining());
    }
}
