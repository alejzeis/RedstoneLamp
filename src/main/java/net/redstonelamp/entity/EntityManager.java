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
package net.redstonelamp.entity;

import net.redstonelamp.level.Level;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manager for all entities in a level.
 *
 * @author RedstoneLamp Team
 */
public class EntityManager {
    private final Level level;
    private List<Entity> entities = new CopyOnWriteArrayList<>();

    /**
     * Create a new EntityManager for the specified <code>level</code>
     * @param level The Level this manager belongs to.
     */
    public EntityManager(Level level) {
        this.level = level;
    }

    /**
     * INTERNAL METHOD!
     * Adds an entity to the manager.
     * @param e The entity to be added.
     */
    public synchronized void addEntity(Entity e) {
        for(Entity entity : entities) {
            if(entity == e || entity.getEntityID() == e.getEntityID()) {
                throw new IllegalArgumentException("Entity is already added!");
            }
        }
        entities.add(e);
    }

    /**
     * INTERNAL METHOD!
     * Removes an entity from the manager.
     * @param e The entity to be removed.
     */
    public synchronized void removeEntity(Entity e) {
        entities.remove(e);
    }

    /**
     * Get an Entity by their entityID.
     * @param id The EntityID of the Entity
     * @return The Entity, if found, null if not.
     */
    public Entity getEntityById(int id) {
        for(Entity entity : entities) {
            if(entity.getEntityID() == id) {
                return entity;
            }
        }
        return null;
    }
}
