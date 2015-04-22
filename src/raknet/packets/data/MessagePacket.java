package raknet.packets.data;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.utils.Hex;

public class MessagePacket extends Packet {
	private String message;
	
	public MessagePacket(String s) {
		message = s;
	}
	
	public MessagePacket(DatagramPacket packet) {
		ByteBuffer b = ByteBuffer.wrap(packet.getData());
		System.out.println(Hex.getHexString(b.array(), true));
		b.get();
		short messageLength = b.getShort();
		message = Hex.bytesToString(b, messageLength);
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size = (message.length() + 3) * 8;
		ByteBuffer response = ByteBuffer.allocate(6 + message.length());
		response.put((byte) 0x00);
		response.putShort((short) size);
		
		response.put((byte) 0x85);
		response.putShort((short) message.length());
		response.put(message.getBytes());
		return response;
	}
	
	public ByteBuffer sendPacket(Player player) {
		
		ByteBuffer response = ByteBuffer.allocate(message.length() + 4 + 6);
		response.put((byte) 0x84);
		response.put(Hex.intToBytes(player.getPacketCount(), 3), 0, 3);
		
		int size = (message.length() + 3) * 8;
		response.put((byte) 0x00);
		response.putShort((short) size);
		
		response.put((byte) 0x85);
		response.putShort((short) message.length());
		response.put(message.getBytes());
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		h.addToQueue(getPacket());
	}
	
	public void processAll(PacketHandler h) {
		h.addToQueueForAll(getPacket());
	}
}
