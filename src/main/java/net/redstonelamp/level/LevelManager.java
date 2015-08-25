/**
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
package net.redstonelamp.level;

import net.redstonelamp.Server;
import net.redstonelamp.level.provider.leveldb.LevelDBProvider;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for different Levels.
 *
 * @author RedstoneLamp Team
 */
public class LevelManager {
    private final Server server;
    private final Map<String, Level> levels = new ConcurrentHashMap<>();
    private Level mainLevel;

    public LevelManager(Server server) {
        this.server = server;
    }

    /**
     * INTERNAL METHOD!
     * Used in Server to initialize the LevelManager class.
     */
    public void init() {
        String name = server.getConfig().getString("level-name");
        File levelDir = new File("worlds" + File.separator + name);
        if(!levelDir.isDirectory()) {
            levelDir.mkdirs();
        }
        mainLevel = new Level(this, levelDir, "levelDB"); //TODO: correct provider
        levels.put(mainLevel.getName(), mainLevel);
    }

    public Server getServer() {
        return server;
    }

    public Level getMainLevel() {
        return mainLevel;
    }

}
