package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

import java.util.List;

/**
 * SetEntityMotionPacket (0x9f)
 */
public class SetEntityMotionPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.SET_ENTITY_MOTION_PACKET;

    public List<entitymotionpacket_EntityData> entities;

    @Override
    public byte getPID() {
        return PENetworkInfo.SET_ENTITY_MOTION_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(entities.size());
        for(entitymotionpacket_EntityData data : entities){
            bb.putLong(data.eid);
            bb.putFloat(data.motionX);
            bb.putFloat(data.motionY);
            bb.putFloat(data.motionZ);
        }
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }

    public static class entitymotionpacket_EntityData{
        public long eid;
        public float motionX;
        public float motionY;
        public float motionZ;
    }
}
