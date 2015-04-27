package redstonelamp.entity;

import java.net.InetSocketAddress;

public interface Player {
	/**
	 * Returns the players display name
	 * 
	 * @return String
	 */
	public String getDisplayName();
	
	/**
	 * Sets the players display name
	 * 
	 * @param String name
	 */
	public void setDisplayName(String name);
	
	/**
	 * Returns the players name in the player list
	 * 
	 * @return String
	 */
	public String getPlayerListName();
	
	/**
	 * Sets the players name in the player list
	 * 
	 * @param String name
	 */
	public void setPlayerListName(String name);
	
	/**
	 * Sets the location for the compass to find
	 * 
	 * @param Location location
	 */
	public void setCompassTarget(Location location);
	
	/**
	 * Returns the location in which the compass is looking for
	 * 
	 * @return Location
	 */
	public Location getCompassTarget();
	
	/**
	 * Returns the players address
	 * 
	 * @return InetSocketAddress
	 */
	public InetSocketAddress getAddress();
	
	/**
	 * Sends a raw message to the player
	 * 
	 * @param String message
	 */
	public void sendRawMessage(String message);
	
	/**
	 * Kicks the player from the Server
	 * 
	 * @param String message
	 */
	public void kickPlayer(String message);
	
	public void chat(String msg);
	
	/**
	 * Issues a command as the player
	 * 
	 * @param String command
	 * @return boolean
	 */
	public boolean performCommand(String command);
	
	/**
	 * Returns true if the player is sneaking
	 * 
	 * @return boolean
	 */
	public boolean isSneaking();
	
	/**
	 * Sets the players sneaking status
	 * 
	 * @param boolean sneak
	 */
	public void setSneaking(boolean sneak);
	
	/**
	 * Returns true if the player is sprinting
	 * 
	 * @return boolean
	 */
	public boolean isSprinting();
	
	/**
	 * Sets the players sprinting status
	 * 
	 * @param boolean sprinting
	 */
	public void setSprinting(boolean sprinting);
	
	/**
	 * Saves data for the player
	 */
	public void saveData();
	
	/**
	 * Loads data for the player
	 */
	public void loadData();
	
	/**
	 * Sets the sleeping ignored status
	 * 
	 * @param boolean isSleeping
	 */
	public void setSleepingIgnored(boolean isSleeping);
	
	/**
	 * Returns true if sleeping is ignored
	 * 
	 * @return boolean
	 */
	public boolean isSleepingIgnored();
	
	/**
	 * Plays a note in a certain location
	 * 
	 * @param Location location
	 * @param byte instrument
	 * @param byte note
	 */
	@Deprecated
	public void playNote(Location loc, byte instrument, byte note);
	
	/**
	 * Gets the health scale
	 * 
	 * @return double
	 */
	public double getHealthScale();
}
