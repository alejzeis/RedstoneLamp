package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * UseItemPacket (0x9B)
 *
 * @author jython234
 */
public class UseItemPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.USE_ITEM_PACKET;

    public int x;
    public int y;
    public int z;
    public byte face;
    public short item;
    public short meta;
    public long eid;
    public float fx;
    public float fy;
    public float fz;
    public float posX;
    public float posY;
    public float posZ;

    @Override
    public byte getPID() {
        return PENetworkInfo.USE_ITEM_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {

    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        x = bb.getInt();
        y = bb.getInt();
        z = bb.getInt();
        face = bb.getByte();
        item = bb.getShort();
        meta = bb.getShort();
        eid = bb.getLong();
        fx = bb.getFloat();
        fy = bb.getFloat();
        fz = bb.getFloat();
        posX = bb.getFloat();
        posY = bb.getFloat();
        posZ = bb.getFloat();
    }
}
