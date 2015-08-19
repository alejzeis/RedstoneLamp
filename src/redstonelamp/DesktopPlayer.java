package redstonelamp;

import org.apache.mina.core.session.IoSession;
import redstonelamp.auth.AuthenticationAgent;
import redstonelamp.auth.pc.PCAuthAgent;
import redstonelamp.entity.Entity;
import redstonelamp.entity.Human;
import redstonelamp.network.packet.DataPacket;
import redstonelamp.network.packet.TextPacket;
import redstonelamp.network.pc.Chat;
import redstonelamp.network.pc.PCInterface;
import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.ProtocolState;
import redstonelamp.network.pc.packet.handshake.HandshakePacket;
import redstonelamp.network.pc.packet.login.LoginDisconnectPacket;
import redstonelamp.network.pc.packet.login.LoginStartPacket;
import redstonelamp.network.pc.packet.login.LoginSuccessPacket;
import redstonelamp.network.pc.packet.login.SetCompressionPacket;
import redstonelamp.network.pc.packet.play.JoinGamePacket;
import redstonelamp.utils.Binary;
import redstonelamp.utils.Skin;
import redstonelamp.utils.TextFormat;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Implementation of a player, connected from a computer.
 */
public class DesktopPlayer extends Human implements Player{
	private String displayName = "Steve"; //TODO: Remove init when ready
    private String username;
    private UUID uuid;

    private Server server;
    private PCInterface pcInterface;

    private final IoSession ioSession;
    private ProtocolState protocolState;

    private boolean compressionActivated = false;
    private int compressionThreshold = -1;

    public DesktopPlayer(PCInterface pcInterface, Server server, IoSession session){
        super(server.getNextEntityId());
        this.pcInterface = pcInterface;
        this.server = server;
        this.ioSession = session;

        protocolState = ProtocolState.STATE_HANDSHAKE;
    }
    
    @Override
    public Server getServer() {
    	return server;
    }

    @Override
    public void handleDataPacket(DataPacket packet) {
        System.out.println("DataPacket: 0x"+String.format("%02X", packet.getPID()));
        if(protocolState == ProtocolState.STATE_HANDSHAKE){
            handleHandshakePacket(packet);
        } else if(protocolState == ProtocolState.STATE_LOGIN){
            handleLoginPacket(packet);
        } else {
            handlePlayPacket(packet);
        }
    }

    private void handleHandshakePacket(DataPacket packet) {
        if(!(packet instanceof HandshakePacket)){
            throw new IllegalArgumentException("Packet must be instanceof HandshakePacket.");
        }
        protocolState = ProtocolState.STATE_LOGIN;
        int version = ((HandshakePacket) packet).protocolVersion;
        if(version != PCNetworkInfo.MC_PROTOCOL){
            Chat message;
            if(version < PCNetworkInfo.MC_PROTOCOL){
                message = new Chat(TextFormat.RED+"Outdated Client! "+TextFormat.GOLD+"I'm on "+PCNetworkInfo.MC_PROTOCOL+", you're on "+version);
            } else {
                message = new Chat(TextFormat.RED+"Outdated Server! "+TextFormat.GOLD+"I'm on "+PCNetworkInfo.MC_PROTOCOL+", you're on "+version);
            }
            LoginDisconnectPacket ldp = new LoginDisconnectPacket();
            ldp.message = message;
            sendDataPacket(ldp);
            close("", "invalid protocol: "+version, false);
        }
    }

    private void handleLoginPacket(DataPacket packet) {
        int id = packet.getPID();
        switch(id){
            case PCNetworkInfo.LOGIN_LOGIN_START:
                LoginStartPacket lsp = (LoginStartPacket) packet;
                //TODO: Initiate auth at EncryptionKeyReponse
                AuthenticationAgent.AuthenticationResponse response = server.getAuthenticationManager().getAgent(PCAuthAgent.class).authenticate(lsp.name, null);
                if(!response.allowed){
                    LoginDisconnectPacket ldp = new LoginDisconnectPacket();
                    switch (response.reason){
                        case "redstonelamp.authFailed.serverFull":
                            ldp.message = new Chat("Server full.");
                            break;
                        default:
                            ldp.message = new Chat(response.reason);
                            break;
                    }
                    sendDataPacket(ldp);
                    close("", response.reason, false);
                    return;
                }
                uuid = response.uuid;

                SetCompressionPacket scp = new SetCompressionPacket();
                //scp.threshold = 512; //TODO: Get threshold from YAML config
                scp.threshold = -1;
                sendDataPacket(scp);

                /*
                compressionActivated = true;
                compressionThreshold = 512;
                */

                LoginSuccessPacket reply = new LoginSuccessPacket();
                reply.uuid = uuid;
                reply.username = lsp.name;
                sendDataPacket(reply);

                protocolState = ProtocolState.STATE_PLAY;

                JoinGamePacket jgp = new JoinGamePacket(); //TODO: replace dummy values
                jgp.entityID = 0; //Player Entity ID is always zero? Not sure if this is for PC too
                jgp.difficulty = 1;
                jgp.dimension = 0;
                jgp.gamemode = 1;
                jgp.maxPlayers = (byte) server.getMaxPlayers(); //TODO: maxPlayers over 256 may result in encoding failure
                jgp.levelType = "flat";
                sendDataPacket(jgp);
                break;
        }
    }

    private void handlePlayPacket(DataPacket packet) {

    }

    @Override
    public void sendDataPacket(DataPacket packet) {
        pcInterface.sendPacket(this, packet, false, false);
    }

    @Override
    public void sendDirectDataPacket(DataPacket packet) {
        pcInterface.sendPacket(this, packet, false, true);
    }

    @Override
    public boolean kick(String reason, boolean admin) {
        return false;
    }

    @Override
    public void close(String message, String reason, boolean notifyClient) {
        if(notifyClient){
            //TODO: send PLAY_DISCONNECT (0x40)
        }
        server.broadcast(getName()+message);
        server.getLogger().info(getName()+" ["+getIdentifier()+"]: logged out with reason: "+reason);
        server.removePlayer(this);

        ioSession.close(false); //Close after all write requests finish
    }

    @Override
    public String getIdentifier() {
        return ioSession.getRemoteAddress().toString();
    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Skin getSkin() {
        return null;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public void sendMessage(String message) {

    }
    
    @Override
    public void sendPopup(String message) {
    	
    }
    
    @Override
    public void sendTip(String message) {
    	
    }

    @Override
    public boolean isOp() {
        return false;
    }

    public IoSession getSession(){
        return ioSession;
    }
    
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String name) {
		displayName = name;
	}

    public ProtocolState getProtocolState() {
        return protocolState;
    }

    public boolean isCompressionActivated() {
        return compressionActivated;
    }

    public int getCompressionThreshold() {
        return compressionThreshold;
    }
}
