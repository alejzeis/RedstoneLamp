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
package net.redstonelamp.request;

import java.util.UUID;

/**
 * Represents a Login Request
 *
 * @author RedstoneLamp Team
 */
public class LoginRequest extends Request{
    public String username;
    public String userAgent;
    public UUID uuid;
    public long clientId;
    public long authid;
    public byte[] skin;
    public boolean slim;

    public LoginRequest(String username, String userAgent, UUID uuid){
        this.username = username;
        this.userAgent = userAgent;
        this.uuid = uuid;
    }

    @Override
    public void execute(){
        //TODO?
    }
}
