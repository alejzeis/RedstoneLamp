package redstonelamp.network.packet;

import redstonelamp.item.Item;
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
    public List<Item> slots = new ArrayList<>();
    public List<Integer> hotbar = new ArrayList<>();

    @Override
    public byte getPID() {
        return NetworkInfo.CONTAINTER_SET_CONTENT_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putByte(windowId);
        bb.putShort((short) slots.size());
        for(Item item : slots){
            putSlot(bb, item);
        }
        if(windowId == SPECIAL_INVENTORY && hotbar.size() > 0){
            bb.putShort((short) hotbar.size());
            for(Integer i : hotbar){
                bb.putInt(i);
            }
        } else {
            bb.putShort((short) 0);
        }
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        clean();
        windowId = bb.getByte();
        int count = bb.getShort();
        for(int s = 0; s < count; s++){
            slots.add(getSlot(bb));
        }
        if(windowId == SPECIAL_INVENTORY){
            count = bb.getShort();
            for(int s = 0; s < count; s++){
                hotbar.add(bb.getInt());
            }
        }
    }

    public void clean(){
        windowId = -1;
        slots.clear();
        hotbar.clear();
    }
}
