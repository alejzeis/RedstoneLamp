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
 * This "interface" contains the MCPC network constants.
 *
 * @author RedstoneLamp Team
 */
public interface PCNetworkConst{
    /**
     * The Minecraft version this software implements.
     */
    public static final String MC_VERSION = "v1.8.8";

    /**
     * The Minecraft network protocol version this software implements.
     */
    public static final int MC_PROTOCOL = 47;

    //Handshake packets
    public final static int HANDSHAKE_HANDSHAKE = 0x00;

    //Status packets
    public final static int STATUS_REQUEST = 0x00;
    public final static int STATUS_RESPONSE = 0x00;
    public final static int STATUS_PING = 0x01;
    public final static int STATUS_PONG = 0x01;

    //Login packets
    public final static int LOGIN_LOGIN_START = 0x00;
    public final static int LOGIN_ENCRYPTION_REQUEST = 0x01;
    public final static int LOGIN_ENCRYPTION_RESPONSE = 0x01;
    public final static int LOGIN_DISCONNECT = 0x00;
    public final static int LOGIN_LOGIN_SUCCESS = 0x02;
    public final static int LOGIN_SET_COMPRESSION = 0x03;

    //Play packets
    public final static int PLAY_JOIN_GAME = 0x01;

}
