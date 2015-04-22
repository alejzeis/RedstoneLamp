package raknet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

import raknet.packets.login.*;
import raknet.packets.data.*;
import raknet.query.QueryPacket;
import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.utils.Hex;
import redstonelamp.utils.MinecraftPacket;

public class PacketHandler implements Runnable {
	public RedstoneLamp server;
	private Server network;
	private DatagramPacket packet;
	private InetAddress clientAddress;
	private int clientPort;
	private Queue<DatagramPacket> splitPackets = new LinkedList();
	private Queue<ByteBuffer> queuePackets = new LinkedList<ByteBuffer>();
	private Queue<ByteBuffer> queuePacketsToAll = new LinkedList<ByteBuffer>();
	private int queueDataSize;
	private int queueDataSizeToAll;
	
	public PacketHandler(RedstoneLamp redstone, Server server, DatagramPacket packet) {
		this.server = redstone;
		this.network = server;
		this.packet = packet;
		this.clientAddress = packet.getAddress();
		this.clientPort = packet.getPort();
	}
	
	public void run() {
		if(packet != null) {
			int packetType = (packet.getData()[0] & 0xFF);
			int packetSize = packet.getData().length;
			switch(packetType) {
				case MinecraftPacket.ID_UNCONNECTED_PING_OPEN_CONNECTIONS_1:
				case MinecraftPacket.ID_OPEN_CONNECTION_REQUEST_1:
				case MinecraftPacket.ID_OPEN_CONNECTION_REPLY_2:
					loginPacketHandler();
				break;
				
				case MinecraftPacket.NACK:
					this.network.getLogger().dev("NACK from: " + clientAddress + ":" + clientPort + " with type: " + Integer.toHexString(packetType) + " and with size: " + packetSize);
				break;
				
				case MinecraftPacket.ACK:
					this.network.getLogger().dev("ACK from: " + clientAddress + ":" + clientPort + " with type: " + Integer.toHexString(packetType) + " and with size: " + packetSize);
				break;
				
				case MinecraftPacket.CustomPacket_5:
				case MinecraftPacket.CustomPacket_6:
				case MinecraftPacket.CustomPacket_7:
				case MinecraftPacket.CustomPacket_8:
				case MinecraftPacket.CustomPacket_9:
				case MinecraftPacket.CustomPacket_10:
				case MinecraftPacket.CustomPacket_11:
				case MinecraftPacket.CustomPacket_12:
				case MinecraftPacket.CustomPacket_13:
				case MinecraftPacket.CustomPacket_14:
				case MinecraftPacket.CustomPacket_15:
				case MinecraftPacket.CustomPacket_16:
					this.network.getLogger().dev("DataPacket from: " + clientAddress + ":" + clientPort + " with type: " + Integer.toHexString(packetType) + " and with size: " + packetSize);
					dataPacketHandler();
				break;
				
				case MinecraftPacket.ID_CONNECTED_PING_OPEN_CONNECTIONS:
					this.network.getLogger().dev("QueryPacket from: " + clientAddress + ":" + clientPort + " with type: " + Integer.toHexString(packetType) + " and with size: " + packetSize);
					new QueryPacket();
				break;
				
				default:
					this.network.getLogger().dev("Unhandled packet: " + packetType);
				break;
			}
		}
	}
	
	private void loginPacketHandler() {
		int packetType = (packet.getData()[0] & 0xFF);
		Packet pkt = null;
		switch(packetType) {
			case MinecraftPacket.ID_UNCONNECTED_PING_OPEN_CONNECTIONS_1:
				pkt = new Login02(packet, network.serverID);
			break;
			
			case MinecraftPacket.ID_OPEN_CONNECTION_REQUEST_1:
				pkt = new Login05(packet, network.serverID);
			break;
			
			case MinecraftPacket.ID_OPEN_CONNECTION_REPLY_2:
				pkt = new Login07(packet, network.serverID, server);
			break;
			
			default:
				this.network.getLogger().error("Unable to handle login packet!");
			break;
		}
		
		if(pkt != null) {
			pkt.process(this);
		}
	}
	
	private void dataPacketHandler() {
		splitDataPacket();
		Player player = currentPlayer();
		while(splitPackets.size() > 0) {
			DatagramPacket p = splitPackets.poll();
			
			int mcpeID = (p.getData()[0] & 0xFF);
			Packet pkt = null;
			
			switch(mcpeID) {
				case MinecraftPacket.PingPacket:
					pkt = new Data00(network.start, player);
				break;
				
				case MinecraftPacket.PongPacket:
				break;
				
				case MinecraftPacket.ClientConnect:
					pkt = new Data09(p, player, network.serverID);
				break;
				
				case MinecraftPacket.ClientHandshake:
					pkt = new StartGamePacket(player);
				break;
				
				case MinecraftPacket.ClientCancelConnect:
					pkt = new RemovePlayerPacket(player);
					server.removePlayer(packet.getAddress(), packet.getPort());
				break;
				
				case MinecraftPacket.LoginPacket:
					pkt = new Data82(p, player);
				break;
				
				case MinecraftPacket.ReadyPacket:
					if(!player.isConnected) {
						pkt = new MessagePacket("Hello, world!");
						player.isConnected = true;
						if(server.players.size() > 0) {
							new CurrentPlayersPacket(server.players, player).process(this);
						}
					}
				break;
				
				case MinecraftPacket.MessagePacket:
					new MessagePacket(p).processAll(this);
				break;
				
				case MinecraftPacket.MovePlayerPacket:
					pkt = new MovePlayerPacket(p, player);
				break;
				
				case MinecraftPacket.RemoveBlockPacket:
					pkt = new RemoveBlockPacket(p);
				break;
				
				case MinecraftPacket.RequestChunkPacket:
					pkt = new RequestChunkPacket(p);
				break;
				
				case MinecraftPacket.PlayerEquipmentPacket:
					pkt = new PlayerEquipmentPacket(p, player);
				break;
				
				case MinecraftPacket.PlayerArmorEquipmentPacket:
					pkt = new MessagePacket("Die DIe DIE!!!!");
				break;
				
				case MinecraftPacket.InteractPacket:
					pkt = new UseItemPacket(p, player);
				break;
				
				case MinecraftPacket.SetEntityMotionPacket:
					pkt = new AnimatePacket(p);
				break;
				
				default:
					System.out.println((new StringBuilder()).append("unknown: ").append(Integer.toHexString(mcpeID)).append(" -> ").append(Hex.getHexString(p.getData(), true)).append(" Size: ").append(p.getLength()).toString());
				break;
			}
			
			if(pkt != null) {
				pkt.process(this);
			}
		}
		
		if(queuePackets.size() > 0) {
			ByteBuffer b1 = ByteBuffer.allocate(queueDataSize + 4);
			b1.put((byte) 0x84);
			b1.put(Hex.intToBytes(player.getPacketCount(), 3), 0, 3);
			while(queuePackets.size() > 0) {
				b1.put(queuePackets.poll().array());
			}
			this.network.getLogger().debug("Send: " + Hex.getHexString(b1.array(), true));
			sendPacket(b1);
		}
		
		if(queuePacketsToAll.size() > 0) {
			for(Player p : server.players) {
				if(!p.clientAddress.equals(clientAddress) && p.clientPort != clientPort) {
					sendPacketToAll(queuePacketsToAll, p);
				}
			}
			queuePacketsToAll.clear();
		}
	}
	
	private Player currentPlayer() {
		for(Player p : server.players) {
			if(p.clientAddress.equals(clientAddress) && p.clientPort == clientPort) { return p; }
		}
		return null;
	}
	
	private void splitDataPacket() {
		ByteBuffer b = ByteBuffer.wrap(packet.getData());
		b.get();
		byte[] count = new byte[3];
		b.get(count); // TODO: Send ack
		sendACK(count);
		
		int len = packet.getLength() - 4;
		byte[] buffer = new byte[len];
		b.get(buffer);
		
		ByteBuffer data = ByteBuffer.wrap(buffer);
		int i = 0;
		int length = 0;
		while(i < buffer.length) {
			if(buffer[i] == 0x00) {
				length = (data.getShort(i + 1) / 8);// + 3;
				i += 3;
			} else if(buffer[i] == 0x40) {
				length = (data.getShort(i + 1) / 8);// + 6;
				i += 6;
			} else if(buffer[i] == 0x60) {
				length = (data.getShort(i + 1) / 8);// + 10;
				i += 10;
			}
			data.position(i);
			byte[] c = new byte[length];
			data.get(c);
			// System.out.println((new
			// StringBuilder()).append("split: ").append(Hex.getHexString(buffer[i])).append(" -> ").append(Hex.getHexString(c,
			// true)).append(" Size: ").append(length).toString());
			DatagramPacket split = new DatagramPacket(c, c.length);
			splitPackets.add(split);
			i += length;
		}
	}
	
	public void sendPacket(ByteBuffer d) {
		// System.out.println("Send: " + Hex.getHexString(d.array(), true));
		DatagramPacket p = new DatagramPacket(d.array(), d.capacity());
		p.setAddress(clientAddress);
		p.setPort(clientPort);
		try {
			network.socket.send(p);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPacketToAll(Queue<ByteBuffer> q, Player player) {
		ByteBuffer b1 = ByteBuffer.allocate(queueDataSizeToAll + 4);
		b1.put((byte) 0x84);
		b1.put(Hex.intToBytes(player.getPacketCount(), 3), 0, 3);
		
		java.util.Iterator<ByteBuffer> it = q.iterator();
		while(it.hasNext()) {
			b1.put(it.next().array());
		}
		
		// System.out.println("PacketToAll: " + Hex.getHexString(b1.array(),
		// true));
		
		DatagramPacket p = new DatagramPacket(b1.array(), b1.capacity());
		p.setAddress(player.clientAddress);
		p.setPort(player.clientPort);
		try {
			network.socket.send(p);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendACK(byte[] count) {
		ByteBuffer r = ByteBuffer.allocate(7);
		r.put((byte) 0xc0);
		r.putShort((short) 1);
		r.put((byte) 0x01);
		r.put(count);
		
		DatagramPacket p = new DatagramPacket(r.array(), r.capacity());
		p.setAddress(clientAddress);
		p.setPort(clientPort);
		try {
			network.socket.send(p);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addToQueue(ByteBuffer b) {
		queuePackets.add(b);
		queueDataSize += b.capacity();
	}
	
	public void addToQueueForAll(ByteBuffer b) {
		queuePacketsToAll.add(b);
		queueDataSizeToAll += b.capacity();
	}
	
	private void ChatHandle() {
		new MessagePacket(packet).processAll(this);
	}
}
