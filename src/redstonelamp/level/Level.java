package redstonelamp.level;

import com.sun.org.apache.xml.internal.security.Init;
import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.level.provider.FakeLevelProvider;
import redstonelamp.network.NetworkChannel;
import redstonelamp.network.packet.FullChunkDataPacket;
import redstonelamp.network.packet.PlayStatusPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Base level class.
 */
public class Level {
    public static int CHUNKS_PER_TICK = 2;

    private Server server;
    private LevelProvider provider;
    private Map<Player, List<ChunkLocation>> chunksToSend = new ConcurrentHashMap<>();

    public Level(Server server){
        this.server = server;
        provider = new FakeLevelProvider(); //TODO: Change.
    }

    public void tick(){
        if(chunksToSend.keySet().isEmpty()){
            return;
        }
        for(Player player : chunksToSend.keySet()) {
            List<ChunkLocation> chunks = chunksToSend.get(player);
            for (int i = 0; i < CHUNKS_PER_TICK; i++) {
                if (i >= chunks.size()) {
                    PlayStatusPacket psp = new PlayStatusPacket();
                    psp.status = PlayStatusPacket.Status.PLAYER_SPAWN;
                    //psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                    player.sendDataPacket(psp);
                    chunksToSend.remove(player);
                    return;
                }
                final int finalI = i;

                byte[] data = provider.orderChunk(chunks.get(finalI).getX(), chunks.get(finalI).getZ());
                FullChunkDataPacket dp = new FullChunkDataPacket();
                dp.x = chunks.get(finalI).getX();
                dp.z = chunks.get(finalI).getZ();
                dp.payload = data;
                //dp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                player.sendDataPacket(dp);
                chunks.remove(finalI);
            }
            chunksToSend.put(player, chunks);
        }
    }

    public void queueLoginChunks(Player player){
        if(chunksToSend.containsKey(player)){
            throw new IllegalArgumentException("Chunks already queued.");
        }

        List<ChunkLocation> chunks = new CopyOnWriteArrayList<>();
        int chunkX = (int) player.getLocation().getX();
        int chunkZ = (int) player.getLocation().getZ();
        for (int distance = 5; distance >= 0; distance--) {
            for (int x = chunkX - distance; x < chunkX + distance; x++) {
                for (int z = chunkZ - distance; z < chunkZ + distance; z++) {
                    if (Math.sqrt((chunkX - x) * (chunkX - x) + (chunkZ - z) * (chunkZ - z)) < 5) {
                        chunks.add(new ChunkLocation(x, z));
                    }
                }
            }
        }
       new Thread(new InitalChunkSender(chunks, player)).start();
    }

    public synchronized void clearQueue(Player player){
        chunksToSend.remove(player);
    }

    public void shutdown() {
        //sendPool.shutdown();
    }

    public class InitalChunkSender implements Runnable{
        private List<ChunkLocation> locations;
        private Player player;

        public InitalChunkSender(List<ChunkLocation> locations, Player player){
            this.locations = locations;
            this.player = player;
        }

        @Override
        public void run() {
            int sent = 0;
            for(ChunkLocation location : locations){
                sent = sent + 1;
                byte[] data = provider.orderChunk(location.getX(), location.getZ());
                FullChunkDataPacket dp = new FullChunkDataPacket();
                dp.x = location.getX();
                dp.z = location.getZ();
                dp.payload = data;
                dp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                player.sendDataPacket(dp);
                System.out.println("Sent chunk "+location.getX()+", "+location.getZ()+" sent: "+sent);
            }
            PlayStatusPacket psp = new PlayStatusPacket();
            psp.status = PlayStatusPacket.Status.PLAYER_SPAWN;
            player.sendDirectDataPacket(psp);
        }
    }
}
