package raknet.packets.data;

import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.utils.Hex;

public class DisconnectPacket extends Packet {
	public DisconnectPacket() {}
	
	@Override
	public ByteBuffer getPacket() {
		return null;
	}
	
	public ByteBuffer sendPacket(Player player) {
		
		ByteBuffer response = ByteBuffer.allocate(8);
		response.put((byte) 0x84);
		response.put(Hex.intToBytes(player.getPacketCount(), 3), 0, 3);
		
		int size = 1 * 8;
		response.put((byte) 0x00);
		response.putShort((short) size);
		
		response.put((byte) 0x15);
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		//		h.addToQueue(getPacket());
	}
}
