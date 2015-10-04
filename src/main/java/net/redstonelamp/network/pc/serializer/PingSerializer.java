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
package net.redstonelamp.network.pc.serializer;

import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.redstonelamp.Server;
import net.redstonelamp.network.pc.PCNetworkConst;

public abstract class PingSerializer {
	
	@SuppressWarnings("unchecked")
	public static String getStatusResponse(Server server, String name) {
		JSONObject root = new JSONObject();

		JSONObject version = new JSONObject();
		version.put("name", PCNetworkConst.MC_VERSION);
		version.put("protocol", PCNetworkConst.MC_PROTOCOL);

		JSONObject players = new JSONObject();
		players.put("max", server.getMaxPlayers());
		players.put("online", server.getPlayers().size());
		// TODO: throw Event

		JSONArray sample = new JSONArray();
		for (int i = 0; i < server.getPlayers().size(); i++) {
			JSONObject player = new JSONObject();
			player.put("name", server.getPlayers().get(i));
			player.put("id", UUID.randomUUID());
			sample.add(i, player);
		}
		players.put("sample", sample);

		JSONObject description = new JSONObject();
		description.put("text", name);

		root.put("version", version);
		root.put("players", players);
		root.put("description", description);
		if (server.getServerIcon() != null)
			root.put("favicon", server.getServerIcon());

		return root.toJSONString();
	}
	
}
