package redstonelamp.plugin.js;

import java.util.ArrayList;

import redstonelamp.RedstoneLamp;

public class JavaScriptManager {
	private ArrayList<JavaScriptAPI> api;
	
	public JavaScriptManager() {
		this.api = new ArrayList<JavaScriptAPI>();
	}
	
	public void addAPI(JavaScriptAPI api) {
		this.api.add(api);
		RedstoneLamp.getServerInstance().getLogger().debug("A JavaScript API with the variable name \"" + api.getClass().getSimpleName() + "\" has been added!");
	}
	
	public ArrayList<JavaScriptAPI> getAPIClasses() {
		return this.api;
	}
}
