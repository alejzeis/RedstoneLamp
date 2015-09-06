package net.redstonelamp.request;

/**
 * Created by gunnar on 03.09.15.
 */
public class PlayerEquipmentRequest extends Request {
    private final short item;
    private final short meta;

    public PlayerEquipmentRequest(short item, short meta) {
        this.item = item;
        this.meta = meta;
    }

    public short getItem() {
        return item;
    }

    public short getMeta() {
        return meta;
    }

    @Override
    public void execute() {

    }
}
