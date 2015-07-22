package redstonelamp.network.pc;

import com.flowpowered.networking.NetworkServer;
import com.flowpowered.networking.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import redstonelamp.Player;
import redstonelamp.Server;
import redstonelamp.network.NetworkInterface;
import redstonelamp.network.packet.DataPacket;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by jython234 on 7/21/2015.
 */
public class NettyInterface extends NetworkServer implements NetworkInterface{
    private Server server;

    public NettyInterface(Server server){
        super();
        this.server = server;
        server.getLogger().info("Starting Minecraft: PC server v" + PCNetworkInfo.MC_VERSION + " (protocol " + PCNetworkInfo.MC_PROTOCOL + ")");
        ChannelFuture f = bind(new InetSocketAddress(25565));
    }

    @Override
    public void sendPacket(Player player, DataPacket packet, boolean needACK, boolean immediate) {

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
        super.shutdown();
    }

    @Override
    public void emergencyShutdown() {
        super.shutdown();
    }

    @Override
    public void onBindSuccess(SocketAddress address) {
        server.getLogger().info("[NettyInterface]: Successfully binded to "+address.toString());
    }

    @Override
    public void onBindFailure(SocketAddress address, Throwable t) {
        server.getLogger().fatal("[NettyInterface]: Failed to bind to "+address.toString()+"!!!!");
        t.printStackTrace(System.err);
    }

    @Override
    public Session newSession(Channel channel) {
        return null;
    }

    @Override
    public void sessionInactivated(Session session) {

    }
}
