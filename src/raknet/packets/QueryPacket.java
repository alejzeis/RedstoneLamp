package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.RedstoneLamp;
import redstonelamp.utils.MinecraftPacket;

public class QueryPacket extends Packet {
	private byte[] magic = new byte[16];
	private long serverID;
	private long clientID;
	
	public QueryPacket(DatagramPacket packet, long serverID) {
		ByteBuffer b = ByteBuffer.wrap(packet.getData());
		if(!(b.get() == MinecraftPacket.QueryPacket))
			return;
		this.serverID = serverID;
		this.clientID = b.getLong();
		b.get(magic);
	}
	
	@Override
	public ByteBuffer getPacket() {
		String motd = "MCCPP;MINECON;" + RedstoneLamp.server.getServerName();
		ByteBuffer response = ByteBuffer.allocate(35 + motd.length());
		response.put((byte) MinecraftPacket.ServerQueryResponse);
		response.putLong(this.clientID);
		response.putLong(this.serverID);
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
