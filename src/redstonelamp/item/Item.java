package redstonelamp.item;

import redstonelamp.block.BlockValues;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Base class for all items.
 */
public class Item implements ItemValues{
    private static Map<Integer, Class<? extends Item>> list = new ConcurrentHashMap<>();
    private static List<Item> creativeItems = new CopyOnWriteArrayList<>();

    private int id;
    private short metadata;
    private int count;

    public Item(int id, short metadata, int count){
        this.id = id;
        this.metadata = metadata;
        this.count = count;
    }

    public static void init(){
        if(list.keySet().isEmpty()) {
            list.put(ItemValues.APPLE, Apple.class);
            list.put(ItemValues.BED, Bed.class);
            list.put(ItemValues.BEETROOT_SEEDS, BeetrootSeeds.class);
            list.put(ItemValues.BEETROOT_SOUP, BeetrootSoup.class);
            list.put(ItemValues.BOW, Bow.class);
            list.put(ItemValues.BOWL, Bowl.class);
            list.put(ItemValues.BRICK, Brick.class);
            list.put(ItemValues.BUCKET, Bucket.class);
            list.put(ItemValues.CAKE, Cake.class);
            list.put(ItemValues.CARROT, Carrot.class);
            list.put(ItemValues.CHAIN_BOOTS, ChainBoots.class);
            list.put(ItemValues.CHAIN_CHESTPLATE, ChainChestplate.class);
            list.put(ItemValues.CHAIN_HELMET, ChainHelmet.class);
            list.put(ItemValues.CHAIN_LEGGINGS, ChainLeggings.class);
            list.put(ItemValues.COAL, Coal.class);
            list.put(ItemValues.COOKED_FISH, CookedFish.class);
            list.put(ItemValues.DIAMOND, Diamond.class);
            list.put(ItemValues.DIAMOND_AXE, DiamondAxe.class);
            list.put(ItemValues.DIAMOND_BOOTS, DiamondBoots.class);
            list.put(ItemValues.DIAMOND_CHESTPLATE, DiamondChestplate.class);
            list.put(ItemValues.DIAMOND_HELMET, DiamondHelmet.class);
            list.put(ItemValues.DIAMOND_HOE, DiamondHoe.class);
            list.put(ItemValues.DIAMOND_LEGGINGS, DiamondLeggings.class);
            list.put(ItemValues.DIAMOND_PICKAXE, DiamondPickaxe.class);
            list.put(ItemValues.DIAMOND_SHOVEL, DiamondShovel.class);
            list.put(ItemValues.DIAMOND_SWORD, DiamondSword.class);
            list.put(ItemValues.FEATHER, Feather.class);
            list.put(ItemValues.FLINT_STEEL, FlintSteel.class);
            list.put(ItemValues.GOLD_AXE, GoldAxe.class);
            list.put(ItemValues.GOLD_BOOTS, GoldBoots.class);
            list.put(ItemValues.GOLD_CHESTPLATE, GoldChestplate.class);
            list.put(ItemValues.GOLD_HELMET, GoldHelmet.class);
            list.put(ItemValues.GOLD_HOE, GoldHoe.class);
            list.put(ItemValues.GOLD_INGOT, GoldIngot.class);
            list.put(ItemValues.GOLD_LEGGINGS, GoldLeggings.class);
            list.put(ItemValues.GOLD_PICKAXE, GoldPickaxe.class);
            list.put(ItemValues.GOLD_SHOVEL, GoldShovel.class);
            list.put(ItemValues.GOLD_SWORD, GoldSword.class);
            list.put(ItemValues.IRON_AXE, IronAxe.class);
            list.put(ItemValues.IRON_BOOTS, IronBoots.class);
            list.put(ItemValues.IRON_CHESTPLATE, IronChestplate.class);
            list.put(ItemValues.IRON_DOOR, IronDoor.class);
            list.put(ItemValues.IRON_HELMET, IronHelmet.class);
            list.put(ItemValues.IRON_HOE, IronHoe.class);
            list.put(ItemValues.IRON_INGOT, IronIngot.class);
            list.put(ItemValues.IRON_LEGGINGS, IronLeggings.class);
            list.put(ItemValues.IRON_PICKAXE, IronPickaxe.class);
            list.put(ItemValues.IRON_SHOVEL, IronShovel.class);
            list.put(ItemValues.IRON_SWORD, IronSword.class);
            list.put(ItemValues.LEATHER_BOOTS, LeatherBoots.class);
            list.put(ItemValues.LEATHER_CAP, LeatherCap.class);
            list.put(ItemValues.LEATHER_PANTS, LeatherPants.class);
            list.put(ItemValues.LEATHER_TUNIC, LeatherTunic.class);
            list.put(ItemValues.MELON_SEEDS, MelonSeeds.class);
            list.put(ItemValues.MUSHROOM_STEW, MushroomStew.class);
            list.put(ItemValues.PAINTING, Painting.class);
            list.put(ItemValues.POTATO, Potato.class);
            list.put(ItemValues.PUMPKIN_SEEDS, PumpkinSeeds.class);
            list.put(ItemValues.RAW_FISH, RawFish.class);
            list.put(ItemValues.SHEARS, Shears.class);
            list.put(ItemValues.SIGN, Sign.class);
            list.put(ItemValues.SNOWBALL, Snowball.class);
            list.put(ItemValues.SPAWN_EGG, SpawnEgg.class);
            list.put(ItemValues.STICK, Stick.class);
            list.put(ItemValues.STONE_AXE, StoneAxe.class);
            list.put(ItemValues.STONE_HOE, StoneHoe.class);
            list.put(ItemValues.STONE_PICKAXE, StonePickaxe.class);
            list.put(ItemValues.STONE_SHOVEL, StoneShovel.class);
            list.put(ItemValues.STONE_SWORD, StoneSword.class);
            list.put(ItemValues.SUGARCANE, Sugarcane.class);
            list.put(ItemValues.WHEAT_SEEDS, WheatSeeds.class);
            list.put(ItemValues.WOODEN_AXE, WoodenAxe.class);
            list.put(ItemValues.WOODEN_DOOR, WoodenDoor.class);
            list.put(ItemValues.WOODEN_HOE, WoodenHoe.class);
            list.put(ItemValues.WOODEN_PICKAXE, WoodenPickaxe.class);
            list.put(ItemValues.WOODEN_SHOVEL, WoodenShovel.class);
            list.put(ItemValues.WOODEN_SWORD, WoodenSword.class);
        }
        Item.initCreativeItems();
    }

    private static void initCreativeItems(){
        creativeItems.clear();

        addCreativeItem(Item.get(BlockValues.COBBLESTONE, (short) 0, 0));
    }

    public static void addCreativeItem(Item item){
        creativeItems.add(Item.get(item.getId(), item.getMetadata(), 0));
    }

    public static List<Item> getCreativeItems(){
        return creativeItems;
    }

    public static Item get(int id, short metadata, int count){
        if(list.containsKey(id)){
            Class<? extends Item> clazz = list.get(id);
            try {
                return clazz.getConstructor(int.class, short.class, int.class).newInstance(id, metadata, count);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if(id < 256){
            //TODO: ItemBlock
        } else {
            return new Item(id, metadata, count);
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public short getMetadata() {
        return metadata;
    }

    public void setMetadata(short metadata) {
        this.metadata = metadata;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
