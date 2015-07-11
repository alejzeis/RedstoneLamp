package redstonelamp.plugin;

public abstract class PluginBase implements Plugin {
	public void onLoad() {}
	
	public void onEnable() {}
	
	public boolean isEnabled() {
		return false;
	}
	
	public void onDisable() {}
	
	public boolean isDisabled() {
		return true;
	}
	
	//TODO: Other methods
}
