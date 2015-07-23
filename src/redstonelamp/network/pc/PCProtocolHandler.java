package redstonelamp.network.pc;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import redstonelamp.DesktopPlayer;
import redstonelamp.Player;
import redstonelamp.network.pc.packet.MinecraftPacket;

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
            case PCNetworkInfo.HANDHSAKE_HANDSHAKE:

                break;
            case PCNetworkInfo.STATUS_STATUS_REQUEST:

                break;
        }

        Player player = pcInterface.getServer().getPlayer(session.getRemoteAddress().toString());
        if(player instanceof DesktopPlayer){

        } else { //No player class

        }
    }

    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
        System.out.println( "IDLE " + session.getIdleCount( status ));
    }
}
