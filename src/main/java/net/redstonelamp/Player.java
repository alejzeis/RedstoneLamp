/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.redstonelamp.block.Block;
import net.redstonelamp.cmd.CommandSender;
import net.redstonelamp.cmd.exception.CommandException;
import net.redstonelamp.entity.PlayerEntity;
import net.redstonelamp.event.EventExecutor;
import net.redstonelamp.event.block.BlockBreakEvent;
import net.redstonelamp.event.block.BlockPlaceEvent;
import net.redstonelamp.event.chunk.ChunkRequestEvent;
import net.redstonelamp.event.player.PlayerAnimateEvent;
import net.redstonelamp.event.player.PlayerChatEvent;
import net.redstonelamp.event.player.PlayerEquipmentChangeEvent;
import net.redstonelamp.event.player.PlayerLoginEvent;
import net.redstonelamp.event.player.PlayerMoveEvent;
import net.redstonelamp.event.player.PlayerSpawnEvent;
import net.redstonelamp.inventory.NBTPlayerInventory;
import net.redstonelamp.inventory.PlayerInventory;
import net.redstonelamp.item.Item;
import net.redstonelamp.level.ChunkPosition;
import net.redstonelamp.level.position.BlockPosition;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.permission.OperatorPermissions;
import net.redstonelamp.permission.Permission;
import net.redstonelamp.permission.PermissionAttachment;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.request.*;
import net.redstonelamp.response.*;
import net.redstonelamp.utils.TextFormat;

/**
 * <strong>Protocol-independent</strong> Player class. Represents a Player on the server
 *
 * @author RedstoneLamp Team
 */
public class Player extends PlayerEntity implements CommandSender{
    private final Protocol protocol;
    private final Server server;
    private final SocketAddress address;
    private final String identifier;

    private long startLogin;

    private String username;
    private String displayName;
    private UUID uuid;
    private int clientID;
    private byte[] skin;
    private boolean slim;

    private boolean connected = true;
    private boolean spawned = false;
    private boolean sprinting = false;
    private boolean sneaking = false;

    private int gamemode;
    private PlayerInventory inventory;
    
    private HashMap<Plugin, PermissionAttachment> permissionAttachments = new HashMap<>();

    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> with the <code>identifier</code>
     *
     * @param protocol   The protocol this player belongs to
     * @param identifier The client's identifier. This is the address the player is connecting from, in the format:
     *                   [ip]:[port]
     */
    @Deprecated
    public Player(Protocol protocol, String identifier){
        this.protocol = protocol;
        this.identifier = identifier;
        address = null;

        server = protocol.getManager().getServer();
    }
    
    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> connecting from
     * <code>address</code>
     *
     * @param protocol The protocol this player belongs to
     * @param address  The SocketAddress this player is connecting from
     */
    public Player(Protocol protocol, SocketAddress address){
        this.protocol = protocol;
        this.address = address;

        identifier = address.toString();
        server = protocol.getManager().getServer();
    }

    private void loadPlayerData(){
        PlayerDatabase.PlayerData data = server.getPlayerDatabase().getData(uuid);
        if(data == null){
            server.getLogger().info("Couldn't find PlayerData for player " + getName() + " (" + uuid + ")");
            data = new PlayerDatabase.PlayerData();
            data.setUuid(uuid);
            data.setPosition(server.getLevelManager().getMainLevel().getSpawnPosition());
            data.setHealth(20);
            data.setGamemode(server.getConfig().getInt("gamemode"));
            PlayerInventory inv = new NBTPlayerInventory();
            inv.setItemInHand(new Item(0, (short) 0, 1));
            data.setInventory(inv);
            server.getPlayerDatabase().updateData(data);
        }
        if(!data.getUuid().toString().equals(uuid.toString()))
            server.getLogger().warning("[Loading data] UUID does not match: " + data.getUuid() + ", " + uuid);
        setPosition(data.getPosition());
        setHealth(data.getHealth());
        gamemode = data.getGamemode();
        inventory = data.getInventory();
    }

    @Override
    protected void initEntity(){
        setEntityID(server.getNextEntityID());
        super.initEntity();
    }

    /**
     * Sends a <code>Response</code> to the client. If they are available please use API methods.
     *
     * @param r The Response to be sent.
     */
    public void sendResponse(Response r){
        protocol.sendResponse(r, this);
    }
    
    @Override
    public void sendMessage(String s){
        protocol.sendImmediateResponse(new ChatResponse(s), this);
    }
    
    @Override
    public void sendMessage(ChatResponse.ChatTranslation translation) {
		ChatResponse r = new ChatResponse("");
		r.translation = translation;
		protocol.sendImmediateResponse(r, this);
	}

    public void sendPopup(String s){
        protocol.sendImmediateResponse(new PopupResponse(s), this);
    }

    /**
     * Handles a Request from the client.
     *
     * @param request The request from the client
     */
    public void handleRequest(Request request){
        if(request instanceof LoginRequest){
            LoginRequest lr = (LoginRequest) request;
            startLogin = System.currentTimeMillis();
            username = lr.username;
            uuid = lr.uuid;
            clientID = (int) lr.clientId;
            skin = lr.skin;
            slim = lr.slim;

            setName(username);
            displayName = username;

            loadPlayerData();

            LoginResponse response = new LoginResponse(true, gamemode, getHealth(), getPosition().getX(), getPosition().getY(), getPosition().getZ());
            PlayerLoginEvent ple = new PlayerLoginEvent(this);
            EventExecutor.throwEvent(ple);
            if(!ple.isCancelled()) {
                if(server.getPlayers().size() > server.getMaxPlayers()){
                    response.loginAllowed = false;
                    response.loginNotAllowedReason = "redstonelamp.loginFailed.serverFull";
                    protocol.sendImmediateResponse(response, this);
                    close("", "redstonelamp.loginFailed.serverFull", false);
                    return;
                }
                server.getPlayers().stream()
                        .filter(player -> player != this && player.getName().equals(getName()))
                        .forEach(player -> player.close("redstonelamp.translation.player.left", "!redstonelamp.translation.player.loggedInFromOtherLocation", true));
                sendResponse(response);
                initEntity();
                server.getLogger().info(username + "[" + address + "] logged in with entity ID " + getEntityID() + " in level \"" + getPosition().getLevel().getName() + "\""
                                + " at position [x: " + getPosition().getX() + ", y: " + getPosition().getY() + ", z: " + getPosition().getZ() + "]"
                );
            } else
                close("", "Disconnected from server", false);
        }else if(request instanceof ChunkRequest){
            ChunkRequestEvent cre = new ChunkRequestEvent(this);
            EventExecutor.throwEvent(cre);
            ChunkRequest r = (ChunkRequest) request;
            ChunkResponse response = new ChunkResponse(getPosition().getLevel().getChunkAt(r.position));
            sendResponse(response);
        }else if(request instanceof SpawnRequest){
            PlayerSpawnEvent pse = new PlayerSpawnEvent(this);
            EventExecutor.throwEvent(pse);
            SpawnResponse sr = new SpawnResponse(getPosition());
            sendResponse(sr);
            TeleportResponse tr = new TeleportResponse(getPosition(), true);
            sendResponse(tr);

            server.getPlayers().stream().filter(player -> player != this && player.getPosition().getLevel() == getPosition().getLevel()).forEach(player -> {
                spawnTo(player);
                player.spawnTo(this);
            });

            server.getLogger().debug("Player " + username + " spawned (took " + (System.currentTimeMillis() - startLogin) + " ms)");
            spawned = true;
            server.broadcastMessage(new ChatResponse.ChatTranslation(TextFormat.YELLOW+"redstonelamp.translation.player.joined", new String[]{username}));
        }else if(request instanceof ChatRequest){
            ChatRequest cr = (ChatRequest) request;
            if(cr.message.startsWith("/")) {
                try {
                    server.getCommandManager().executeCommand(cr.message, this);
                } catch (CommandException e) {
                    e.printStackTrace();
                }
            } else {
                PlayerChatEvent pce = new PlayerChatEvent(this, cr.message);
                EventExecutor.throwEvent(pce);
                if(!pce.isCancelled())
                    server.broadcastMessage("<" + username + "> " + cr.message);
            }
        }else if(request instanceof PlayerMoveRequest){
            PlayerMoveEvent pme = new PlayerMoveEvent(this);
            PlayerMoveRequest pmr = (PlayerMoveRequest) request;
            EventExecutor.throwEvent(pme);
            if(!pme.isCancelled()) {
                if(gamemode == 1){
                    PlayerMoveResponse response = new PlayerMoveResponse(getEntityID(), pmr.position, pmr.onGround);
                    setPosition(pmr.position);
                    server.broadcastResponse(server.getPlayers().stream().filter(player -> player != this), response);
                } //TODO: Check movement if in survival
            }
        }else if(request instanceof PlayerEquipmentRequest){
            PlayerEquipmentChangeEvent pece = new PlayerEquipmentChangeEvent(this);
            PlayerEquipmentRequest er = (PlayerEquipmentRequest) request;
            EventExecutor.throwEvent(pece);
            if(!pece.isCancelled()) {
                PlayerEquipmentResponse response = new PlayerEquipmentResponse(er.item);
                server.broadcastResponse(server.getPlayers().stream().filter(player -> player != this), response);
            }
        }else if(request instanceof AnimateRequest){
            PlayerAnimateEvent pae = new PlayerAnimateEvent(this);
            AnimateRequest ar = (AnimateRequest) request;
            EventExecutor.throwEvent(pae);
            if(!pae.isCancelled()) {
                AnimateResponse response = new AnimateResponse(ar.actionType, getEntityID());
                server.broadcastResponse(server.getPlayers().stream().filter(player -> player != this), response);
            }
        }else if(request instanceof BlockPlaceRequest){
            BlockPlaceEvent bpe = new BlockPlaceEvent();
            BlockPlaceRequest bpr = (BlockPlaceRequest) request;
            EventExecutor.throwEvent(bpe);
            if(!bpe.isCancelled()) {
                //System.out.println("Request to place at: " + bpr.blockPosition);
                BlockPlaceResponse response = new BlockPlaceResponse(bpr.block, BlockPosition.fromVector3(bpr.blockPosition, getPosition().getLevel()));
                if(!getPosition().getLevel().isChunkLoaded(new ChunkPosition(bpr.blockPosition.getX() / 16, bpr.blockPosition.getZ() / 16))){
                    server.getLogger().warning(username + " attempted to place block in an unloaded chunk");
                    sendMessage("Attempted to place block in unloaded chunk, hacking?");
                    response.block = new Block(0, (short) 0, 1); //AIR
                    response.placeAllowed = false;
                    sendResponse(response);
                    return;
                }
                //TODO: Check last place time as to prevent speed placing
                getPosition().getLevel().setBlock(BlockPosition.fromVector3(bpr.blockPosition, getPosition().getLevel()), bpr.block);
            }
        }else if(request instanceof RemoveBlockRequest){
            BlockBreakEvent bbe = new BlockBreakEvent();
            RemoveBlockRequest rbr = (RemoveBlockRequest) request;
            EventExecutor.throwEvent(bbe);
            if(!bbe.isCancelled()) {
                //System.out.println("Request to remove at: " + rbr.position);
                RemoveBlockResponse response = new RemoveBlockResponse(rbr.position);
                if(!getPosition().getLevel().isChunkLoaded(new ChunkPosition(rbr.position.getX() / 16, rbr.position.getZ() / 16))){
                    server.getLogger().warning(username + " attempted to remove block in an unloaded chunk");
                    sendMessage("Attempted to remove block in unloaded chunk, hacking?");
                    sendBlockChange(getPosition().getLevel().getBlock(rbr.position), rbr.position);
                    return;
                }
                //TODO: Check last place time as to prevent speed breaking
                //getPosition().getLevel().removeBlock(rbr.position);
                getPosition().getLevel().setBlock(rbr.position, new Block(0, (short) 0, 1));
            }
        } else if(request instanceof SetHeldItemRequest) {
            SetHeldItemRequest shir = (SetHeldItemRequest) request;
            inventory.setItemInHand(shir.item);
            inventory.setSelectedSlot(shir.inventorySlot);
            inventory.setItemInHandSlot(shir.hotbarSlot);
            server.broadcastResponse(server.getPlayers().stream().filter(player -> player != this), new SetHeldItemResponse(getEntityID(), inventory.getItemInHand(), inventory.getSelectedSlot(), inventory.getItemInHandSlot()));
        } else if(request instanceof SprintRequest) {
            boolean starting = ((SprintRequest) request).starting;
            if(!isSprinting() && starting) {
                sprinting = true;
            }
            if(isSprinting() && !starting) {
                sprinting = false;
            }
            server.broadcastResponse(new SprintResponse(starting, this));
        }
    }

    public void sendBlockChange(Block block, BlockPosition position){
        BlockPlaceResponse bpr = new BlockPlaceResponse(block, position);
        sendResponse(bpr);
    }

    /**
     * Notice to plugin developers: use Player.kick() instead.
     * <br>
     * Closes the connection to the player.
     * @param leaveMessage Leave message to be broadcasted in chat.
     * @param reason Reason displayed to the client. If sending a translation constant, first character must be "!"
     * @param notifyClient If the client should be sent a disconnect notification with the reason or not.
     */
    //TODO: Better solution for translated reasons
    public void close(String leaveMessage, String reason, boolean notifyClient){
        DisconnectResponse dr = new DisconnectResponse(reason);
        dr.notifyClient = notifyClient;

        protocol.sendImmediateResponse(dr, this);

        server.getPlayers().stream().filter(player -> player != this && player.getPosition().getLevel() == getPosition().getLevel()).forEach(this::despawnFrom);

        destroyEntity();

        server.closeSession(this);
        if(reason.startsWith("!")) { // Check if reason is a translation constant
            server.getLogger().info(username + "[" + identifier + "] logged out with reason: " + server.getTranslationManager().translateServerSide(new ChatResponse.ChatTranslation(reason.replaceAll("!", ""), new String[0])));
        } else {
            server.getLogger().info(username + "[" + identifier + "] logged out with reason: " + reason);
        }
        connected = false;
        protocol.close(this);

        PlayerDatabase.PlayerData data = new PlayerDatabase.PlayerData();
        data.setUuid(uuid);
        data.setPosition(getPosition());
        data.setHealth(getHealth());
        data.setGamemode(getGamemode());
        data.setInventory(new NBTPlayerInventory());
        server.getPlayerDatabase().updateData(data);
        server.savePlayerData();

        if(!leaveMessage.isEmpty()){
            switch (leaveMessage) {
                case "redstonelamp.translation.player.left":
            	    server.broadcastMessage(new ChatResponse.ChatTranslation(TextFormat.YELLOW+"redstonelamp.translation.player.left", new String[] {username}));
                    break;
                default:
                    server.broadcastMessage(username + leaveMessage);
                    break;
            }
        }
    }
    
    @Override
    public boolean hasOp() {
    	return true; // TODO: Get operators working
    }
    
    public String getDisplayName() {
    	return displayName;
    }
    
    public void setDisplayName(String displayName) {
    	this.displayName = displayName;
    }

    public String getUsername() {
        return username;
    }

    public Protocol getProtocol(){
        return protocol;
    }

    @Deprecated
    public String getIdentifier(){
        return identifier;
    }

    public SocketAddress getAddress(){
        return address;
    }

    public boolean isSpawned(){
        return spawned;
    }

    public int getGamemode(){
        return gamemode;
    }

    public byte[] getSkin(){
        return skin;
    }

    public boolean isSlim(){
        return slim;
    }

    public UUID getUuid(){
        return uuid;
    }

    public boolean isConnected(){
        return connected;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }
    
    public void addAttachment(Plugin plugin) {
    	permissionAttachments.put(plugin, new PermissionAttachment(this, plugin));
    }
    
    public PermissionAttachment getAttachment(Plugin plugin) {
    	return permissionAttachments.get(plugin);
    }
    
    public PermissionAttachment[] getAttachments() {
    	return permissionAttachments.values().toArray(new PermissionAttachment[permissionAttachments.size()]);
    }
    
    @Override
    public boolean hasPermission(String permission) {
    	ArrayList<String> permissions = new ArrayList<String>();
    	for(PermissionAttachment attachment : getAttachments()) {
    		for(Permission perm : attachment.getPermissions()) {
    			if(!permissions.contains(perm.toString()))
    				permissions.add(perm.toString());
    		}
    	}
    	if(hasOp())
    		for(Permission perm : OperatorPermissions.getPermissions(RedstoneLamp.SERVER.getConfig().getInt("op-permission-level")-1))
    			permissions.add(perm.toString());
    	return permissions.contains(permission);
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public boolean isSneaking() {
        return sneaking;
    }
}
