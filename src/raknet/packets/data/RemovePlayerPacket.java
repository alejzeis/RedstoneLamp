package raknet.packets.data;

import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class RemovePlayerPacket extends Packet {
	
	private int x, z;
	private Player player;
	
	public RemovePlayerPacket(Player p) {
		player = p;
		
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size = 13 * 8;
		ByteBuffer response = ByteBuffer.allocate(3 + 13);
		response.put((byte) 0x00);
		response.putShort((short) size);
		
		response.put((byte) 0x8a);
		response.putInt(player.entityID);
		response.putLong(player.clientID);
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		h.addToQueueForAll(getPacket());
	}
}
