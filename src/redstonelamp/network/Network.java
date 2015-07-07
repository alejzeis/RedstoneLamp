package redstonelamp.network;

import redstonelamp.Server;
import redstonelamp.network.packet.BatchPacket;
import redstonelamp.network.packet.DataPacket;

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
    }

    public void tick(){
        for(NetworkInterface networkInterface : interfaces){
            networkInterface.processData();
        }
    }

    public void registerInterface(NetworkInterface interface_){
        interfaces.add(interface_);
    }

    public void removeInterface(NetworkInterface interface_){
        interfaces.remove(interface_);
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
    }
}
