package redstonelamp.plugin;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;

public class PluginPolicy extends Policy {
	public Permissions permissions = new Permissions();
	
	/**
	 * No available documentation
	 * 
	 * @param CodeSource
	 * @return PermissionCollection
	 */
	public PermissionCollection getPermissions(CodeSource source) {
		this.permissions.add(new AllPermission());
		return this.permissions;
	}
	
	/**
	 * Does nothing
	 */
	public void refresh() {
		
	}
}
