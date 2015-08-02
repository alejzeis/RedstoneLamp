package redstonelamp;

import redstonelamp.entity.Entity;
import redstonelamp.entity.EntityMetadata;
import redstonelamp.event.player.PlayerKickEvent;
import redstonelamp.event.player.PlayerQuitEvent;
import redstonelamp.item.Item;
import redstonelamp.level.Location;
import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.NetworkChannel;
import redstonelamp.network.PENetworkInfo;
import redstonelamp.network.packet.*;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;

/**
 * Implementation of a Player, connected from a mobile device or windows 10.
 */
public class PocketPlayer extends Entity implements Player{
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

    public PocketPlayer(Server server, JRakLibInterface rakLibInterface, String identifier, String address, int port, long clientId){
        this.server = server;
        this.rakLibInterface = rakLibInterface;
        this.identifier = identifier;
        this.address = new InetSocketAddress(address, port);
        this.clientId = clientId;
        connected = true;

        spawnTo(this);
    }

    @Override
    public void handleDataPacket(DataPacket packet){
        if(!connected){
            return;
        }

        if(packet.getBuffer()[0] == PENetworkInfo.BATCH_PACKET){
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

            case PENetworkInfo.LOGIN_PACKET:
                if(loggedIn){
                    break;
                }
                LoginPacket lp = (LoginPacket) packet;

                username = lp.username;
                //setNameTag(username);
                mojangClientId = lp.clientId;
                uuid = UUID.nameUUIDFromBytes(username.getBytes());

                if(lp.protocol1 != PENetworkInfo.MCPE_PROTOCOL){
                    String message;
                    if(lp.protocol1 < PENetworkInfo.MCPE_PROTOCOL){
                        message = "disconnectionScreen.outdatedClient";

                        PlayStatusPacket psp = new PlayStatusPacket();
                        psp.status = PlayStatusPacket.Status.LOGIN_FAILED_CLIENT;
                        //psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                        sendDirectDataPacket(psp);
                    } else {
                        message = "disconnectionScreen.outdatedServer";

                        PlayStatusPacket psp = new PlayStatusPacket();
                        psp.status = PlayStatusPacket.Status.LOGIN_FAILED_SERVER;
                       //psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
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
                        if(!player.kick("logged in from another location", false)){
                            player.close("left the game", "logged in from another location", true);
                        }
                        return;
                    }
                }

                setLocation(new Location(0, 2, 0, null));

                sendLoginPackets();

                server.getMainLevel().queueLoginChunks(this);

                /*
                SetEntityMotionPacket semp = new SetEntityMotionPacket();
                SetEntityMotionPacket.entitymotionpacket_EntityData data = new SetEntityMotionPacket.entitymotionpacket_EntityData();
                data.eid = 0;
                data.motionX = 0;
                data.motionY = 0;
                data.motionZ = 0;
                semp.entities = Arrays.asList(new SetEntityMotionPacket.entitymotionpacket_EntityData[] {data});
                sendDataPacket(semp);

                EntityMetadata em = new EntityMetadata();
                em.set((byte) 17, new CopyOnWriteArrayList<>());
                em.add((byte) 17, EntityMetadata.DataType.DATA_TYPE_POS);
                em.add((byte) 17, Arrays.asList(0, 0, 0));

                sendData(this, Arrays.asList(this), 0, em);

                PlayStatusPacket psp = new PlayStatusPacket();
                psp.status = PlayStatusPacket.Status.LOGIN_SUCCESS;
                psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(psp);

                StartGamePacket sgp = new StartGamePacket();
                sgp.seed = -1;
                sgp.x = (float) getLocation().getX();
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
                stp.time = 28617;
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
                cscp.slots = Item.getCreativeItems();
                cscp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDataPacket(cscp);

                //server.getMainLevel().queueLoginChunks(this);
                */

                break;
        }
    }

    private void sendLoginPackets() {
        PlayStatusPacket psp = new PlayStatusPacket();
        psp.status = PlayStatusPacket.Status.LOGIN_SUCCESS;
        //psp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(psp);

        StartGamePacket sgp = new StartGamePacket();
        sgp.seed = -1;
        sgp.x = (float) getLocation().getX();
        sgp.y = (float) getLocation().getY();
        sgp.z = (float) getLocation().getZ();
        sgp.spawnX = (int) getLocation().getX();
        sgp.spawnY = (int) getLocation().getY();
        sgp.spawnZ = (int) getLocation().getZ();
        sgp.generator = 1;
        sgp.gamemode = 1; //CREATIVE
        sgp.eid = 0; //Player EntityID is always 0
        //sgp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(sgp);

        SetTimePacket stp = new SetTimePacket();
        stp.time = 28617;
        stp.started = true;
        //stp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(stp);

        SetSpawnPositionPacket sspp = new SetSpawnPositionPacket();
        sspp.x = (int) getLocation().getX();
        sspp.y = (byte) getLocation().getY();
        sspp.z = (int) getLocation().getZ();
        //sspp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(sspp);

        AdventureSettingsPacket asp = new AdventureSettingsPacket();
        asp.flags = 0x80;
        //asp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(asp);

        SetHealthPacket shp = new SetHealthPacket();
        shp.health = 20;
        //shp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(shp);

        SetDifficultyPacket sdp = new SetDifficultyPacket();
        sdp.difficulty = 1;
        //sdp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(sdp);

        server.getLogger().info(username+" ["+identifier+"] logged in with entity id 0 at [x: "+Math.round(getLocation().getX())+", y: "+Math.round(getLocation().getY())+", z: "+Math.round(getLocation().getZ())+"]"); //TODO: Real info here.

        sendMetadata();

        ContainerSetContentPacket cscp = new ContainerSetContentPacket();
        cscp.windowId = ContainerSetContentPacket.SPECIAL_CREATIVE;
        cscp.slots = Item.getCreativeItems();
        //cscp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
        sendDataPacket(cscp);
    }
    
    public void doFirstSpawn(){
        SetTimePacket stp = new SetTimePacket();
        stp.time = 28617;
        stp.started = true;
        sendDataPacket(stp);

        RespawnPacket rp = new RespawnPacket();
        rp.x = (float) getLocation().getX();
        rp.y = (float) getLocation().getY();
        rp.z = (float) getLocation().getZ();
        sendDataPacket(rp);

        PlayStatusPacket psp = new PlayStatusPacket();
        psp.status = PlayStatusPacket.Status.PLAYER_SPAWN;
        sendDataPacket(psp);
    }

    private void sendMetadata() {
        EntityMetadata em = new EntityMetadata();
        em.set((byte) 0, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0));
        em.set((byte) 1, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_SHORT, (short) 300));
        em.set((byte) 2, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_STRING, username));
        em.set((byte) 3, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 1));
        em.set((byte) 4, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0));
        em.set((byte) 7, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_INT, 1));
        em.set((byte) 8, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0));
        em.set((byte) 15, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0));
        em.set((byte) 16, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_BYTE, (byte) 0));
        em.set((byte) 17, Arrays.asList((Object) EntityMetadata.DataType.DATA_TYPE_LONG, (long) 0));

        sendData(this, Arrays.asList(this), 0, em);
    }

    @Override
    public void sendDirectDataPacket(DataPacket packet){
        if(!connected){
            return;
        }
        //TODO: Call datapacket send event
        rakLibInterface.sendPacket(this, packet, false, true);
    }

    @Override
    public void sendDataPacket(DataPacket packet) {
        if(!connected){
            return;
        }
        //TODO: Call datapacket send event
        rakLibInterface.sendPacket(this, packet, false, false);
    }

    @Override
    public boolean kick(String reason, boolean admin){
        String message;
        if(admin){
            message = "Kicked by admin. Reason: "+ reason;
        } else {
            message = reason != "" ? reason : "disconnectionScreen.noReason";
        }
        PlayerKickEvent evt = new PlayerKickEvent(this, reason);
        server.getEventManager().getEventExecutor().execute(evt);
        if(!evt.isCanceled()) {
            close("left the game.", message, true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close(String message, String reason, boolean notifyClient){
        if(connected){
            server.getMainLevel().clearQueue(this);
            if(notifyClient){
                DisconnectPacket dp = new DisconnectPacket();
                dp.message = reason;
                dp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDirectDataPacket(dp);
            }

            connected = false;
            loggedIn = false;

            //TODO: Skip if Player.kick() called
            server.getEventManager().getEventExecutor().execute(new PlayerQuitEvent(this));
            
            rakLibInterface.close(this, notifyClient ? reason : "");

            server.getLogger().info(username + "["+identifier+"] logged out: "+reason);

            server.removePlayer(this);
        }
    }

    /**
     * Returns the players identifier
     * 
     * @return String
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the players IP Address
     * 
     * @return InetSocketAddress
     */
    public InetSocketAddress getAddress() {
        return address;
    }

    /**
     * Returns true if the player is online
     * 
     * @return boolean
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Returns true if the player is logged in
     * 
     * @return boolean
     */
    public boolean isLoggedIn(){
        return loggedIn;
    }

    /**
     * Returns the players username
     * 
     * @return String
     */
    public String getName() {
        return username;
    }
    
    /**
     * Returns the players skin as a String
     * 
     * @return String
     */
    public String getSkin() {
    	return skin;
    }
    
    /**
     * Returns the players UUID
     * 
     * @return UUID
     */
    public UUID getUUID() {
    	return uuid;
    }
    
    /**
     * Sends a message to the player
     * 
     * @param message The message to be sent to the player.
     */
    public void sendMessage(String message) {
    	//TODO: Send a message to the player
    }
    
    /**
     * Returns true if the player is an Operator
     * 
     * @return boolean
     */
	public boolean isOp() {
		return false;
	}
}
