package net.redstonelamp;

import net.redstonelamp.inventory.PlayerInventory;
import net.redstonelamp.level.Level;
import net.redstonelamp.level.position.Position;
import org.spout.nbt.*;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A NBT Player database that stores all the data in one file.
 *
 * @author jython234
 */
public class NBTPlayerDatabase implements PlayerDatabase {

    private final Server server;
    private final Map<String, PlayerData> entries = new ConcurrentHashMap<>();

    public NBTPlayerDatabase(Server server) {
        this.server = server;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadFrom(File location) throws IOException {
        if(!location.isFile()) {
            server.getLogger().warning("Could not locate PlayerDatabase, creating new...");
            location.createNewFile();
            saveTo(location);
            return;
        }
        NBTInputStream in = new NBTInputStream(new FileInputStream(location), true);
        Tag t = in.readTag();
        in.close();
        if(!(t instanceof CompoundTag)) throw new RuntimeException("Invalid Database! First tag must be Compound!");
        CompoundTag c = (CompoundTag) t;
        for(Tag tag : c.getValue()) {
            if(!(tag instanceof CompoundTag)) throw new RuntimeException("Tag must be compound.");
            CompoundTag entry = (CompoundTag) tag;
            StringTag uuid = (StringTag) entry.getValue().get(0);
            IntTag gamemode = (IntTag) entry.getValue().get(1);
            IntTag health = (IntTag) entry.getValue().get(2);
            StringTag levelName = (StringTag) entry.getValue().get(3);
            FloatTag posX = (FloatTag) entry.getValue().get(4);
            FloatTag posY = (FloatTag) entry.getValue().get(5);
            FloatTag posZ = (FloatTag) entry.getValue().get(6);
            FloatTag posYaw = (FloatTag) entry.getValue().get(7);
            FloatTag posPitch = (FloatTag) entry.getValue().get(8);
            StringTag inventoryProvider = (StringTag) entry.getValue().get(9);
            ByteArrayTag inventory = (ByteArrayTag) entry.getValue().get(10);

            PlayerData data = new PlayerData();
            data.setUuid(UUID.fromString(uuid.getValue()));
            data.setGamemode(gamemode.getValue());
            data.setHealth(health.getValue());
            Level l = server.getLevelManager().getLevelByName(levelName.getValue());
            if(l == null) {
                server.getLogger().warning("(Malformed Database?) Could not find correct level, player will spawn in main level.");
                l = server.getLevelManager().getMainLevel();
            }
            data.setPosition(new Position(posX.getValue(), posY.getValue(), posZ.getValue(), posYaw.getValue(), posPitch.getValue(), l));
            try{
                Class invClass = Class.forName(inventoryProvider.getValue());
                if(!PlayerInventory.class.isAssignableFrom(invClass)){
                    server.getLogger().error("[Malformed Database?] Inventory Provider does not extend PlayerInventory!");
                    continue;
                }
                try{
                    Method m = invClass.getDeclaredMethod("createFromBytes", byte[].class);
                    data.setInventory((PlayerInventory) m.invoke(null, inventory.getValue()));
                    entries.put(data.getUuid().toString(), data);
                }catch(NoSuchMethodException e){
                    server.getLogger().error("Inventory Provider MUST have method \"static PlayerInventory createFromBytes(byte[] bytes)\"");
                    server.getLogger().trace(e);
                }catch(InvocationTargetException e){
                    server.getLogger().error(e.getClass().getName() + " while attempting to load PlayerInventory for " + data.getUuid().toString() + ": " + e.getMessage());
                    server.getLogger().trace(e);
                }catch(IllegalAccessException e){
                    server.getLogger().error(e.getClass().getName() + " while attempting to load PlayerInventory for " + data.getUuid());
                    server.getLogger().error("(Check if method public?) " + e.getMessage());
                    server.getLogger().trace(e);
                }
            }catch(ClassNotFoundException e){
                server.getLogger().error("[Malformed Database?] FAILED TO FIND INVENTORY PROVIDER \"" + inventoryProvider.getValue() + "\" FOR " + data.getUuid().toString());
                server.getLogger().trace(e);
            }
        }
    }

    @Override
    public void saveTo(File location) throws IOException {
        NBTOutputStream out = new NBTOutputStream(new FileOutputStream(location), true);
        List<Tag> compounds = new ArrayList<>();
        for(PlayerData entry : entries.values()) {
            List<Tag> tags = new ArrayList<>();
            tags.add(new StringTag("uuid", entry.getUuid().toString()));
            tags.add(new IntTag("gamemode", entry.getGamemode()));
            tags.add(new IntTag("health", entry.getHealth()));
            tags.add(new StringTag("levelName", entry.getPosition().getLevel().getName()));
            tags.add(new FloatTag("positionX", entry.getPosition().getX()));
            tags.add(new FloatTag("positionY", entry.getPosition().getY()));
            tags.add(new FloatTag("positionZ", entry.getPosition().getZ()));
            tags.add(new FloatTag("positionYaw", entry.getPosition().getYaw()));
            tags.add(new FloatTag("positionPitch", entry.getPosition().getPitch()));
            tags.add(new StringTag("inventoryProvider", entry.getInventory().getClass().getName()));
            tags.add(new ByteArrayTag("inventory", entry.getInventory().saveToBytes()));
            compounds.add(new CompoundTag("entry-"+entry.getUuid().toString(), tags));
        }
        out.writeTag(new CompoundTag("playerData", compounds));
        out.close();
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

    @Override
    public void release() throws IOException {

    }
}
