package raknet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import raknet.packets.QueryPacket;
import raknet.packets.StartLoginPacket;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;

public class PacketHandler implements Runnable {
	public RedstoneLamp server;
	private Server network;
	private DatagramPacket packet;
	private InetAddress clientAddress;
	private int clientPort;
	
	public PacketHandler(RedstoneLamp redstone, Server server, DatagramPacket packet) {
		this.server = redstone;
		this.network = server;
		this.packet = packet;
		this.clientAddress = packet.getAddress();
		this.clientPort = packet.getPort();
	}
	
	public void run() {
		Packet pkt = null;
		if(!(packet == null)) {
			int packetType = (packet.getData()[0] & 0xFF);
			int packetSize = packet.getData().length;
			switch(packetType) {
				case MinecraftPacket.ID_CONNECTED_PING_OPEN_CONNECTIONS:
					pkt = new QueryPacket(packet, network.serverID);
				break;
				
				case MinecraftPacket.ID_OPEN_CONNECTION_REQUEST_1:
					pkt = new StartLoginPacket(packet, network.serverID, clientAddress, clientPort);
				break;
				
				default:
					this.network.getLogger().info("Unknown packet from: " + clientAddress + ":" + clientPort);
				break;
			}
			
			if(!(pkt == null))
				pkt.process(this);
		}
	}

	public void sendPacket(ByteBuffer d) {
		DatagramPacket p = new DatagramPacket(d.array(), d.capacity());
		p.setAddress(clientAddress);
		p.setPort(clientPort);
		try {
			network.socket.send(p);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
