package redstonelamp.inventory;

import redstonelamp.Player;
import redstonelamp.item.Item;

/**
 * Represents a Player's inventory.
 *
 * @author jython234
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
