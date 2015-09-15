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
package net.redstonelamp;

import net.redstonelamp.inventory.PlayerInventory;
import net.redstonelamp.level.position.Position;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Represents a Database for storing PlayerData
 *
 * @author RedstoneLamp Team
 */
public interface PlayerDatabase{

    /**
     * Loads this database from a location.
     * <br>
     * The location can be a file if the implementation depends on a
     * single file, or a directory where each player has their
     * own file.
     *
     * @param location The location where the database is stored.
     * @throws IOException If there is an exception while loading the database.
     */
    void loadFrom(File location) throws IOException;

    /**
     * Saves this database to a location.
     * <br>
     * The location can be a file if the implementation depends on a
     * single file, or a directory where each player has their
     * own file.
     *
     * @param location The location where the database is stored.
     * @throws IOException If there is an exception while saving the database.
     */
    void saveTo(File location) throws IOException;

    /**
     * Updates an entry in the database.
     *
     * @param data The PlayerData to be updated.
     */
    void updateData(PlayerData data);

    /**
     * Gets an entry in the PlayerDatabase by the player's UUID.
     *
     * @param uuid The UUID of the player.
     * @return The PlayerData if found, null if not.
     */
    PlayerData getData(UUID uuid);

    /**
     * Represents an entry in the PlayerDatabase containing
     * information about a Player.
     *
     * @author RedstoneLamp Team
     */
    public static class PlayerData{
        private UUID uuid;
        private Position position;
        private PlayerInventory inventory;
        private int health;
        private int gamemode;

        public UUID getUuid(){
            return uuid;
        }

        public void setUuid(UUID uuid){
            this.uuid = uuid;
        }

        public Position getPosition(){
            return position;
        }

        public void setPosition(Position position){
            this.position = position;
        }

        public int getHealth(){
            return health;
        }

        public void setHealth(int health){
            this.health = health;
        }

        public PlayerInventory getInventory(){
            return inventory;
        }

        public void setInventory(PlayerInventory inventory){
            this.inventory = inventory;
        }

        public int getGamemode(){
            return gamemode;
        }

        public void setGamemode(int gamemode){
            this.gamemode = gamemode;
        }
    }
}
