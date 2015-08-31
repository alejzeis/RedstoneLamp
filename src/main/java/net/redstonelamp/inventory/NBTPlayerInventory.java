/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
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
