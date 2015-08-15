package redstonelamp.network.pc;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import redstonelamp.DesktopPlayer;
import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.network.NetworkInterface;
import redstonelamp.network.packet.DataPacket;
import redstonelamp.network.pc.codec.HeaderDecoder;
import redstonelamp.network.pc.codec.HeaderEncoder;
import redstonelamp.network.pc.packet.MinecraftPacket;
import redstonelamp.network.pc.packet.PCDataPacket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PC Networking interface.
 */
public class PCInterface implements NetworkInterface{
    private Server server;
    private Map<Integer, Class<? extends PCDataPacket>> packets = new ConcurrentHashMap<>();

    private IoAcceptor acceptor;
    private PCProtocolHandler handler;

    public PCInterface(Server server){
        this.server = server;

        server.getLogger().info("Starting Minecraft: PC server v"+PCNetworkInfo.MC_VERSION+" (protocol "+PCNetworkInfo.MC_PROTOCOL+").");
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new HeaderEncoder(), new HeaderDecoder()));

        handler = new PCProtocolHandler(this);
        acceptor.setHandler(handler);

        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        try {
            acceptor.bind(new InetSocketAddress(Integer.parseInt(RedstoneLamp.properties.getProperty("mcpc-port", "25565"))));
            server.getLogger().debug("[PCInterface]: Successfully bound to *:25565");
        } catch (IOException e) {
            server.getLogger().fatal("[PCInterface]: Failed to bind to *:25565!");
            e.printStackTrace();
        }
    }

    public PCDataPacket getPacket(int pid){
        if(packets.containsKey(pid)){
            try {
                return packets.get(pid).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void registerPacket(int pid, Class<? extends PCDataPacket> clazz){
        packets.put(pid, clazz);
    }

    private void registerPackets() {

    }

    @Override
    public void sendPacket(Player player, DataPacket packet, boolean needACK, boolean immediate) {
        if(!(player instanceof DesktopPlayer)){
           throw new IllegalArgumentException("Player must be a DesktopPlayer instance!");
        }
        try {
            ((DesktopPlayer) player).getSession().write(packet);
        } catch (Exception e) {
            server.getLogger().warning("Exception while sending packet from: "+player.getIdentifier());
            e.printStackTrace();
        }
    }

    @Override
    public void close(Player player, String reason) {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void processData() {

    }

    @Override
    public void shutdown() {
        acceptor.unbind();
        acceptor.dispose();
    }

    @Override
    public void emergencyShutdown() {
        acceptor.unbind();
    }

    public Server getServer() {
        return server;
    }
}
