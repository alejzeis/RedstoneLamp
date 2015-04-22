package raknet.packets.data;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class PlayerEquipmentPacket extends Packet {
	private int entityID;
	private short blockID, metadata;
	private Player player;
	
	public PlayerEquipmentPacket(DatagramPacket p, Player player) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		entityID = b.getInt();
		blockID = b.getShort();
		metadata = b.getShort();
		this.player = player;
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size = 9 * 8;
		ByteBuffer response = ByteBuffer.allocate(3 + 9);
		response.put((byte) 0x00);
		response.putShort((short) size);
		
		response.put((byte) 0x9f);
		response.putInt(entityID);
		response.putShort(blockID);
		response.putShort(metadata);
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		h.addToQueueForAll(getPacket());
		player.blockID = blockID;
		player.metadata = metadata;
		
	}
}
