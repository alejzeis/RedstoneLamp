package net.redstonelamp.event.server;

import java.net.SocketAddress;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;
import net.redstonelamp.network.pe.PacketType;

public class ServerReceivePacketEvent extends Event implements Cancellable {
	
	private boolean cancelled = false;
	private byte[] buffer;
	private final PacketType type;
	private final SocketAddress sender;
	
	public ServerReceivePacketEvent(byte[] buffer, PacketType type, SocketAddress sender) {
		this.buffer = buffer;
		this.type = type;
		this.sender = sender;
	}
	
	public byte[] getBuffer() {
		return buffer;
	}
	
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	
	public PacketType getType() {
		return type;
	}
	
	public SocketAddress getSender() {
		return sender;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
}
