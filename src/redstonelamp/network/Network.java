package redstonelamp.network;

import redstonelamp.Player;
import redstonelamp.Server;
import redstonelamp.network.packet.*;
import redstonelamp.utils.CompressionUtils;
import redstonelamp.utils.DynamicByteBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Networking class.
 */
public class Network {
    private Map<Byte, Class<? extends DataPacket>> packets = new ConcurrentHashMap<>();
    private Server server;
    private List<NetworkInterface> interfaces = new ArrayList<>();

    public Network(Server server){
        this.server = server;
        registerPackets();
    }

    public void tick(){
        for(NetworkInterface networkInterface : interfaces){
            networkInterface.processData();
        }
    }

    public void processBatch(BatchPacket bp, Player player) {
        try{
            bp.payload = CompressionUtils.zlibInflate(bp.payload);
            int len = bp.payload.length;
            int offset = 0;
            while(offset < len){
                DataPacket pk = getPacket(bp.payload[offset++]);
                if(pk == null){
                    pk = new UnknownDataPacket();
                }
                pk.decode(bp.payload, offset);
                player.handleDataPacket(pk);
                offset =+ (pk.getOffset() - offset);
                if(offset >= bp.payload.length || offset < 0){
                    return;
                }
            }
        } catch(Exception e){
            server.getLogger().error("Exception: "+e.getMessage());
            if(server.isDebugMode()){
                server.getLogger().debug("Exception while handling BatchPacket 0x" + String.format("%02X ", bp.payload[0]));
                e.printStackTrace();
            }
        }
    }

    public void sendBatches(Player[] players, DataPacket[] packets, NetworkChannel channel){
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance();
        for(DataPacket packet : packets){
            bb.put(packet.encode());
        }

        BatchPacket bp = new BatchPacket();
        bp.setChannel(channel);
        bp.payload = CompressionUtils.zlibDeflate(bb.toArray(), NetworkInfo.COMPRESSION_LEVEL);

        for(Player player : players){
            if(server.getPlayer(player.getIdentifier()) != null){
                player.sendDataPacket(bp);
            }
        }
    }

    public void registerInterface(NetworkInterface interface_){
        interfaces.add(interface_);
    }

    public void removeInterface(NetworkInterface interface_){
        interfaces.remove(interface_);
    }

    public NetworkInterface getInterface(Class<? extends NetworkInterface> clazz){
        for(NetworkInterface networkInterface : interfaces){
            if(networkInterface.getClass().getName().equals(clazz.getName())){
                return networkInterface;
            }
        }
        return null;
    }

    public DataPacket getPacket(byte ID){
        if(packets.containsKey(ID)) {
            Class<? extends DataPacket> clazz = packets.get(ID);
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void registerPackets(){
        packets.put(BatchPacket.ID, BatchPacket.class);
        packets.put(LoginPacket.ID, LoginPacket.class);
        packets.put(DisconnectPacket.ID, DisconnectPacket.class);
    }
}
