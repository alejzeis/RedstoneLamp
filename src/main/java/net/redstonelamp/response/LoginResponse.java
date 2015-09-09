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
package net.redstonelamp.response;

/**
 * Represents a Response to a LoginRequest
 *
 * @author RedstoneLamp Team
 */
public class LoginResponse extends Response{
    public static final String DEFAULT_loginNotAllowedReason = "redstonelamp.loginFailed.noReason";
    public static final int DEFAULT_generator = 1;
    public static final long DEFAULT_entityID = 0; //Player entity ID is always 0

    public boolean loginAllowed;
    public String loginNotAllowedReason = DEFAULT_loginNotAllowedReason;

    public int generator = DEFAULT_generator;
    public int gamemode;
    public int health;
    public long entityID = DEFAULT_entityID;
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public float x;
    public float y;
    public float z;

    public LoginResponse(boolean loginAllowed, int gamemode, int health, float x, float y, float z){
        this.loginAllowed = loginAllowed;
        this.gamemode = gamemode;
        this.health = health;
        this.x = x;
        this.y = y;
        this.z = z;

        spawnX = Math.round(x);
        spawnY = Math.round(y);
        spawnZ = Math.round(z);
    }
}
