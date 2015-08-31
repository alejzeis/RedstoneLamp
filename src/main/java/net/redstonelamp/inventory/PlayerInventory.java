package net.redstonelamp.inventory;

import net.redstonelamp.Player;
import net.redstonelamp.item.Item;

/**
 * Created by jython234 on 8/31/2015.
 *
 * @author RedstoneLamp Team
 */
public interface PlayerInventory extends Inventory{
    /**
     * Gets the Item in the Player's hand.
     * @return The Item in the hand.
     */
    Item getItemInHand();

    /**
     * Sets the Item in the player's hand.
     * @param item The Item to be set in the player's hand.
     */
    void setItemInHand(Item item);

    /**
     * Returns the Player this inventory belongs to.
     * @return The Player the inventory belongs to.
     */
    Player getPlayer();
}
