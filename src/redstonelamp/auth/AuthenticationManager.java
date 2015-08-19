package redstonelamp.auth;

import redstonelamp.Server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manager for different AuthenticationAgents.
 *
 * @author jython234
 */
public class AuthenticationManager {
    private Server server;
    private List<AuthenticationAgent> agents;

    public AuthenticationManager(Server server){
        this.server = server;
        this.agents = new CopyOnWriteArrayList<>();
    }

    public synchronized void registerAuthenticationAgent(AuthenticationAgent agent){
        agents.add(agent);
        server.getLogger().debug("Registered AuthenticationAgent: "+agent.getName());
    }

    public synchronized AuthenticationAgent getAgent(Class<? extends AuthenticationAgent> clazz){
        for(AuthenticationAgent agent: agents){
            if(agent.getClass().getName().equals(clazz.getName())){
                return agent;
            }
        }
        return null;
    }

    public Server getServer() {
        return server;
    }
}
