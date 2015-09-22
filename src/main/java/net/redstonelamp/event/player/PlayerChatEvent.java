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
package net.redstonelamp.event.player;

import lombok.Getter;
import net.redstonelamp.Player;
import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;

public class PlayerChatEvent extends Event implements Cancellable {
    @Getter private Player player;
    @Getter private String message;
    private boolean cancelled = false;
    
    public PlayerChatEvent(Player player, String message) {
        this.player = player;
        this.message = message;
    }
    
    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
