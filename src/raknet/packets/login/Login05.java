package raknet.packets.login;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.RedstoneLamp;

public class Login05 extends Packet {
	private byte packetType;
	private byte[] magic = new byte[16];
	private short mtuSize;
	private RedstoneLamp redstone;
	private long serverID;
	
	public Login05(DatagramPacket p, long serverID) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		packetType = b.get();
		if(packetType != 0x05) { return; }
		
		b.get(magic);
		mtuSize = (short) p.getLength();
		this.serverID = serverID;
	}
	
	public ByteBuffer getPacket() {
		int size = 28;
		ByteBuffer response = ByteBuffer.allocate(size);
		response.put((byte) 0x06);
		response.put(magic);
		response.putLong(serverID);
		response.put((byte) 0x00);
		response.putShort(mtuSize);
		return response;
	}
	
	public void process(PacketHandler handler) {
		handler.sendPacket(getPacket());
	}
}
