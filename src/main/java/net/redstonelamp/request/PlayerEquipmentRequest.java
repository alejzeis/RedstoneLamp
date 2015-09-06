package net.redstonelamp.request;

import net.redstonelamp.item.Item;

/**
 * Created by gunnar on 03.09.15.
 */
public class PlayerEquipmentRequest extends Request {
    public Item item;

    public PlayerEquipmentRequest(Item item) {
        this.item = item;
    }

    @Override
    public void execute() {

    }
}
