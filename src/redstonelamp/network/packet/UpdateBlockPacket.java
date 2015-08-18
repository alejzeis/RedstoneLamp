package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

import java.util.List;

/**
 * UpdateBlockPacket (0x91)
 *
 * @author jython234
 */
public class UpdateBlockPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.UPDATE_BLOCK_PACKET;

    public final static byte FLAG_NONE = 0b00000;
    public final static byte FLAG_NEIGHBORS = 0b00001;
    public final static byte FLAG_NETWORK = 0b0010;
    public final static byte FLAG_NOGRAPHIC = 0b0100;
    public final static byte FLAG_PRIORITY = 0b1000;

    public final static byte FLAG_ALL = FLAG_NEIGHBORS | FLAG_NETWORK;
    public final static byte FLAG_ALL_PRIORITY = FLAG_ALL | FLAG_PRIORITY;

    public List<Record> records;

    @Override
    public byte getPID() {
        return PENetworkInfo.UPDATE_BLOCK_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(records.size());
        for(Record r : records){
            bb.putInt(r.x);
            bb.putInt(r.z);
            bb.putByte(r.y);
            bb.putByte(r.blockId);
            bb.putByte((byte) (r.flags << 4 | r.blockData));
        }
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }

    public static class Record {
        public int x;
        public int z;
        public byte y;
        public byte blockId;
        public byte blockData;
        public byte flags;
    }
}
