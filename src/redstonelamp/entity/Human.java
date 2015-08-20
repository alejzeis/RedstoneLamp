package redstonelamp.entity;

import redstonelamp.Player;
import redstonelamp.Server;
import redstonelamp.item.ItemValues;
import redstonelamp.network.packet.AddPlayerPacket;
import redstonelamp.network.packet.PlayerArmorEquipmentPacket;
import redstonelamp.network.packet.RemovePlayerPacket;
import redstonelamp.utils.Skin;

import java.util.Arrays;

/**
 * Abstract class for NPC/Player (Human)
 *
 * @author RedstoneLampProject
 */
public class Human extends Entity{

    protected Skin skin;
    protected String nametag;
    protected boolean isSlim;

    public Human(int id) {
        super(id);
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);
        if(player != this){

            AddPlayerPacket app = new AddPlayerPacket();
            app.clientID = getId();
            app.username = getName();
            app.eid = getId();
            app.x = (float) getLocation().getX();
            app.y = (float) getLocation().getY();
            app.z = (float) getLocation().getZ();
            app.speedX = 0f; //TODO
            app.speedY = 0f;
            app.speedZ = 0f;
            app.yaw = getLocation().getYaw();
            app.pitch = getLocation().getPitch();
            app.item = ItemValues.AIR; //TODO
            app.meta = 0;
            app.skin = skin;
            app.slim = isSlim;
            app.metadata = getFakeMetadata();
            player.sendDataPacket(app);

            PlayerArmorEquipmentPacket paep = new PlayerArmorEquipmentPacket(); //TODO: Real values
            paep.eid = getId();
            paep.slots = new byte[] {(byte) 255, (byte) 255, (byte) 255, (byte) 255};
            //player.sendDataPacket(paep);
        }
    }

    @Override
    public void despawnFrom(Player player) {
        if(player != this){
            super.despawnFrom(player);

            RemovePlayerPacket rpp = new RemovePlayerPacket();
            rpp.eid = getId();
            rpp.clientID = getId();
            player.sendDataPacket(rpp);
        }
    }

    public void spawnToAll(Server server){
        if(this instanceof Player){
            getLocation().getLevel().spawnToAll((Player) this);
        }
    }

    protected EntityMetadata getFakeMetadata(){
        EntityMetadata em = new EntityMetadata();
        em.set((byte) 0, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //Is player on fire
        em.set((byte) 1, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_SHORT, (short) 300)); //Strange Air thing
        em.set((byte) 2, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_STRING, getName())); //nametag
        em.set((byte) 3, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 1)); //Hide nametag?
        em.set((byte) 4, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //silent thing
        em.set((byte) 7, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_INT, 0)); //Potion color
        em.set((byte) 8, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //Potion ambient
        em.set((byte) 15, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 1)); //No ai
        em.set((byte) 16, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0)); //Player flags
        em.set((byte) 17, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_LONG, (long) 0));
        return em;
    }

    @Override
    public void setNameTag(String tag) {
        nametag = tag;
    }

    @Override
    public String getNameTag() {
        return nametag;
    }

    public String getName() {
        return getNameTag();
    }
}
