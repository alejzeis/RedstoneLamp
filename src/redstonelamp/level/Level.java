package redstonelamp.level;

import redstonelamp.Player;
import redstonelamp.PocketPlayer;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.level.generator.FlatGenerator;
import redstonelamp.level.location.ChunkLocation;
import redstonelamp.level.location.Location;
import redstonelamp.level.provider.leveldb.LevelDBProvider;
import redstonelamp.network.packet.FullChunkDataPacket;
import redstonelamp.network.packet.MovePlayerPacket;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Base level class.
 */
public class Level {
    public static int CHUNKS_PER_TICK = 2;

    private String name;

    private Server server;
    private LevelProvider provider;
    private Map<Player, List<ChunkLocation>> chunksToSend = new ConcurrentHashMap<>();

    private int gamemode;
    private long time;
    private Location spawnLocation;

    public Level(Server server){
        this.server = server;
        this.name = getDefaultWorldDataFolder().getName();
        try {
            provider = new LevelDBProvider(this, new FlatGenerator(), new File(getDefaultWorldDataFolder()+File.separator+"db"));
            provider.loadLevelData(new File(getDefaultWorldDataFolder() + File.separator + "level.dat"));
        } catch (IOException e) {
            server.getLogger().error("FAILED TO LOAD LEVEL DATA! " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void tick(){
        if(chunksToSend.keySet().isEmpty()){
            return;
        }
        int sent = 0;
        for(Player player : chunksToSend.keySet()){
            if(sent >= CHUNKS_PER_TICK) break;

            List<ChunkLocation> chunks = chunksToSend.get(player);
            for(ChunkLocation location : chunks){
                if(sent >= CHUNKS_PER_TICK) break;

                byte[] data = provider.orderChunk(location.getX() / 16, location.getZ() / 16);
                FullChunkDataPacket dp = new FullChunkDataPacket();
                dp.x = location.getX();
                dp.z = location.getZ();
                dp.payload = data;
                player.sendDataPacket(dp);
                chunks.remove(location);
                sent++;
            }
            if(!chunks.isEmpty()){
                chunksToSend.put(player, chunks);
            } else {
                ((PocketPlayer) player).doFirstSpawn(); //TODO
                chunksToSend.remove(player);
            }
        }
    }

    public void queueLoginChunks(Player player){
        if(chunksToSend.containsKey(player)){
            throw new IllegalArgumentException("Chunks already queued.");
        }

        List<ChunkLocation> chunks = new CopyOnWriteArrayList<>();
        int centerX = (int) player.getLocation().getX();
        int centerZ = (int) player.getLocation().getZ();

        int cornerX = centerX - 64;
        int cornerZ = centerZ + 64;

        int x = cornerX;
        int z = cornerZ;

        int chunkNum = 0;
        try{
            while(chunkNum < 96){
                //System.out.println("ChunkSender chunk "+x+", "+z);

                chunks.add(new ChunkLocation(x, z));

                if(x < cornerX + 144){
                    x = x + 16;
                } else {
                    x = cornerX;
                    z = z - 16;
                }
                chunkNum++;
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        chunksToSend.put(player, chunks);
    }

    public synchronized void clearQueue(Player player){
        chunksToSend.remove(player);
    }

    public void shutdown() {

    }
    
    /**
     * Returns the "worlds" directory
     * 
     * @return File
     */
    public File getDataFolder() {
    	File dataFolder = new File("./worlds/");
    	if(!dataFolder.isDirectory())
    		dataFolder.mkdirs();
    	return dataFolder;
    }
    
    public File getDefaultWorldDataFolder() {
       	File dataFolder = new File(getDataFolder() + "/" + RedstoneLamp.properties.getProperty("level-name", "world"));
       	if(!dataFolder.isDirectory())
       		dataFolder.mkdirs();
       	return dataFolder;
    }

    public LevelProvider getProvider() {
        return provider;
    }

    public void spawnToAll(Player p){
        for(Player player : server.getOnlinePlayers()){
            if(player != p && player.getLocation().getLevel() == this) {
                p.spawnTo(player);
                player.spawnTo(p);
            }
        }
    }

    public void despawnFromAll(Player p){
        for(Player player : server.getOnlinePlayers()){
            if(player != this){
                if(player instanceof PocketPlayer) {
                    p.despawnFrom(player);
                    player.despawnFrom(p);
                }
            }
        }
    }

    public void broadcastMovement(Player player, MovePlayerPacket cMpp) {
        Location l = player.getLocation();
        if(l.getLevel() != this){
            throw new IllegalArgumentException("Player is on a different Level!");
        }
        MovePlayerPacket mpp = new MovePlayerPacket(); //We assume that the movement checking was done in the Player implementation
        mpp.eid = player.getId();
        mpp.x = (float) l.getX();
        mpp.y = (float) l.getY();
        mpp.z = (float) l.getZ();
        mpp.yaw = l.getYaw();
        mpp.bodyYaw = cMpp.bodyYaw;
        mpp.pitch = l.getPitch();
        mpp.mode = cMpp.mode;
        mpp.onGround = cMpp.onGround;

        server.getOnlinePlayers().stream().filter(p -> p.getLocation().getLevel() == this && p != player).forEach(p -> p.sendDataPacket(mpp));
    }

    public void setSpawnLocation(Location location){
        spawnLocation = location;
    }

    public void setGamemode(int gamemode){
        this.gamemode = gamemode;
    }

    public String getName() {
        return name;
    }

    public int getGamemode() {
        return gamemode;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Server getServer() {
        return server;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
