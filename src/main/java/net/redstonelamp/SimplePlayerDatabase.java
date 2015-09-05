package net.redstonelamp;

import net.redstonelamp.inventory.PlayerInventory;
import net.redstonelamp.level.Level;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.nio.BinaryBuffer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Simple Implementation of a PlayerDatabase. This implementation
 * saves to one big file. The file is NOT compressed.
 *
 * @author RedstoneLamp Team
 */
public class SimplePlayerDatabase implements PlayerDatabase {
    /**
     * Storage Version of SimplePlayerDatabase. It is used to prevent strange things while
     * processing outdated databases. The version is always the first byte of the file.
     */
    public static final byte STORAGE_VERSION = 2;
    private final Server server;
    private final Map<String, PlayerData> entries = new ConcurrentHashMap<>();

    public SimplePlayerDatabase(Server server) {
        this.server = server;
    }

    @Override
    public void loadFrom(File location) throws IOException {
        if(!location.isFile()) {
            server.getLogger().warning("Could not locate PlayerDatabase, creating new...");
            location.createNewFile();
            saveTo(location);
            return;
        }
        byte[] data = FileUtils.readFileToByteArray(location);
        if(data == null || data.length < 1) {
            throw new IOException("DatabaseFile empty (corrupt database?)");
        }
        BinaryBuffer bb = BinaryBuffer.wrapBytes(data, ByteOrder.LITTLE_ENDIAN);
        if(bb.getByte() != STORAGE_VERSION) {
            throw new IOException("Database is outdated!");
        }
        int numEntries = bb.getVarInt();
        for(int i = 0; i < numEntries; i++) {
            readEntry(bb);
        }
        if(bb.getByte() != (byte) 0x7E) {
            server.getLogger().warning("End of database byte does not match");
        }
    }

    @SuppressWarnings("unchecked")
    private void readEntry(BinaryBuffer bb) {
        PlayerData data = new PlayerData();
        data.setUuid(bb.getUUID());

        double x = bb.getDouble();
        double y = bb.getDouble();
        double z = bb.getDouble();
        float yaw = bb.getFloat();
        float pitch = bb.getFloat();
        String name = bb.getVarString();
        Level level = server.getLevelManager().getLevelByName(name);
        if(level == null) {
            server.getLogger().warning("Could not find level: \""+name+"\", player "+data.getUuid().toString()+" will spawn in main level");
            level = server.getLevelManager().getMainLevel();
        }
        data.setPosition(new Position(x, y, z, yaw, pitch, level));
        data.setHealth(bb.getVarInt());
        data.setGamemode(bb.getVarInt());

        String inventoryImpl = bb.getVarString();
        try {
            Class invClass = Class.forName(inventoryImpl);
            if(!invClass.isAssignableFrom(PlayerInventory.class)) {
                server.getLogger().error("[Malformed Database?] Inventory Provider does not extend PlayerInventory!");
                return;
            }
            try {
                Method m = invClass.getDeclaredMethod("createFromBytes", byte[].class);
                data.setInventory((PlayerInventory) m.invoke(null, bb.remainingBytes()));
            } catch (NoSuchMethodException e) {
                server.getLogger().error("Inventory Provider MUST have method \"static PlayerInventory createFromBytes(byte[] bytes)\"");
                server.getLogger().trace(e);
            } catch (InvocationTargetException e) {
                server.getLogger().error(e.getClass().getName()+" while attempting to load PlayerInventory for "+data.getUuid().toString()+": "+e.getMessage());
                server.getLogger().trace(e);
            } catch (IllegalAccessException e) {
                server.getLogger().error(e.getClass().getName()+" while attempting to load PlayerInventory for "+data.getUuid());
                server.getLogger().error("(Check if method public?) "+e.getMessage());
                server.getLogger().trace(e);
            }
        } catch (ClassNotFoundException e) {
            server.getLogger().error("[Malformed Database?] FAILED TO FIND INVENTORY PROVIDER \""+inventoryImpl+"\" FOR "+data.getUuid().toString());
            server.getLogger().trace(e);
        }
    }

    @Override
    public void saveTo(File location) throws IOException {
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.LITTLE_ENDIAN);
        bb.putByte(STORAGE_VERSION);
        bb.putVarInt(entries.size());
        for(PlayerData entry : entries.values()) {
            putEntry(bb, entry);
        }
        bb.putByte((byte) 0x7E); //Signal end of database
        FileUtils.writeByteArrayToFile(location, bb.toArray());
    }

    private void putEntry(BinaryBuffer bb, PlayerData entry) {
        bb.putUUID(entry.getUuid());

        bb.putDouble(entry.getPosition().getX());
        bb.putDouble(entry.getPosition().getY());
        bb.putDouble(entry.getPosition().getZ());
        bb.putFloat(entry.getPosition().getYaw());
        bb.putFloat(entry.getPosition().getPitch());
        bb.putVarString(entry.getPosition().getLevel().getName());

        bb.putVarInt(entry.getHealth());
        bb.putVarInt(entry.getGamemode());

        bb.putVarString(entry.getInventory().getClass().getName());
        bb.put(entry.getInventory().saveToBytes());

        bb.putByte((byte) 0x7F); //Signal end of entry
    }

    @Override
    public void updateData(PlayerData data) {
        entries.put(data.getUuid().toString(), data);
    }

    @Override
    public PlayerData getData(UUID uuid) {
        if(entries.containsKey(uuid.toString())) {
            return entries.get(uuid.toString());
        }
        return null;
    }
}
