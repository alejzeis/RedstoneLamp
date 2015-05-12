package raknet.packets;

import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.utils.Hex;

public class DisconnectPacket extends Packet {

	private Player player;
	
	public DisconnectPacket(Player player) {
		this.player = player;
	}
	
	
	@Override
	public ByteBuffer getPacket() {
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
		h.sendPacket(getPacket());
	}

}
