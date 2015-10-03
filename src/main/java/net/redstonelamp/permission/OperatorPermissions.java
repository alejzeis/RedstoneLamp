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
package net.redstonelamp.permission;

import java.util.ArrayList;

/**
 * This class stores all of the default permissions for RedstoneLamp, and also
 * stores the permissions for all op levels (1-4)
 * 
 * @author RedstoneLamp Team
 */
public abstract class OperatorPermissions {

	private static final String[] OP_1 = {
		"net.redstonelamp.world.spawn"
	};
	
	private static final String[] OP_2 = {
		"net.redstonelamp.inventory.clear", "net.redstonelamp.world.difficulty",
		"net.redstonelamp.world.gamerule", "net.redstonelamp.world.commandblock",
		"net.redstonelamp.player.give", "net.redstonelamp.player.gamemode",
		"net.redstonelamp.player.tp", "net.redstonelamp.player.effect"
	};

	private static final String[] OP_3 = {
		"net.redstonelamp.player.op", "net.redstonelamp.player.deop",
		"net.redstonelamp.player.kick", "net.redstonelamp.player.ban",
		"net.redstonelamp.player.banip", "net.redstonelamp.player.pardon",
		"net.redstonelamp.player.pardonip"
	};

	private static final String[] OP_4 = {
		"net.redstonelamp.stop"
	};

	private static final String[][] OP_LEVELS = {
		OP_1, OP_2, OP_3, OP_4
	};

	public static Permission[] getPermissions(int level) {
		if (level > OP_LEVELS.length)
			return null;
		ArrayList<Permission> permissions = new ArrayList<Permission>();
		for (int i = 0; i < level + 1; i++)
			for (int j = 0; j < OP_LEVELS[i].length; j++)
				permissions.add(new Permission(OP_LEVELS[i][j]));
		return permissions.toArray(new Permission[permissions.size()]);
	}

}
