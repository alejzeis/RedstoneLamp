package net.redstonelamp.script;

import lombok.Getter;
import net.redstonelamp.Server;
import net.redstonelamp.ui.Logger;

public class DefaultScriptAPI implements ScriptAPI {
    @Getter private Server server;
    @Getter private Logger logger;
    
    public DefaultScriptAPI(Server server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }
}
