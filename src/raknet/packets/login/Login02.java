package raknet.packets.login;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.RedstoneLamp;

public class Login02 extends Packet {
	private byte packetType;
	private long clientID;
	private byte[] magic = new byte[16];
	private long serverID;
	
	public Login02(DatagramPacket p, long serverID) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		packetType = b.get();
		if(packetType != 0x02) { return; }
		clientID = b.getLong();
		b.get(magic);
		
		this.serverID = serverID;
	}
	
	@Override
	public ByteBuffer getPacket() {
		String motd = RedstoneLamp.server.getMOTD();
		int size = 35 + motd.length();
		ByteBuffer response = ByteBuffer.allocate(size);
		response.put((byte) 0x1c);
		response.putLong(clientID);
		response.putLong(serverID);
		response.put(magic);
		response.putShort((short) motd.length());
		response.put(motd.getBytes());
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}
}
