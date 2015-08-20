package redstonelamp;

import redstonelamp.event.Event;
import redstonelamp.event.Listener;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerQuitEvent;

import java.io.IOException;

/**
 * Internal Listener for the Server.
 *
 * @author jython234
 */
public class InternalListener implements Listener{
    private Server server;

    public InternalListener(Server server){
        this.server = server;
    }

    public void onPlayerQuit(PlayerQuitEvent evt){
        server.getNetwork().setName(server.getMotd()); //Update the player list

        try {
            server.getPlayerDatabase().save(server.getPlayerDatbaseLocation());
        } catch (IOException e) {
            server.getLogger().warning("Failed to save PlayerDatabase, java.io.IOException: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void onPlayerJoin(PlayerJoinEvent evt){
        server.getNetwork().setName(server.getMotd()); //Update the player list
    }

    @Override
    public void onEvent(Event event) {
    }
}
