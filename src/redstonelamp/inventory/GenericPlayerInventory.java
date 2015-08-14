package redstonelamp.inventory;

import redstonelamp.Player;
import redstonelamp.io.InvalidDataException;
import redstonelamp.item.Item;
import redstonelamp.item.ItemValues;
import redstonelamp.utils.DynamicByteBuffer;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Generic Implementation of a Player's Inventory.
 * This class is currently NOT Thread safe!
 *
 * @author jython234
 */
public class GenericPlayerInventory implements PlayerInventory{
    public final static byte STORAGE_ID = 0x01;
    public final static byte STORAGE_END = (byte) 0xFF;

    private int maxStackSize = Inventory.MAX_STACK;
    private Map<Integer, Stack<Item>> stacks = new HashMap<>();
    private Item itemInHand = Item.get(ItemValues.AIR, 0);

    private Player player;

    /**
     * Creates a new GenericPlayerInventory that belongs to the specified player.
     * @param player The Player this Inventory belongs to.
     */
    public GenericPlayerInventory(Player player){
        this.player = player;

    }

    @Override
    public Item getItemInHand() {
        return itemInHand;
    }

    @Override
    public void setItemInHand(Item item) {
        this.itemInHand = item;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getSize() {
        return stacks.size();
    }

    @Override
    public void setMaxStackSize(int size) {
        maxStackSize = size;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public Stack<Item> getStackAt(int index) {
        if(stacks.containsKey(index)){
            return stacks.get(index);
        }
        return null;
    }

    @Override
    public Item popItemFromStack(int index) {
        if(stacks.containsKey(index)){
            Stack<Item> stack = getStackAt(index);
            if(!stack.empty()){
                return stack.pop();
            }
        }
        return null;
    }

    @Override
    public Item getItemFromStack(int index) {
        if(stacks.containsKey(index)){
            Stack<Item> stack = getStackAt(index);
            if(!stack.empty()){
                return stack.peek();
            }
        }
        return null;
    }

    @Override
    public void addItemToStack(Item item, int index) {
        if(!stacks.containsKey(index)){
            Stack<Item> stack = new Stack<>();
            stacks.put(index, stack);
        }
        Stack<Item> stack = getStackAt(index);
        stack.push(item);
        stacks.put(index, stack);
    }

    @Override
    public void setStackAt(Stack<Item> stack, int index) {
        stacks.put(index, stack);
    }

    @Override
    public byte[] store() {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(ByteOrder.LITTLE_ENDIAN);
        bb.putByte(STORAGE_ID);
        bb.putInt(getItemInHand().getId());
        bb.putShort(getItemInHand().getMetadata());
        bb.putInt(getItemInHand().getCount());
        bb.putInt(stacks.keySet().size());
        for(int i = 0; i < stacks.keySet().size(); i++){
            Stack<Item> stack = getStackAt(i);
            bb.putInt(stack.size());
            for(int i2 = 0; i2 < stack.size(); i2++){
                Item item = stack.get(i2);
                bb.putInt(item.getId());
                bb.putShort(item.getMetadata());
            }
        }
        bb.putByte(STORAGE_END);
        return bb.toArray();
    }

    @Override
    public void load(byte[] source) {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(source, ByteOrder.LITTLE_ENDIAN);
        if(bb.getByte() != STORAGE_ID) {
            throw new InvalidDataException("STORAGE_ID does not match.");
        }
        int id = bb.getInt();
        short md = bb.getShort();
        int count = bb.getInt();
        setItemInHand(Item.get(id, md, count));

        int stacks = bb.getInt();
        for(int i = 0; i < stacks; i++){
            int stackSize = bb.getInt();
            Stack<Item> stack = new Stack<>();

            for(int i2 = 0; i2 < stackSize; i2++) {
                int itemId = bb.getInt();
                short itemMd = bb.getShort();
                stack.push(Item.get(itemId, itemMd));
            }
            setStackAt(stack, i);
        }
        if(bb.getByte() != STORAGE_END){
            throw new InvalidDataException("STORAGE_END does not match.");
        }
    }
}
