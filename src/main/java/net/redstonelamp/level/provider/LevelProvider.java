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
package net.redstonelamp.level.provider;

import net.redstonelamp.level.Chunk;
import net.redstonelamp.level.ChunkPosition;

import java.io.File;
import java.io.IOException;

/**
 * Represents a level I/O storage provider.
 */
public interface LevelProvider{
    /**
     * Get a Chunk from a ChunkPosition
     *
     * @param position The Position of the chunk
     * @return The chunk from the disk
     */
    Chunk getChunk(ChunkPosition position);

    /**
     * Called whenever the parent Level class is shutting down. Save the world here.
     */
    void shutdown();

    /**
     * Load the Level data such as the spawn position and level time
     * from the disk.
     *
     * @param file The File that contains the level data
     * @throws IOException If there is an error while loading the data.
     */
    void loadLevelData(File file) throws IOException;
}
