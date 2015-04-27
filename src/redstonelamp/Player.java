package redstonelamp;

import java.net.InetAddress;

public class Player {
	public InetAddress clientAddress;
	public int clientPort;
	public int packetCount;
	public int dataCount;
	public String name;
	public int entityID;
	public float x;
	public float y;
	public float z;
	public float yaw;
	public float pitch;
	public long clientID;
	public short blockID;
	public short metadata;
	public boolean isConnected;
	
	public Player(InetAddress i, int p, int eid, long cid) {
		clientAddress = i;
		clientPort = p;
		packetCount = 0;
		dataCount = 0;
		entityID = eid;
		clientID = cid;
		blockID = 0;
		metadata = 0;
		yaw = 0;
		pitch = 0;
		isConnected = false;
	}
	
	/**
	 * Returns the Packet Count for a player
	 * 
	 * @return int
	 */
	public int getPacketCount() {
		return packetCount++;
	}
	
	/**
	 * Returns the Data Count for a player
	 * 
	 * @return int
	 */
	public int getDataCount() {
		return dataCount++;
	}
	
	/**
	 * Sets a players Client ID
	 * @param long cid
	 */
	public void setClientID(long cid) {
		clientID = cid;
	}
}
