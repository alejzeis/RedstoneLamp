package redstonelamp.network.pc;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.simple.JSONObject;
import redstonelamp.DesktopPlayer;
import redstonelamp.Player;
import redstonelamp.network.pc.packet.MinecraftPacket;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.network.pc.packet.handshake.HandshakePacket;
import redstonelamp.network.pc.packet.status.StatusResponse;
import redstonelamp.utils.TextFormat;

/**
 * Protocol handler.
 */
public class PCProtocolHandler extends IoHandlerAdapter{
    private PCInterface pcInterface;

    public PCProtocolHandler(PCInterface pcInterface){
        this.pcInterface = pcInterface;
    }

    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception {
        MinecraftPacket pkt = (MinecraftPacket) message;

        switch (pkt.packetID){
            case PCNetworkInfo.HANDHSAKE_HANDSHAKE: //Since the status request, and handshake have the same id, we must check the length
                if(pkt.payload.length <= 14){ //It's a status request.
                    sendStatusReply(session);
                } else { //It's a handshake
                    HandshakePacket hp = new HandshakePacket();
                    hp.decode(pkt.payload);
                    if (hp.nextState == HandshakePacket.STATE_LOGIN) {
                        DesktopPlayer player = new DesktopPlayer(pcInterface, pcInterface.getServer(), session);
                        pcInterface.getServer().addPlayer(player);
                    } //Send status reply once we get the status request
                }
                break;
        }

        Player player = pcInterface.getServer().getPlayer(session.getRemoteAddress().toString());
        if(player instanceof DesktopPlayer){
            
        } else { //No player class

        }
    }

    private void sendStatusReply(IoSession session) {
        JSONObject root = new JSONObject();
        JSONObject version = new JSONObject();
        JSONObject players = new JSONObject();
        JSONObject description = new JSONObject();

        version.put("name", "RedstoneLamp "+PCNetworkInfo.MC_VERSION);
        version.put("protocol", PCNetworkInfo.MC_PROTOCOL);

        players.put("max", pcInterface.getServer().getMaxPlayers());
        players.put("online", pcInterface.getServer().getOnlinePlayers().size());

        description.put("text", TextFormat.RED+"A RedstoneLamp server.");

        root.put("version", version);
        root.put("players", players);
        root.put("description", description);

        StatusResponse response = new StatusResponse();
        response.jsonResponse = root.toJSONString();
        session.write(response);
    }

    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
        System.out.println( "IDLE " + session.getIdleCount( status ));
    }
}
