package redstonelamp.auth.pc;

import redstonelamp.auth.AuthenticationAgent;
import redstonelamp.auth.AuthenticationManager;

import java.util.UUID;

/**
 * Authentication agent for PC
 *
 * @author jython234
 */
public class PCAuthAgent extends AuthenticationAgent{

    public PCAuthAgent(AuthenticationManager manager) {
        super(manager);
    }

    @Override
    public AuthenticationResponse authenticate(String username, Object... aditionalArgs) {
        AuthenticationResponse response = new AuthenticationResponse(true, "redstonelamp.authSuccess", UUID.nameUUIDFromBytes(username.getBytes()));
        if(getManager().getServer().getOnlinePlayers().size() > getManager().getServer().getMaxPlayers()){
            response = new AuthenticationResponse(false, "redstonelamp.authFailed.serverFull", null);
        }
        return response;
    }

    @Override
    public String getName() {
        return "An authentication agent for Minecraft: PC edition.";
    }
}
