package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * PlayerArmorEquipmentPacket (0x99)
 *
 * @author jython234
 */
public class PlayerArmorEquipmentPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.PLAYER_ARMOR_EQUIPMENT_PACKET;

    public long eid;
    /**
     * 4 slots, (helmet, chest, legs, boots)
     */
    public byte[] slots;

    @Override
    public byte getPID() {
        return PENetworkInfo.PLAYER_ARMOR_EQUIPMENT_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putLong(eid);
        bb.putByte(slots[0]);
        bb.putByte(slots[1]);
        bb.putByte(slots[2]);
        bb.putByte(slots[3]);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        eid = bb.getLong();
        slots = new byte[4];
        slots[0] = bb.getByte();
        slots[1] = bb.getByte();
        slots[2] = bb.getByte();
        slots[3] = bb.getByte();
    }
}
