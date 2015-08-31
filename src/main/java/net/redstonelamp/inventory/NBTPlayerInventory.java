package net.redstonelamp.inventory;

import net.redstonelamp.Player;
import net.redstonelamp.item.Item;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Player Inventory that can be stored and loaded using NBT Compound Tags
 *
 * @author RedstoneLamp Team
 */
public class NBTPlayerInventory implements PlayerInventory{
    private Map<Integer, Stack<Item>> inventory = new ConcurrentHashMap<>();
    private Item inHand;
    private Player player;
    private int maxStackSize = MAX_STACK;

    @Override
    public Item getItemInHand() {
        return inHand;
    }

    @Override
    public void setItemInHand(Item item) {
        inHand = item;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getSize() {
        return 0;
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
        return inventory.get(index);
    }

    @Override
    public Item popItemFromStack(int index) {
        Stack<Item> stack = inventory.get(index);
        if(stack != null) {
            return stack.pop();
        }
        return null;
    }

    @Override
    public Item getItemFromStack(int index) {
        Stack<Item> stack = inventory.get(index);
        if(stack != null) {
            return stack.get(0);
        }
        return null;
    }

    @Override
    public void addItemToStack(Item item, int index) {
        Stack<Item> stack = inventory.get(index);
        if(stack != null) {
            stack.push(item);
        } else {
            stack = new Stack<>();
            stack.push(item);
            setStackAt(stack, index);
        }
    }

    @Override
    public void setStackAt(Stack<Item> stack, int index) {
        inventory.put(index, stack);
    }
}
