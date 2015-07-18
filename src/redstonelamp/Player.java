package redstonelamp;

import redstonelamp.entity.Entity;
import redstonelamp.item.Item;
import redstonelamp.level.Location;
import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.NetworkChannel;
import redstonelamp.network.NetworkInfo;
import redstonelamp.network.packet.*;
import redstonelamp.utils.TextFormat;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;

/**
 * Represents a player, playing on the server.
 */
public class Player extends Entity{
    private Server server;

    private String username;
    private UUID uuid;
    private long mojangClientId;
    private String skin;

    private long clientId;
    private String identifier;
    private InetSocketAddress address;

    private boolean connected = false;
    private boolean loggedIn = false;

    private JRakLibInterface rakLibInterface;

    public Player(Server server, JRakLibInterface rakLibInterface, String identifier, String address, int port, long clientId){
        this.server = server;
        this.rakLibInterface = rakLibInterface;
        this.identifier = identifier;
        this.address = new InetSocketAddress(address, port);
        this.clientId = clientId;
        connected = true;
    }

    public void handleDataPacket(DataPacket packet){
        if(!connected){
            return;
        }

        if(packet.getBuffer()[0] == NetworkInfo.BATCH_PACKET){
            server.getNetwork().processBatch((BatchPacket) packet, this);
            return;
        }

        switch (packet.getBuffer()[0]){

            default:
                if(packet instanceof UnknownDataPacket && server.isDebugMode()){
                    server.getLogger().debug("Unknown Packet: 0x"+String.format("%02X", packet.getBuffer()[0]));
                    break;
                }
                if(server.isDebugMode()){
                    server.getLogger().debug("Packet ("+packet.getClass().getName()+") 0x"+String.format("%02X ", packet.getBuffer()[0]));
                }

            case NetworkInfo.LOGIN_PACKET:
                if(loggedIn){
                    break;
                }
                LoginPacket lp = (LoginPacket) packet;

                username = lp.username;
                mojangClientId = lp.clientId;
                uuid = UUID.nameUUIDFromBytes(username.getBytes());

                if(lp.protocol1 != NetworkInfo.MCPE_PROTOCOL){
                    String message;
                    if(lp.protocol1 < NetworkInfo.MCPE_PROTOCOL){
                        message = "disconnectionScreen.outdatedClient";

                        PlayStatusPacket psp = new PlayStatusPacket();
                        psp.status = PlayStatusPacket.Status.LOGIN_FAILED_CLIENT;
                        psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                        sendDirectDataPacket(psp);
                    } else {
                        message = "disconnectionScreen.outdatedServer";

                        PlayStatusPacket psp = new PlayStatusPacket();
                        psp.status = PlayStatusPacket.Status.LOGIN_FAILED_SERVER;
                        psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                        sendDirectDataPacket(psp);
                    }
                    close("", message, false);

                    break;
                }

                if(lp.skin.length() != 64 * 32 * 4 && lp.skin.length() != 64 * 64 * 4){
                    close("", "disconnectionScreen.invalidSkin", true);
                    break;
                }

                skin = lp.skin;

                for(Player player : server.getOnlinePlayers()){
                    if(player.getName().equalsIgnoreCase(username) && player.isConnected() && player.isLoggedIn()){
                        player.kick("logged in from another location", false);
                        return;
                    }
                }

                setLocation(new Location(0, 64, 0, null));

                PlayStatusPacket psp = new PlayStatusPacket();
                psp.status = PlayStatusPacket.Status.LOGIN_SUCCESS;
                psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(psp);

                StartGamePacket sgp = new StartGamePacket();
                sgp.seed = -1;
                sgp.x = (float) getLocation().getX(); //Dummy positions
                sgp.y = (float) getLocation().getY();
                sgp.z = (float) getLocation().getZ();
                sgp.spawnX = (int) getLocation().getX();
                sgp.spawnY = (int) getLocation().getY();
                sgp.spawnZ = (int) getLocation().getZ();
                sgp.generator = 1;
                sgp.gamemode = 1; //CREATIVE
                sgp.eid = 0; //Player EntityID is always 0
                sgp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(sgp);

                SetTimePacket stp = new SetTimePacket();
                stp.time = 12000;
                stp.started = true;
                stp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(stp);

                SetSpawnPositionPacket sspp = new SetSpawnPositionPacket();
                sspp.x = (int) getLocation().getX();
                sspp.y = (byte) getLocation().getY();
                sspp.z = (int) getLocation().getZ();
                sspp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(sspp);

                SetHealthPacket shp = new SetHealthPacket();
                shp.health = 20;
                shp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(shp);

                SetDifficultyPacket sdp = new SetDifficultyPacket();
                sdp.difficulty = 1;
                sdp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(sdp);

                server.getLogger().info(username+" ["+identifier+"] logged in with entity id 0 at [x: "+Math.round(getLocation().getX())+", y: "+Math.round(getLocation().getY())+", z: "+Math.round(getLocation().getZ())+"]"); //TODO: Real info here.

                ContainerSetContentPacket cscp = new ContainerSetContentPacket();
                cscp.windowId = ContainerSetContentPacket.SPECIAL_CREATIVE;
                cscp.slots = Arrays.asList(new Item[] {new Item(1, (short) 0, 4)});
                cscp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(cscp);

                server.getMainLevel().queueLoginChunks(this);

                break;
        }
    }

    public void sendDirectDataPacket(DataPacket packet){
        if(!connected){
            return;
        }
        //TODO: Call datapacket send event
        rakLibInterface.sendPacket(this, packet, false, true);
    }

    public void sendDataPacket(DataPacket packet) {
        if(!connected){
            return;
        }
        //TODO: Call datapacket send event
        rakLibInterface.sendPacket(this, packet, false, false);
    }

    public void kick(String reason, boolean admin){
        String message;
        if(admin){
            message = "Kicked by admin. Reason: "+ reason;
        } else {
            message = reason != "" ? reason : "disconnectionScreen.noReason";
        }
        close("left the game.", message, true);
    }

    /**
     * Internal Method, please use <code>Player.kick</code> instead.
     * @param message
     * @param reason
     * @param notifyClient
     */
    public void close(String message, String reason, boolean notifyClient){
        if(connected){
            if(notifyClient){
                DisconnectPacket dp = new DisconnectPacket();
                dp.message = reason;
                dp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDirectDataPacket(dp);
            }

            connected = false;
            loggedIn = false;

            rakLibInterface.close(this, notifyClient ? reason : "");

            server.getLogger().info(username + "["+identifier+"] logged out: "+reason);

            server.removePlayer(this);
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public String getName() {
        return username;
    }
}
