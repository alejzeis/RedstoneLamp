package raknet.packets.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class CurrentPlayersPacket extends Packet {
	private ArrayList<Player> players;
	private Player player;
	
	public CurrentPlayersPacket(ArrayList<Player> players, Player player) {
		this.players = players;
		this.player = player;
	}
	
	public ByteBuffer getPacket(Player p) {
		Player current = (Player) p;
		int size = (32 + current.name.length()) * 8;
		ByteBuffer response = ByteBuffer.allocate(35 + current.name.length());
		response.put((byte) 0x00);
		response.putShort((short) size);
		
		response.put((byte) 0x89);
		response.putLong(current.clientID);
		response.putShort((short) current.name.length());
		response.put(current.name.getBytes());
		response.putInt(current.entityID);
		response.putFloat(current.x);
		response.putFloat(current.y);
		response.putFloat(current.z);
		response.put((byte) 0x00);
		response.put((byte) 0x00);
		response.putShort((short) current.blockID);
		response.put((byte) 0x7f);
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		for(Player p : players) {
			if(!p.clientAddress.equals(player.clientAddress) && p.clientPort != player.clientPort && p.isConnected) {
				h.addToQueue(getPacket((Player) p));
			}
		}
		h.addToQueueForAll(getPacket(player));
	}
	
	public ByteBuffer getPacket() {
		return null;
	}
}
