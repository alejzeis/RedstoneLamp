/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.event.server;

import java.net.SocketAddress;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;
import net.redstonelamp.network.PacketType;

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
