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
package net.redstonelamp.network.pc;

/**
 * Represents different states of the Minecraft protocol
 *
 * @author RedstoneLamp Team
 */
public enum ProtocolState{
    /**
     * The Handshake state is the first state when a client connects.
     * Currently there is only one packet sent in this state.
     */
    STATE_HANDSHAKE,
    /**
     * The Status state is the state when the client pings the server for the
     * MOTD.
     */
    STATE_STATUS,
    /**
     * The Login state is when the client and server authenticate.
     */
    STATE_LOGIN,
    /**
     * The Play state is the final state in which all game packets are sent.
     */
    STATE_PLAY
}
