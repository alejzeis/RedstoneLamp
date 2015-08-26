/**
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
package net.redstonelamp.request;


public class ChatRequest extends Request{
	
	public static final byte TYPE_RAW = 0;
	public static final byte TYPE_CHAT = 1;
	public static final byte TYPE_TRANSLATION = 2;
	public static final byte TYPE_POPUP = 3;
	public static final byte TYPE_TIP = 4;
	
	public byte type;
	public String source;
	public String message;
	public String[] parameters;
	
	public ChatRequest(byte type) {
		this.type = type;
	}
	
	@Override
	public void execute() {
		// TODO?
	}
	
}
