package redstonelamp;

import redstonelamp.level.location.Location;
import redstonelamp.network.packet.DataPacket;
import redstonelamp.utils.Skin;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Template for a player.
 */
public interface Player {
    void handleDataPacket(DataPacket packet);
    void sendDataPacket(DataPacket packet);
    void sendDirectDataPacket(DataPacket packet);

    Location getLocation();

    /**
     * Kicks the player from the server
     *
     * @param reason The reason for the kick.
     * @param admin If this kick was by an admin.
     * @return If the client was kicked.
     */
    boolean kick(String reason, boolean admin);

    /**
     * INTERNAL METHOD, please use <code>Player.kick</code> instead.
     *
     * @param message
     * @param reason
     * @param notifyClient
     */
    void close(String message, String reason, boolean notifyClient);

    /**
     * Returns the players identifier
     *
     * @return String
     */
     String getIdentifier();

    /**
     * Returns the players IP Address
     *
     * @return InetSocketAddress
     */
     InetSocketAddress getAddress();
     
     /**
      * Returns the players server
      * 
      * @return Server
      */
     Server getServer();

    /**
     * Returns true if the player is online
     *
     * @return boolean
     */
     boolean isConnected();

    /**
     * Returns true if the player is logged in
     *
     * @return boolean
     */
     boolean isLoggedIn();

    /**
     * Returns the players username
     *
     * @return String
     */
     String getName();

    /**
     * Returns the player's skin.
     *
     * @return String
     */
     Skin getSkin();

    /**
     * Returns the players UUID
     *
     * @return UUID
     */
     UUID getUUID();

    /**
     * Sends a message to the player
     *
     * @param message The message to be sent to the player.
     */
     void sendMessage(String message);
     
     void sendPopup(String message);
     
     void sendTip(String message);

    /**
     * Returns true if the player is an Operator
     *
     * @return boolean
     */
     boolean isOp();
     
     /**
      * Returns the "players" folder
      * 
      * @return File
      */
     default File getDataFolder() {
 		File dataFolder = new File("./players/");
 		if(!dataFolder.isDirectory())
 			dataFolder.mkdirs();
 		return dataFolder;
     }
     
	String getDisplayName();
	
	void setDisplayName(String name);

    long getId();

    void spawnTo(Player player);

    void despawnFrom(Player player);
}
