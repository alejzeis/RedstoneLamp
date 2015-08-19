package redstonelamp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import redstonelamp.entity.EntityMetadata;
import redstonelamp.entity.Human;
import redstonelamp.event.network.DataPacketReceiveEvent;
import redstonelamp.event.network.DataPacketSendEvent;
import redstonelamp.event.player.PlayerChatEvent;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerKickEvent;
import redstonelamp.event.player.PlayerMoveEvent;
import redstonelamp.event.player.PlayerQuitEvent;
import redstonelamp.inventory.PlayerInventory;
import redstonelamp.io.playerdata.PlayerDatabase;
import redstonelamp.item.Item;
import redstonelamp.item.ItemValues;
import redstonelamp.level.Chunk;
import redstonelamp.level.location.Location;
import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.PENetworkInfo;
import redstonelamp.network.packet.*;
import redstonelamp.security.BanSecurity;
import redstonelamp.utils.Skin;
import redstonelamp.utils.TextFormat;

/**
 * Implementation of a Player, connected from a mobile device or windows 10.
 */
public class PocketPlayer extends Human implements Player{
    private Server server;

    private String username;
    private String displayName;
    private UUID uuid;
    private long mojangClientId;

    private long clientId;
    private String identifier;
    private InetSocketAddress address;

    private boolean connected = false;
    private boolean loggedIn = false;
    private boolean spawned = false;

    private PlayerDatabase.DatabaseEntry dbEntry;
    private PlayerInventory inventory;

    private int gamemode;
    private int health;

    private JRakLibInterface rakLibInterface;
    
    private BanSecurity ban_security;

    public PocketPlayer(Server server, JRakLibInterface rakLibInterface, String identifier, String address, int port, long clientId){
        super(server.getNextEntityId());
        ban_security = new BanSecurity();
        this.server = server;
        this.rakLibInterface = rakLibInterface;
        this.identifier = identifier;
        this.address = new InetSocketAddress(address, port);
        this.clientId = clientId;

        connected = true;

        spawnTo(this);
    }
    
    @Override
    public Server getServer() {
    	return server;
    }

    private void loadPlayerData() {
        if(dbEntry.getGamemode() == -1){ //We need a new entry
            dbEntry.setGamemode(server.getMainLevel().getGamemode());
            dbEntry.setHealth(20);
            dbEntry.setLocation(server.getMainLevel().getSpawnLocation());
            dbEntry.setUUID(uuid);
            server.getPlayerDatabase().putEntry(dbEntry);

            loadPlayerData();
        }
        if(!uuid.toString().equals(dbEntry.getUUID().toString())){
            server.getLogger().error("UUID Does not match: {mine: " + uuid.toString() + ", DB: " + dbEntry.getUUID().toString());
        }
        gamemode = dbEntry.getGamemode();
        health = dbEntry.getHealth();
        setLocation(dbEntry.getLocation());
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

        DataPacketReceiveEvent evt = new DataPacketReceiveEvent(packet, this);
        server.throwEvent(evt);
        if(evt.isCanceled())
            return;

        if(packet instanceof UnknownDataPacket && server.isDebugMode())
            server.getLogger().debug("Unknown Packet: 0x"+String.format("%02X", packet.getBuffer()[0]));


        switch (packet.getBuffer()[0]){

            case PENetworkInfo.LOGIN_PACKET:
                if(loggedIn){
                    break;
                }
                LoginPacket lp = (LoginPacket) packet;

                username = lp.username;
                setNameTag(username);
                displayName = username;
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

                if(ban_security.nameBanned(username) || ban_security.ipBanned(identifier.substring(0, identifier.indexOf(":"))))
                    close("", "You are banned from this server.", true);
                	

                if(lp.skin.getBytes().length != 64 * 32 * 4 && lp.skin.getBytes().length != 64 * 64 * 4){
                	if(server.isDebugMode())
                        System.out.println(lp.skin.getBytes().length);
                    close("", "disconnectionScreen.invalidSkin", true);
                    break;
                }


                skin = lp.skin;
                isSlim = lp.slim;

                for(Player player : server.getOnlinePlayers()){
                    if(player.getName().equalsIgnoreCase(username) && player.isConnected() && player.isLoggedIn()){
                        if(!player.kick("logged in from another location", false)){
                            player.close(" left the game", "logged in from another location", true);
                        }
                        return;
                    }
                }

                dbEntry = server.getPlayerDatabase().getEntry(uuid);
                loadPlayerData();

                loggedIn = true;

                //setLocation(new Location(128, 2, 128, server.getMainLevel()));

                sendLoginPackets();

                RedstoneLamp.getAsync().submit(() -> server.getMainLevel().queueLoginChunks(this));
                break;

            case PENetworkInfo.MOVE_PLAYER_PACKET:
                MovePlayerPacket mpp = (MovePlayerPacket) packet;
                Location l = getLocation();
                PlayerMoveEvent pme = new PlayerMoveEvent(this, mpp.onGround, l);
                server.throwEvent(pme);
                if(!pme.isCanceled()) {
                	//TODO: check movement
                	l.setX(mpp.x);
                	l.setY(mpp.y);
                	l.setZ(mpp.z);
                	l.setYaw(mpp.yaw);
                	l.setPitch(mpp.pitch);
                	setLocation(l);
                	getLocation().getLevel().broadcastMovement(this, mpp);
                    return;
                }
                sendPosition(pme.getLocation(), pme.isOnGround());
                break;

            case PENetworkInfo.TEXT_PACKET:
                TextPacket tp = (TextPacket) packet;
                PlayerChatEvent pce = new PlayerChatEvent(this, tp.message);
                switch (tp.type){
                    case TextPacket.TYPE_RAW:
                    	if(!tp.message.toLowerCase().startsWith("/")) {
                    		server.throwEvent(pce);
                    		if(!pce.isCanceled()) {
                    			server.getLogger().info(pce.getFormat());
                    			for(Player p : pce.getRecipents()) {
                    				p.sendMessage(pce.getFormat());
                    			}
                    		}	
                    	} else
                    		server.getCommandManager().getCommandExecutor().executeCommand(tp.message, this);
                    break;
                    case TextPacket.TYPE_CHAT:
                    	if(!tp.message.toLowerCase().startsWith("/")) {
                    		server.throwEvent(pce);
                    		if(!pce.isCanceled()) {
                    			server.getLogger().info(pce.getFormat());
                    			for(Player p : pce.getRecipents()) {
                    				p.sendMessage(pce.getFormat());
                    			}
                    		}	
                    	} else
                    		server.getCommandManager().getCommandExecutor().executeCommand(tp.message, this);
                    break;
                }
                break;

            case PENetworkInfo.REMOVE_BLOCK_PACKET:
                if(!spawned /*|| alive*/) {
                    break;
                }
                //TODO: break level blocks.
                //TODO: Check survival, add/remove inventory etc.
                RemoveBlockPacket rbp = (RemoveBlockPacket) packet;
                UpdateBlockPacket ubp = new UpdateBlockPacket();

                UpdateBlockPacket.Record r = new UpdateBlockPacket.Record();
                r.x = rbp.x;
                r.y = rbp.y;
                r.z = rbp.z;
                r.blockId = ItemValues.AIR; //TODO
                r.blockData = 0;
                r.flags = UpdateBlockPacket.FLAG_NONE;

                ubp.records = Arrays.asList(r);
                server.getNetwork().broadcastPacket(ubp, PocketPlayer.class);
                break;

            case PENetworkInfo.ANIMATE_PACKET:
                if(!spawned /*|| !alive*/){
                    break;
                }
                AnimatePacket ap = (AnimatePacket) packet;

                AnimatePacket ap2 = new AnimatePacket();
                ap2.eid = getId();
                ap2.action = ap.action;
                server.getNetwork().broadcastPacket(ap2, PocketPlayer.class);
                break;

            default:
                if(server.isDebugMode()){
                    server.getLogger().warning("Unhandled packet: 0x"+String.format("%02X", packet.getPID()));
                }
        }
    }

    /**
     * NOTE TO PLUGIN DEVELOPERS: Please use <code>PocketPlayer.teleport()</code> instead.
     * @param l
     */
    public void sendPosition(Location l, boolean onGround) {
        MovePlayerPacket mpp = new MovePlayerPacket();
        mpp.onGround = onGround;
        mpp.eid = getId();
        mpp.x = (float) l.getX();
        mpp.y = (float) l.getY();
        mpp.z = (float) l.getZ();
        mpp.yaw = l.getYaw();
        mpp.pitch = l.getPitch();
        mpp.bodyYaw = l.getYaw(); //TODO: body yaw
        sendDataPacket(mpp);
    }

    private void sendLoginPackets() {
        PlayStatusPacket psp = new PlayStatusPacket();
        psp.status = PlayStatusPacket.Status.LOGIN_SUCCESS;
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
        sgp.gamemode = gamemode;
        sgp.eid = 0; //Player EntityID is always 0
        sendDataPacket(sgp);

        SetTimePacket stp = new SetTimePacket();
        stp.time = (int) getLocation().getLevel().getTime();
        stp.started = true;
        sendDataPacket(stp);

        SetSpawnPositionPacket sspp = new SetSpawnPositionPacket();
        sspp.x = (int) getLocation().getX();
        sspp.y = (byte) getLocation().getY();
        sspp.z = (int) getLocation().getZ();
        sendDataPacket(sspp);

        AdventureSettingsPacket asp = new AdventureSettingsPacket();
        int flags = 0;
        flags |= 0x20; //Allow nametags
        if(gamemode == 1) {
            flags |= 0x80; //Allow flight
        }
        asp.flags = flags;
        sendDataPacket(asp);

        SetHealthPacket shp = new SetHealthPacket();
        shp.health = health;
        sendDataPacket(shp);

        SetDifficultyPacket sdp = new SetDifficultyPacket();
        sdp.difficulty = 1;
        sendDataPacket(sdp);

        server.getLogger().info(username+" ["+identifier+"] logged in with entity id "+getId()+" at [x: "+Math.round(getLocation().getX())+", y: "+Math.round(getLocation().getY())+", z: "+Math.round(getLocation().getZ())+"]");
        server.getLogger().debug("Players online: " + server.getOnlinePlayers().size());
        
        sendMetadata();

        if(gamemode == 1) {
            ContainerSetContentPacket cscp = new ContainerSetContentPacket();
            cscp.windowId = ContainerSetContentPacket.SPECIAL_CREATIVE;
            cscp.slots = Item.getCreativeItems();
            sendDataPacket(cscp);
        }
    }
    
    public void doFirstSpawn(){
        spawned = true;

        PlayStatusPacket psp = new PlayStatusPacket();
        psp.status = PlayStatusPacket.Status.PLAYER_SPAWN;
        sendDataPacket(psp);

        RespawnPacket rp = new RespawnPacket();
        rp.x = (float) getLocation().getX();
        rp.y = (float) getLocation().getY();
        rp.z = (float) getLocation().getZ();
        sendDataPacket(rp);

        SetTimePacket stp = new SetTimePacket();
        stp.time = (int) getLocation().getLevel().getTime();
        stp.started = true;
        sendDataPacket(stp);

        server.broadcast(TextFormat.YELLOW+username+" joined the game.");
        server.throwEvent(new PlayerJoinEvent(this));

        spawnToAll(server);
    }

    private void sendMetadata() {
        sendData(this, Arrays.asList(this), 0, getFakeMetadata());
    }

    @Override
    public void sendDirectDataPacket(DataPacket packet){
        if(!connected){
            return;
        }

        DataPacketSendEvent evt = new DataPacketSendEvent(packet, this);
        server.throwEvent(evt);
        if(evt.isCanceled())
            return;

        rakLibInterface.sendPacket(this, packet, false, true);
    }

    @Override
    public void sendDataPacket(DataPacket packet) {
        if(!connected){
            return;
        }

        DataPacketSendEvent evt = new DataPacketSendEvent(packet, this);
        server.throwEvent(evt);
        if(evt.isCanceled())
            return;

        rakLibInterface.sendPacket(this, packet, false, false);
    }

    @Override
    public boolean kick(String reason, boolean admin){
        String message;
        if(admin){
            message = "Kicked by admin. Reason: "+ reason;
        } else {
            message = (reason.equals("") ? "disconnectionScreen.noReason" : reason);
        }
        PlayerKickEvent evt = new PlayerKickEvent(this, reason);
        server.throwEvent(evt);
        if(!evt.isCanceled()) {
            close(" left the game.", message, true);
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
                //dp.setChannel(NetworkChannel.CHANNEL_PRIORITY);
                sendDirectDataPacket(dp);
            }

            connected = false;
            loggedIn = false;
            spawned = false;

            getLocation().getLevel().despawnFromAll(this);
            
            rakLibInterface.close(this, notifyClient ? reason : "");

            server.getLogger().info(username + "["+identifier+"] logged out: "+reason);
            server.broadcast(TextFormat.YELLOW + username + message);
            server.throwEvent(new PlayerQuitEvent(this));
            
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
     * Returns the player's skin.
     * 
     * @return String
     */
    public Skin getSkin() {
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
    	TextPacket packet = new TextPacket();
        packet.type = TextPacket.TYPE_RAW;
        packet.message = message;
        sendDataPacket(packet);
    }
    
    public void sendPopup(String message) {
    	TextPacket packet = new TextPacket();
        packet.type = TextPacket.TYPE_POPUP;
        packet.message = message;
        sendDataPacket(packet);
    }
    
    public void sendTip(String message) {
    	TextPacket packet = new TextPacket();
        packet.type = TextPacket.TYPE_TIP;
        packet.message = message;
        sendDataPacket(packet);
    }
    
    /**
     * Returns true if the player is an Operator
     * 
     * @return boolean
     */
	public boolean isOp() {
		return false;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String name) {
		displayName = name;
	}

    @Override
    public byte[] orderChunk(Chunk chunk) {
        ByteBuffer bb = ByteBuffer.allocate(83200);
        bb.put(chunk.getBlockIds());
        bb.put(chunk.getBlockMeta());
        bb.put(chunk.getSkylight());
        bb.put(chunk.getBlocklight());
        bb.put(chunk.getHeightmap());
        bb.put(chunk.getBiomeColors());
        return bb.array();
    }

    public void ban() {
        ban_security.addPlayer(username);
        close(" left the game", "You have been banned!", true);
    }
    
    public void banIp() {
        ban_security.addIP(identifier.substring(0, identifier.indexOf(":")));
        close(" left the game", "You have been banned!", true);
    }
}
