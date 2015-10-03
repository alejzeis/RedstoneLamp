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
/*
 * This file is part of RedstoneLamp
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
import java.util.HashMap;

import lombok.Getter;
import net.redstonelamp.Player;
import net.redstonelamp.plugin.Plugin;

/**
 * Represents a permission attachment hooked by a plugin
 * 
 * @author RedstoneLamp team
 */
public class PermissionAttachment {
	
	private final HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
	@Getter private final Player player;
	@Getter private final Plugin plugin;
	
	public PermissionAttachment(Player player, Plugin plugin) {
		this.player = player;
		this.plugin = plugin;
	}
	
	public void setPermission(String permission, boolean value) {
		this.setPermission(permission, value);
	}
	
	public void setPermission(Permission permission, boolean value) {
		this.setPermission(permission.toString(), value);
	}
	
	public void unsetPermission(String permission) {
		permissions.remove(permission);
	}
	
	public void unsetPermission(Permission permission) {
		permissions.remove(permission.toString());
	}
	
	public Permission[] getPermissions() {
		ArrayList<Permission> perms = new ArrayList<Permission>();
		for(String perm : permissions.keySet())
			perms.add(new Permission(perm));
		return perms.toArray(new Permission[permissions.size()]);
	}
	
	public boolean registeredPermission(String permission) {
		return permissions.values().contains(permission);
	}
	
	public boolean registeredPermission(Permission permission) {
		return this.registeredPermission(permission.toString());
	}
	
	public boolean hasPermission(String permission) {
		if(permissions.get(permission) == null)
			return false;
		System.out.println(permissions.get(permission).booleanValue());
		return permissions.get(permission).booleanValue();
	}
	
	public boolean hasPermission(Permission permission) {
		return this.hasPermission(permission.toString());
	}
	
}
