package redstonelamp.io.playerdata;

import org.apache.commons.io.IOUtils;
import redstonelamp.io.Storable;
import redstonelamp.level.location.Location;

import java.io.*;
import java.util.UUID;

/**
 * Represents a global Player Database for the server.
 *
 * @author jython234
 */
public interface PlayerDatabase extends Storable{

    /**
     * Retrieve an entry for a Player by their UUID.
     * @param id The Player's UUID.
     * @return The DatabaseEntry for the player if found, null if not.
     */
    DatabaseEntry getEntry(UUID id);

    /**
     * Put an entry into the database.
     * @param entry The Entry.
     */
    void putEntry(DatabaseEntry entry);

    /**
     * Saves this Database to a file.
     * @param file The File to be saved to.
     * @throws IOException If there is an error while saving.
     */
    default void save(File file) throws IOException{
        FileOutputStream out = new FileOutputStream(file);
        out.write(store());
        out.close();
    }

    /**
     * Loads this Database from a file.
     * @param file The File this database is to be loaded from.
     * @throws IOException If there is an error while loading.
     */
    default void loadFromFile(File file) throws IOException {
        byte[] data = IOUtils.toByteArray(new FileInputStream(file));
        load(data);
    }

    /**
     * Represents a DatabaseEntry in a PlayerDatabase.
     *
     * @author jython234
     */
    public static interface DatabaseEntry extends Storable{
        /**
         * Get the Player's gamemode for this entry.
         * @return The player's gamemode in the entry.
         */
        int getGamemode();

        /**
         * Get the Player's last known location for this entry.
         * @return The player's last known location in the entry.
         */
        Location getLocation();

        /**
         * Get the Player's health for this entry.
         * @return The player's health in the entry.
         */
        int getHealth();

        /**
         * Get the Player's UUID for this entry.
         * @return The player's UUID in the entry.
         */
        UUID getUUID();

        /**
         * Set the player's gamemode for this entry.
         * @param gamemode The gamemode as an Integer.
         */
        void setGamemode(int gamemode);

        /**
         * Set the player's last known location for this entry.
         * @param location The player's location.
         */
        void setLocation(Location location);

        /**
         * Set the player's health for this entry.
         * @param health The player's health.
         */
        void setHealth(int health);

        /**
         *
         * @param id
         */
        void setUUID(UUID id);
    }
}
