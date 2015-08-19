package redstonelamp.auth;

import java.util.UUID;

/**
 * Base class for Authentication agent
 *
 * @author jython234
 */
public abstract class AuthenticationAgent {
    private AuthenticationManager manager;

    public AuthenticationAgent(AuthenticationManager manager){
        this.manager = manager;
    }

    public abstract AuthenticationResponse authenticate(String username, Object... aditionalArgs);

    public abstract String getName();

    @Override
    public String toString() {
        return "AuthenticationAgent: "+getName();
    }

    public AuthenticationManager getManager() {
        return manager;
    }

    public static class AuthenticationResponse {
        public final boolean allowed;
        public final String reason;
        public final UUID uuid;

        public AuthenticationResponse(boolean allowed, String reason, UUID uuid) {
            this.allowed = allowed;
            this.reason = reason;
            this.uuid = uuid;
        }

        @Override
        public String toString() {
            return "AuthenticationResponse: {allowed: "+allowed+", reason: "+reason+", UUID: "+uuid.toString()+"}";
        }
    }
}
