package redstonelamp.network.packet;

import redstonelamp.entity.EntityMetadata;
import redstonelamp.network.NetworkInfo;
import redstonelamp.utils.Binary;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * SetEntityDataPacket (0x93)
 */
public class SetEntityDataPacket extends DataPacket{
    public final static byte ID = NetworkInfo.SET_ENTITY_DATA_PACKET;

    public long eid;
    public EntityMetadata metadata;

    @Override
    public byte getPID() {
        return NetworkInfo.SET_ENTITY_DATA_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putLong(eid);
        bb.put(Binary.getDefaultInstance().writeMetadata(metadata));
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
