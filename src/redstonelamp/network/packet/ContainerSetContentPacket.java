package redstonelamp.network.packet;

import redstonelamp.network.NetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * ContainerSetContentPacket (0xaa)
 */
public class ContainerSetContentPacket extends DataPacket{
    public final static byte ID = NetworkInfo.CONTAINTER_SET_CONTENT_PACKET;

    public final static byte SPECIAL_INVENTORY = 0x00;
    public final static byte SPECIAL_ARMOR = 0x78;
    public final static byte SPECIAL_CREATIVE = 0x79;
    public final static byte SPECIAL_CRAFTING = 0x7a;

    public byte windowId;
    //public List<Item> slots = new ArrayList<>();
    public List<Integer> hotbar = new ArrayList<>();

    @Override
    public byte getPID() {
        return NetworkInfo.CONTAINTER_SET_CONTENT_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {

    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
