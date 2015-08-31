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
