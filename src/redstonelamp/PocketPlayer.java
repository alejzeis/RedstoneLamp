package redstonelamp;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;

import redstonelamp.entity.EntityMetadata;
import redstonelamp.entity.Human;
import redstonelamp.event.player.PlayerChatEvent;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerKickEvent;
import redstonelamp.event.player.PlayerMoveEvent;
import redstonelamp.event.player.PlayerQuitEvent;
import redstonelamp.inventory.PlayerInventory;
import redstonelamp.io.playerdata.PlayerDatabase;
import redstonelamp.item.Item;
import redstonelamp.level.location.Location;
import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.PENetworkInfo;
import redstonelamp.network.packet.AdventureSettingsPacket;
import redstonelamp.network.packet.BatchPacket;
import redstonelamp.network.packet.ContainerSetContentPacket;
import redstonelamp.network.packet.DataPacket;
import redstonelamp.network.packet.DisconnectPacket;
import redstonelamp.network.packet.LoginPacket;
import redstonelamp.network.packet.MovePlayerPacket;
import redstonelamp.network.packet.PlayStatusPacket;
import redstonelamp.network.packet.RespawnPacket;
import redstonelamp.network.packet.SetDifficultyPacket;
import redstonelamp.network.packet.SetHealthPacket;
import redstonelamp.network.packet.SetSpawnPositionPacket;
import redstonelamp.network.packet.SetTimePacket;
import redstonelamp.network.packet.StartGamePacket;
import redstonelamp.network.packet.TextPacket;
import redstonelamp.network.packet.UnknownDataPacket;
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

        switch (packet.getBuffer()[0]){

            default:
                if(packet instanceof UnknownDataPacket && server.isDebugMode()){
                    server.getLogger().debug("Unknown Packet: 0x"+String.format("%02X", packet.getBuffer()[0]));
                    break;
                }

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
                PlayerMoveEvent pme = new PlayerMoveEvent(this);
                server.getEventManager().getEventExecutor().execute(pme);
                Location l = getLocation();
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
                sendPosition(l, mpp.onGround);
                break;

            case PENetworkInfo.TEXT_PACKET:
                TextPacket tp = (TextPacket) packet;
                PlayerChatEvent pce = new PlayerChatEvent(this, tp.message);
                switch (tp.type){
                    case TextPacket.TYPE_RAW:
                    	if(!tp.message.toLowerCase().startsWith("/")) {
                    		server.getEventManager().getEventExecutor().execute(pce);
                    		if(!pce.isCanceled())
                    			server.broadcast("<"+username+"> "+tp.message);
                    	} else
                    		server.getCommandManager().getCommandExecutor().executeCommand(tp.message, this);
                    break;
                    case TextPacket.TYPE_CHAT:
                    	if(!tp.message.toLowerCase().startsWith("/")) {
                    		server.getEventManager().getEventExecutor().execute(pce);
                    		if(!pce.isCanceled())
                    			server.broadcast("<"+tp.source+"> "+tp.message);
                    	} else
                    		server.getCommandManager().getCommandExecutor().executeCommand(tp.message, this);
                    break;
                }
                break;
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
        
        sendMetadata();

        if(gamemode == 1) {
            ContainerSetContentPacket cscp = new ContainerSetContentPacket();
            cscp.windowId = ContainerSetContentPacket.SPECIAL_CREATIVE;
            cscp.slots = Item.getCreativeItems();
            sendDataPacket(cscp);
        }
    }
    
    public void doFirstSpawn(){
        PlayStatusPacket psp = new PlayStatusPacket();
        psp.status = PlayStatusPacket.Status.PLAYER_SPAWN;
        sendDataPacket(psp);

        RespawnPacket rp = new RespawnPacket();
        rp.x = (float) getLocation().getX();
        rp.y = (float) getLocation().getY();
        rp.z = (float) getLocation().getZ();
        sendDataPacket(rp);

        SetTimePacket stp = new SetTimePacket();
        stp.time = 2;
        stp.started = true;
        sendDataPacket(stp);

        server.broadcast(TextFormat.YELLOW+username+" joined the game.");
        server.getEventManager().getEventExecutor().execute(new PlayerJoinEvent(this));

        spawnToAll(server);

        for(Player player : server.getOnlinePlayers()){
            if(player instanceof PocketPlayer){ //TODO: Spawn to PC
                ((PocketPlayer) player).spawnTo(this);
                spawnTo(player);
            }
        }
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
            server.getEventManager().getEventExecutor().execute(new PlayerQuitEvent(this));
            
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
    
    public void ban() {
        ban_security.addPlayer(username);
        close(" left the game", "You have been banned!", true);
    }
    
    public void banIp() {
        ban_security.addIP(identifier.substring(0, identifier.indexOf(":")));
        close(" left the game", "You have been banned!", true);
    }
}
