package redstonelamp.network.pc.packet;

import redstonelamp.network.packet.DataPacket;
import redstonelamp.utils.DynamicByteBuffer;

import java.nio.ByteOrder;

/**
 * A PC DataPacket
 */
public abstract class PCDataPacket extends DataPacket{

    @Override
    public final byte getPID() {
        return (byte) getID();
    }

    public abstract int getID();

    @Override
    public byte[] encode() {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(ByteOrder.BIG_ENDIAN); //All MCPC data is BIG_ENDIAN
        bb.putVarInt(getPID());
        _encode(bb);
        return bb.toArray();
    }

    @Override
    public void decode(byte[] buffer) {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(buffer, ByteOrder.BIG_ENDIAN);
        bb.getVarInt();
        _decode(bb);
    }
}
