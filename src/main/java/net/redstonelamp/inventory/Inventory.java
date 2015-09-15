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

import net.redstonelamp.item.Item;

import java.util.Stack;

/**
 * Represents an Inventory
 *
 * @author RedstoneLamp Team
 */
public interface Inventory{
    /**
     * The maximum amount of items/blocks in a stack.
     */
    public final static int MAX_STACK = 64;

    /**
     * Get the size of the inventory.
     *
     * @return The amount of items in this inventory.
     */
    int getSize();

    /**
     * Returns the maximum amount of items/blocks in a stack for this Inventory.
     *
     * @return The Max Stack Size for this Inventory.
     */
    default int getMaxStackSize(){
        return MAX_STACK;
    }

    /**
     * Set the max stack size for this inventory.
     *
     * @param size The size.
     */
    void setMaxStackSize(int size);

    /**
     * Get the Stack stored at the specified index. This does NOT remove the stack from the inventory.
     *
     * @param index The index where the stack is.
     * @return The stack if found, null if not.
     */
    Stack<Item> getStackAt(int index);

    /**
     * "pops" an Item from the stack. This removes the Item from the stack, and returns it. The Item will be removed from the top of the Stack.
     *
     * @param index The index where the stack is.
     * @return The Item if the stack contains Items, null if not.
     */
    Item popItemFromStack(int index);

    /**
     * Same as <code>Inventory::popItemFromStack(int index)</code> except the item is not removed from the Stack.
     *
     * @param index The index where the stack is.
     * @return
     */
    Item getItemFromStack(int index);

    /**
     * Adds an Item to the stack at the specified index. If the index is empty, a new stack will be created to fill that index, and the item will be added to that.
     *
     * @param item  The Item to be added.
     * @param index The index where the Item should be added.
     */
    void addItemToStack(Item item, int index);

    /**
     * Sets the Stack at the specified index to the Stack given.
     *
     * @param stack The stack to be set.
     * @param index The index where the stack will be set.
     */
    void setStackAt(Stack<Item> stack, int index);

    /**
     * Gets the Item in the Entity's hand.
     *
     * @return The Item in the hand.
     */
    Item getItemInHand();

    /**
     * Sets the Item in the Entity's hand.
     *
     * @param item The Item to be set in the entity's hand.
     */
    void setItemInHand(Item item);
}
