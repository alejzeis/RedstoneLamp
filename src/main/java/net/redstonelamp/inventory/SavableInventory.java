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

import net.redstonelamp.nio.BinaryBuffer;

/**
 * An Inventory that can be saved and loaded from byte arrays.
 *
 * @author RedstoneLamp Team
 */
public interface SavableInventory extends Inventory{
    /**
     * Saves this inventory into a ByteArray.
     * @return The ByteArray representation of this Inventory.
     */
    byte[] saveToBytes();

    /**
     * Loads this inventory from a ByteArray.
     * @param bytes The ByteArray representation of this Inventory that will be loaded
     *              from.
     */
    void loadFromBytes(byte[] bytes);
}
