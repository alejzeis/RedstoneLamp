package net.redstonelamp.response;

/**
 * Created by gunnar on 03.09.15.
 */
public class PlayerEquipmentResponse extends Response {
    private final long entityID;
    private final short item;
    private final short meta;

    public PlayerEquipmentResponse(long entityID, short item, short meta) {
        this.entityID = entityID;
        this.item = item;
        this.meta = meta;
    }

    public long getEntityID() {
        return entityID;
    }

    public short getItem() {
        return item;
    }

    public short getMeta() {
        return meta;
    }
}
