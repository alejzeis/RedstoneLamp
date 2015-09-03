package net.redstonelamp.inventory;

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
     * @param bytes The ByteArray representation of this Inventory
     *              to be loaded from.
     */
    void loadFromBytes(byte[] bytes);
}
