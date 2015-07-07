package redstonelamp.network.packet;

import redstonelamp.utils.DynamicByteBuffer;

/**
 * Base class for all packets.
 */
public abstract class DataPacket {

    /**
     * Get's this packet's Packet ID.
     * @return The Packet ID (byte).
     */
    public abstract byte getPID();

    /**
     * Encode this packet into  bytes.
     * @return The packet's bytes.
     */
    public final byte[] encode(){
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance();
        bb.putByte(getPID());
        _encode(bb);
        return bb.toArray();
    }

    /**
     * Decodes the raw bytes of this packet into the child class.
     * @param buffer The packet's raw bytes.
     */
    public final void decode(byte[] buffer){
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(buffer);
        bb.getByte(); //PID
        _decode(bb);
        bb = null;
    }

    protected abstract void _encode(DynamicByteBuffer bb);
    protected abstract void _decode(DynamicByteBuffer bb);
}
