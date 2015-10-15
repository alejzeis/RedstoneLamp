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
		"redstonelamp.command.world.spawn"
	};
	
	private static final String[] OP_2 = {
		"redstonelamp.command.inventory.clear", "redstonelamp.command.world.difficulty",
		"redstonelamp.command.world.gamerule", "redstonelamp.command.world.commandblock",
		"redstonelamp.command.player.give", "redstonelamp.command.player.gamemode",
		"redstonelamp.command.player.tp", "redstonelamp.command.player.effect"
	};

	private static final String[] OP_3 = {
		"redstonelamp.command.player.op", "redstonelamp.command.player.deop",
		"redstonelamp.command.player.kick", "redstonelamp.command.player.ban",
		"redstonelamp.command.player.banip", "redstonelamp.command.player.pardon",
		"redstonelamp.command.player.pardonip"
	};

	private static final String[] OP_4 = {
		"redstonelamp.command.stop"
	};

	private static final String[][] OP_LEVELS = {
		OP_1, OP_2, OP_3, OP_4
	};

	public static Permission[] getPermissions(int level) {
		while(level > OP_LEVELS.length)
			level--;
		ArrayList<Permission> permissions = new ArrayList<>();
		for (int i = 0; i < level + 1; i++)
			for (int j = 0; j < OP_LEVELS[i].length; j++)
				permissions.add(new Permission(OP_LEVELS[i][j]));
		return permissions.toArray(new Permission[permissions.size()]);
	}

}
