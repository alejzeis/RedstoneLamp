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

/**
 * Represents an Inventory used by a Player that can be saved.
 *
 * @author RedstoneLamp Team
 */
public interface PlayerInventory extends Inventory, SavableInventory{

    /**
     * Returns the Player this inventory belongs to.
     *
     * @return The Player the inventory belongs to.
     */
    Player getPlayer();

    /**
     * Get the currently selected slot. This includes the inventory slots, not just the hotbar.
     * @return The currently selected slot.
     */
    int getSelectedSlot();

    /**
     * Set the currently selected slot. This includes the inventory slots, not just the hotbar.
     * @param slot The currently selected slot.
     */
    void setSelectedSlot(int slot);

    /**
     * Get the currently selected hotbar slot.
     * @return The currently selected hotbar slot.
     */
    int getItemInHandSlot();

    /**
     * Set the currently selected hotbar slot.
     * @param slot The currently selected hotbar slot.
     */
    void setItemInHandSlot(int slot);
}
