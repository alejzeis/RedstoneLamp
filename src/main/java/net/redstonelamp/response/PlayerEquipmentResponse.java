package net.redstonelamp.response;

import net.redstonelamp.item.Item;

/**
 * Response to a PlayerEquipmentRequest.
 *
 * @author RedstoneLamp Team
 */
public class PlayerEquipmentResponse extends Response {
    public Item item;

    public PlayerEquipmentResponse(Item item) {
        this.item = item;
    }
}
