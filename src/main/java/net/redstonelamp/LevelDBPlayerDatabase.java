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
import net.redstonelamp.level.Level;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.nio.BinaryBuffer;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A LevelDB implementation of a PlayerDatabase.
 *
 * @author RedstoneLamp Team
 */
public class LevelDBPlayerDatabase implements PlayerDatabase {

    private final Server server;
    private final Map<String, PlayerData> entries = new ConcurrentHashMap<>();
    private DB database;

    public LevelDBPlayerDatabase(Server server) {
        this.server = server;
    }

    @Override
    public void loadFrom(File location) throws IOException {
        if(!location.isDirectory()){
            server.getLogger().warning("Could not locate PlayerDatabase, creating new...");
            location.mkdirs();
            Options options = new Options();
            options.createIfMissing(true);
            options.compressionType(CompressionType.SNAPPY);
            database = Iq80DBFactory.factory.open(location, options);
            saveTo(location);
            return;
        }
        Options options = new Options();
        options.createIfMissing(false);
        options.compressionType(CompressionType.SNAPPY);
        database = Iq80DBFactory.factory.open(location, options);
    }

    @Override
    public void saveTo(File location) throws IOException {
        if(database == null) {
            throw new UnsupportedOperationException("Database is closed!");
        }
        for(PlayerData data : entries.values()) {
            BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.LITTLE_ENDIAN);
            bb.putUUID(data.getUuid());
            bb.putInt(data.getGamemode());
            bb.putInt(data.getHealth());
            bb.putVarString(data.getPosition().getLevel().getName());
            bb.putFloat(data.getPosition().getX());
            bb.putFloat(data.getPosition().getY());
            bb.putFloat(data.getPosition().getZ());
            bb.putFloat(data.getPosition().getYaw());
            bb.putFloat(data.getPosition().getPitch());
            bb.putVarString(data.getInventory().getClass().getName());
            byte[] inventory = data.getInventory().saveToBytes();
            bb.putVarInt(inventory.length);
            bb.put(inventory);
            BinaryBuffer key = BinaryBuffer.newInstance(16, ByteOrder.LITTLE_ENDIAN);
            key.putUUID(data.getUuid());
            database.put(key.toArray(), bb.toArray());
        }
    }

    @Override
    public void updateData(PlayerData data) {
        entries.put(data.getUuid().toString(), data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PlayerData getData(UUID uuid) {
        if(entries.containsKey(uuid.toString())) {
            return entries.get(uuid.toString());
        }
        BinaryBuffer key = BinaryBuffer.newInstance(16, ByteOrder.LITTLE_ENDIAN);
        key.putUUID(uuid);
        byte[] data = database.get(key.toArray());
        if(data != null) {
            BinaryBuffer bb = BinaryBuffer.wrapBytes(data, ByteOrder.LITTLE_ENDIAN);
            PlayerData entry = new PlayerData();
            entry.setUuid(bb.getUUID());
            entry.setGamemode(bb.getInt());
            entry.setHealth(bb.getInt());

            String lName = bb.getVarString();
            Level level = server.getLevelManager().getLevelByName(lName);
            if(level == null) {
                server.getLogger().warning("Could not find level \""+lName+"\", player will spawn in main level (malformed database?)");
                level = server.getLevelManager().getMainLevel();
            }

            Position position = new Position(level);
            position.setX(bb.getFloat());
            position.setY(bb.getFloat());
            position.setZ(bb.getFloat());
            position.setYaw(bb.getFloat());
            position.setPitch(bb.getFloat());
            entry.setPosition(position);

            String inventoryProviderName = bb.getVarString();
            try{
                Class invClass = Class.forName(inventoryProviderName);
                if(!PlayerInventory.class.isAssignableFrom(invClass)){
                    server.getLogger().error("[Malformed Database?] Inventory Provider does not extend PlayerInventory!");
                    return null;
                }
                try{
                    Method m = invClass.getDeclaredMethod("createFromBytes", byte[].class);
                    entry.setInventory((PlayerInventory) m.invoke(null, bb.get(bb.getVarInt())));
                    entries.put(entry.getUuid().toString(), entry);
                    return entry;
                }catch(NoSuchMethodException e){
                    server.getLogger().error("Inventory Provider MUST have method \"static PlayerInventory createFromBytes(byte[] bytes)\"");
                    server.getLogger().trace(e);
                }catch(InvocationTargetException e){
                    server.getLogger().error(e.getClass().getName() + " while attempting to load PlayerInventory for " + entry.getUuid().toString() + ": " + e.getMessage());
                    server.getLogger().trace(e);
                }catch(IllegalAccessException e){
                    server.getLogger().error(e.getClass().getName() + " while attempting to load PlayerInventory for " + entry.getUuid());
                    server.getLogger().error("(Check if method public?) " + e.getMessage());
                    server.getLogger().trace(e);
                }
            }catch(ClassNotFoundException e){
                server.getLogger().error("[Malformed Database?] FAILED TO FIND INVENTORY PROVIDER \"" + inventoryProviderName + "\" FOR " + entry.getUuid().toString());
                server.getLogger().trace(e);
            }
        }
        return null;
    }

    @Override
    public void release() throws IOException {
        database.close();
        database = null;
    }
}
