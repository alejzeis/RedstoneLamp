package redstonelamp.entity;

import redstonelamp.Player;
import redstonelamp.level.Location;
import redstonelamp.network.NetworkChannel;
import redstonelamp.network.packet.SetEntityDataPacket;

import java.util.*;

/**
 * Base class for entities.
 */
public abstract class Entity {
    public final static byte DATA_FLAGS = 0;
    public final static byte DATA_AIR = 1;
    public final static byte DATA_NAMETAG = 2;
    public final static byte DATA_SHOW_NAMETAG = 3;
    public final static byte DATA_SILENT = 4;
    public final static byte DATA_POTION_COLOR = 7;
    public final static byte DATA_POTION_AMBIENT = 8;
    public final static byte DATA_NO_AI = 15;
    
    
    private Location location;
    private long entityID;

    private List<Player> hasSpawned = new ArrayList<>();
    private Map<Integer, EntityMetadata> dataProperties = new HashMap<>();

    public Location getLocation(){
        return location;
    }

    protected void setLocation(Location location){
        this.location = location;
    }

    public List<Player> getViewers(){
        return hasSpawned;
    }

    public void spawnTo(Player player){
        hasSpawned.add(player);
    }

    public void despawnFrom(Player player){
        //TODO
    }

    public void setNameTag(String tag){
        setDataProperty(DATA_NAMETAG, EntityMetadata.DataType.DATA_TYPE_STRING, tag);
    }

    public void setDataProperty(int id, EntityMetadata.DataType type, Object value){
        if(!dataProperties.containsKey(id)){
            dataProperties.put(id, new EntityMetadata().add((byte) id, type).add((byte) id, value));

            sendData(this, hasSpawned, id, dataProperties.get(id));
        }
    }

    public void sendData(Entity entity, List<Player> players, int id, EntityMetadata metadata) {
        SetEntityDataPacket sedp = new SetEntityDataPacket();
        sedp.eid = entity instanceof Player ? 0 : entity.getId();
        sedp.metadata = metadata;
        for(Player player : players){
            sedp.setChannel(NetworkChannel.CHANNEL_WORLD_EVENTS);
            player.sendDataPacket(sedp);
        }
    }

    public long getId() {
        return entityID;
    }
}
