package redstonelamp.io.playerdata;

import redstonelamp.io.InvalidDataException;
import redstonelamp.level.location.Location;
import redstonelamp.level.location.Position;
import redstonelamp.utils.Binary;
import redstonelamp.utils.DynamicByteBuffer;

import java.nio.ByteOrder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple Playerdatabse that stores into one big file.
 *
 * @author jython234
 */
public class GenericPlayerDatabase implements PlayerDatabase{
    public final static byte DB_STORAGE_VERSION = 1;
    private Map<String, GenericDatabaseEntry> entries = new ConcurrentHashMap<>();

    @Override
    public DatabaseEntry getEntry(UUID id) {
        if(entries.containsKey(id.toString())){
            return entries.get(id.toString());
        }
        return null;
    }

    @Override
    public void putEntry(DatabaseEntry entry) {
        if(!(entry instanceof GenericDatabaseEntry)){
            throw new IllegalArgumentException("DatabaseEntry must be instanceof a GenericDatabaseEntry.");
        }
        entries.put(entry.getUUID().toString(), (GenericDatabaseEntry) entry);
    }

    @Override
    public byte[] store() {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(ByteOrder.LITTLE_ENDIAN);
        bb.putByte(DB_STORAGE_VERSION);
        bb.putLong(entries.size());
        for(GenericDatabaseEntry entry : entries.values()) {
            byte[] data = entry.store();
            bb.putUnsignedShort(data.length);
            bb.put(data);
        }
        return bb.toArray();
    }

    @Override
    public void load(byte[] source) {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(source, ByteOrder.LITTLE_ENDIAN);
        if(bb.getByte() != DB_STORAGE_VERSION){
            throw new InvalidDataException("DB_STORAGE_VERSION does not match.");
        }
        long entries = bb.getLong();
        for(long i = 0; i < entries; i++){
            int len = bb.getUnsignedShort();
            GenericDatabaseEntry entry = new GenericDatabaseEntry();
            entry.load(bb.get(len));
        }
    }

    public static class GenericDatabaseEntry implements DatabaseEntry {
        public final static byte ENTRY_STORAGE_VERSION = 1;

        private int gamemode;
        private Location location;
        private int health;
        private UUID uuid;

        @Override
        public synchronized int getGamemode() {
            return gamemode;
        }

        @Override
        public synchronized Location getLocation() {
            return location;
        }

        @Override
        public synchronized int getHealth() {
            return health;
        }

        @Override
        public synchronized UUID getUUID() {
            return uuid;
        }

        @Override
        public synchronized void setGamemode(int gamemode) {
            this.gamemode = gamemode;
        }

        @Override
        public synchronized void setLocation(Location location) {
            this.location = location;
        }

        @Override
        public synchronized void setHealth(int health) {
            this.health = health;
        }

        @Override
        public synchronized void setUUID(UUID id) {
            this.uuid = id;
        }

        @Override
        public byte[] store() {
            DynamicByteBuffer bb = DynamicByteBuffer.newInstance(ByteOrder.LITTLE_ENDIAN);
            bb.putByte(ENTRY_STORAGE_VERSION);
            bb.putInt(gamemode);

            byte[] locStoreData = location.store();
            bb.putUnsignedShort(locStoreData.length);
            bb.put(locStoreData);

            bb.putInt(health);
            bb.put(Binary.newInstance(ByteOrder.LITTLE_ENDIAN).writeUUID(uuid));
            return bb.toArray();
        }

        @Override
        public void load(byte[] source) {
            DynamicByteBuffer bb = DynamicByteBuffer.newInstance(source, ByteOrder.LITTLE_ENDIAN);
            if(bb.getByte() != ENTRY_STORAGE_VERSION){
                throw new InvalidDataException("ENTRY_STORAGE_VERSION does not match!");
            }
            gamemode = bb.getInt();
            location = new Location(0, 0, 0, null);
            int len = bb.getUnsignedShort();
            location.load(bb.get(len));
            health = bb.getInt();
            uuid = Binary.newInstance(ByteOrder.LITTLE_ENDIAN).readUUID(bb.get(16));
        }
    }
}
