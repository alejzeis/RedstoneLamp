package net.redstonelamp.response;


/**
 * Represents a Response to a LoginRequest
 *
 * @author RedstoneLamp Team
 */
public class LoginResponse extends Response{
    public static final String DEFAULT_loginNotAllowedReason = "redstonelamp.loginFailed.noReason";

    public boolean loginAllowed;
    public String loginNotAllowedReason = DEFAULT_loginNotAllowedReason;

    public LoginResponse(boolean loginAllowed) {
        this.loginAllowed = loginAllowed;
    }
}
