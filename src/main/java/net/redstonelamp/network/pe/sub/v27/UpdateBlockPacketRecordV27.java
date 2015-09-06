package net.redstonelamp.network.pe.sub.v27;

/**
 * Represents a record in UpdateBlockPacket (protocol 27)
 *
 * @author RedstoneLamp Team
 */
public class UpdateBlockPacketRecordV27 {
    public final int x;
    public final int y;
    public final int z;
    public final byte id;
    public final byte meta;
    public final byte flags;

    public UpdateBlockPacketRecordV27(int x, int y, int z, byte id, byte meta, byte flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
        this.meta = meta;
        this.flags = flags;
    }
}
