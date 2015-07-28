package redstonelamp.item;

import redstonelamp.RedstoneLamp;
import redstonelamp.block.Block;
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

        //Building
        addCreativeItem(Item.get(ItemValues.COBBLESTONE, 0));
        addCreativeItem(Item.get(ItemValues.STONE_BRICKS, 0));
        addCreativeItem(Item.get(ItemValues.STONE_BRICKS, 1));
        addCreativeItem(Item.get(ItemValues.STONE_BRICKS, 2));
        addCreativeItem(Item.get(ItemValues.STONE_BRICKS, 3));
        addCreativeItem(Item.get(ItemValues.MOSS_STONE, 0));
        addCreativeItem(Item.get(ItemValues.WOODEN_PLANKS, 0));
        addCreativeItem(Item.get(ItemValues.WOODEN_PLANKS, 1));
        addCreativeItem(Item.get(ItemValues.WOODEN_PLANKS, 2));
        addCreativeItem(Item.get(ItemValues.WOODEN_PLANKS, 3));
        addCreativeItem(Item.get(ItemValues.WOODEN_PLANKS, 4));
        addCreativeItem(Item.get(ItemValues.WOODEN_PLANKS, 5));
        addCreativeItem(Item.get(ItemValues.BRICKS, 0));
        addCreativeItem(Item.get(ItemValues.STONE, 0));
        addCreativeItem(Item.get(ItemValues.STONE, 1));
        addCreativeItem(Item.get(ItemValues.STONE, 2));
        addCreativeItem(Item.get(ItemValues.STONE, 3));
        addCreativeItem(Item.get(ItemValues.STONE, 4));
        addCreativeItem(Item.get(ItemValues.STONE, 5));
        addCreativeItem(Item.get(ItemValues.STONE, 6));
        addCreativeItem(Item.get(ItemValues.DIRT, 0));
        addCreativeItem(Item.get(ItemValues.PODZOL, 0));
        addCreativeItem(Item.get(ItemValues.GRASS, 0));
        addCreativeItem(Item.get(ItemValues.MYCELIUM, 0));
        addCreativeItem(Item.get(ItemValues.CLAY_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.HARDENED_CLAY, 0));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 0));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 7));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 6));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 5));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 4));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 3));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 2));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 1));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 15));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 14));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 13));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 12));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 11));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 10));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 9));
        addCreativeItem(Item.get(ItemValues.STAINED_CLAY, 8));
        addCreativeItem(Item.get(ItemValues.SANDSTONE, 0));
        addCreativeItem(Item.get(ItemValues.SANDSTONE, 1));
        addCreativeItem(Item.get(ItemValues.SANDSTONE, 2));
        addCreativeItem(Item.get(ItemValues.SAND, 0));
        addCreativeItem(Item.get(ItemValues.SAND, 1));
        addCreativeItem(Item.get(ItemValues.GRAVEL, 0));
        addCreativeItem(Item.get(ItemValues.TRUNK, 0));
        addCreativeItem(Item.get(ItemValues.TRUNK, 1));
        addCreativeItem(Item.get(ItemValues.TRUNK, 2));
        addCreativeItem(Item.get(ItemValues.TRUNK, 3));
        addCreativeItem(Item.get(ItemValues.TRUNK2, 0));
        addCreativeItem(Item.get(ItemValues.TRUNK2, 1));
        addCreativeItem(Item.get(ItemValues.NETHER_BRICKS, 0));
        addCreativeItem(Item.get(ItemValues.NETHERRACK, 0));
        addCreativeItem(Item.get(ItemValues.BEDROCK, 0));
        addCreativeItem(Item.get(ItemValues.COBBLESTONE_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.OAK_WOODEN_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.SPRUCE_WOODEN_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.BIRCH_WOODEN_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.JUNGLE_WOODEN_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.ACACIA_WOODEN_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.DARK_OAK_WOODEN_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.BRICK_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.SANDSTONE_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.STONE_BRICK_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.NETHER_BRICKS_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.QUARTZ_STAIRS, 0));
        addCreativeItem(Item.get(ItemValues.SLAB, 0));
        addCreativeItem(Item.get(ItemValues.SLAB, 1));
        addCreativeItem(Item.get(ItemValues.WOODEN_SLAB, 0));
        addCreativeItem(Item.get(ItemValues.WOODEN_SLAB, 1));
        addCreativeItem(Item.get(ItemValues.WOODEN_SLAB, 2));
        addCreativeItem(Item.get(ItemValues.WOODEN_SLAB, 3));
        addCreativeItem(Item.get(ItemValues.WOODEN_SLAB, 4));
        addCreativeItem(Item.get(ItemValues.WOODEN_SLAB, 5));
        addCreativeItem(Item.get(ItemValues.SLAB, 3));
        addCreativeItem(Item.get(ItemValues.SLAB, 4));
        addCreativeItem(Item.get(ItemValues.SLAB, 5));
        addCreativeItem(Item.get(ItemValues.SLAB, 6));
        addCreativeItem(Item.get(ItemValues.QUARTZ_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.QUARTZ_BLOCK, 1));
        addCreativeItem(Item.get(ItemValues.QUARTZ_BLOCK, 2));
        addCreativeItem(Item.get(ItemValues.COAL_ORE, 0));
        addCreativeItem(Item.get(ItemValues.IRON_ORE, 0));
        addCreativeItem(Item.get(ItemValues.GOLD_ORE, 0));
        addCreativeItem(Item.get(ItemValues.DIAMOND_ORE, 0));
        addCreativeItem(Item.get(ItemValues.LAPIS_ORE, 0));
        addCreativeItem(Item.get(ItemValues.REDSTONE_ORE, 0));
        addCreativeItem(Item.get(ItemValues.EMERALD_ORE, 0));
        addCreativeItem(Item.get(ItemValues.OBSIDIAN, 0));
        addCreativeItem(Item.get(ItemValues.ICE, 0));
        addCreativeItem(Item.get(ItemValues.SNOW_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.END_STONE, 0));
        //Decoration
        addCreativeItem(Item.get(ItemValues.COBBLESTONE_WALL, 0));
        addCreativeItem(Item.get(ItemValues.COBBLESTONE_WALL, 1));
        addCreativeItem(Item.get(ItemValues.WATER_LILY, 0));
        addCreativeItem(Item.get(ItemValues.GOLD_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.IRON_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.DIAMOND_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.LAPIS_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.COAL_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.EMERALD_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.REDSTONE_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.SNOW_LAYER, 0));
        addCreativeItem(Item.get(ItemValues.GLASS, 0));
        addCreativeItem(Item.get(ItemValues.GLOWSTONE_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.VINES, 0));
        addCreativeItem(Item.get(ItemValues.NETHER_REACTOR, 0));
        addCreativeItem(Item.get(ItemValues.LADDER, 0));
        addCreativeItem(Item.get(ItemValues.SPONGE, 0));
        addCreativeItem(Item.get(ItemValues.GLASS_PANE, 0));
        addCreativeItem(Item.get(ItemValues.WOODEN_DOOR, 0));
        addCreativeItem(Item.get(ItemValues.TRAPDOOR, 0));
        addCreativeItem(Item.get(ItemValues.FENCE, 0));
        addCreativeItem(Item.get(ItemValues.FENCE, 1));
        addCreativeItem(Item.get(ItemValues.FENCE, 2));
        addCreativeItem(Item.get(ItemValues.FENCE, 3));
        addCreativeItem(Item.get(ItemValues.FENCE, 4));
        addCreativeItem(Item.get(ItemValues.FENCE, 5));
        addCreativeItem(Item.get(ItemValues.FENCE_GATE, 0));
        addCreativeItem(Item.get(ItemValues.FENCE_GATE_BIRCH, 0));
        addCreativeItem(Item.get(ItemValues.FENCE_GATE_SPRUCE, 0));
        addCreativeItem(Item.get(ItemValues.FENCE_GATE_DARK_OAK, 0));
        addCreativeItem(Item.get(ItemValues.FENCE_GATE_JUNGLE, 0));
        addCreativeItem(Item.get(ItemValues.FENCE_GATE_ACACIA, 0));
        addCreativeItem(Item.get(ItemValues.IRON_BARS, 0));
        addCreativeItem(Item.get(ItemValues.BED, 0));
        addCreativeItem(Item.get(ItemValues.BOOKSHELF, 0));
        addCreativeItem(Item.get(ItemValues.PAINTING, 0));
        addCreativeItem(Item.get(ItemValues.WORKBENCH, 0));
        addCreativeItem(Item.get(ItemValues.STONECUTTER, 0));
        addCreativeItem(Item.get(ItemValues.CHEST, 0));
        addCreativeItem(Item.get(ItemValues.FURNACE, 0));
        addCreativeItem(Item.get(ItemValues.END_PORTAL, 0));
        addCreativeItem(Item.get(ItemValues.DANDELION, 0));
        /*
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_POPPY));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_BLUE_ORCHID));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_ALLIUM));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_AZURE_BLUET));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_RED_TULIP));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_ORANGE_TULIP));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_WHITE_TULIP));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_PINK_TULIP));
        addCreativeItem(Item.get(ItemValues.RED_FLOWER, Flower::TYPE_OXEYE_DAISY));
        */
        //TODO: Lilac
        //TODO: Double Tallgrass
        //TODO: Large Fern
        //TODO: Rose Bush
        //TODO: Peony
        addCreativeItem(Item.get(ItemValues.BROWN_MUSHROOM, 0));
        addCreativeItem(Item.get(ItemValues.RED_MUSHROOM, 0));
        //TODO: Mushroom block (brown, cover)
        //TODO: Mushroom block (red, cover)
        //TODO: Mushroom block (brown, stem)
        //TODO: Mushroom block (red, stem)
        addCreativeItem(Item.get(ItemValues.CACTUS, 0));
        addCreativeItem(Item.get(ItemValues.MELON_BLOCK, 0));
        addCreativeItem(Item.get(ItemValues.PUMPKIN, 0));
        addCreativeItem(Item.get(ItemValues.LIT_PUMPKIN, 0));
        addCreativeItem(Item.get(ItemValues.COBWEB, 0));
        addCreativeItem(Item.get(ItemValues.HAY_BALE, 0));
        addCreativeItem(Item.get(ItemValues.TALL_GRASS, 1));
        addCreativeItem(Item.get(ItemValues.TALL_GRASS, 2));
        addCreativeItem(Item.get(ItemValues.DEAD_BUSH, 0));
        addCreativeItem(Item.get(ItemValues.SAPLING, 0));
        addCreativeItem(Item.get(ItemValues.SAPLING, 1));
        addCreativeItem(Item.get(ItemValues.SAPLING, 2));
        addCreativeItem(Item.get(ItemValues.SAPLING, 3));
        addCreativeItem(Item.get(ItemValues.SAPLING, 4));
        addCreativeItem(Item.get(ItemValues.SAPLING, 5));
        addCreativeItem(Item.get(ItemValues.LEAVES, 0));
        addCreativeItem(Item.get(ItemValues.LEAVES, 1));
        addCreativeItem(Item.get(ItemValues.LEAVES, 2));
        addCreativeItem(Item.get(ItemValues.LEAVES, 3));
        addCreativeItem(Item.get(ItemValues.LEAVES2, 0));
        addCreativeItem(Item.get(ItemValues.LEAVES2, 1));
        addCreativeItem(Item.get(ItemValues.CAKE, 0));
        addCreativeItem(Item.get(ItemValues.SIGN, 0));
        addCreativeItem(Item.get(ItemValues.MONSTER_SPAWNER, 0));
        addCreativeItem(Item.get(ItemValues.WOOL, 0));
        addCreativeItem(Item.get(ItemValues.WOOL, 7));
        addCreativeItem(Item.get(ItemValues.WOOL, 6));
        addCreativeItem(Item.get(ItemValues.WOOL, 5));
        addCreativeItem(Item.get(ItemValues.WOOL, 4));
        addCreativeItem(Item.get(ItemValues.WOOL, 3));
        addCreativeItem(Item.get(ItemValues.WOOL, 2));
        addCreativeItem(Item.get(ItemValues.WOOL, 1));
        addCreativeItem(Item.get(ItemValues.WOOL, 15));
        addCreativeItem(Item.get(ItemValues.WOOL, 14));
        addCreativeItem(Item.get(ItemValues.WOOL, 13));
        addCreativeItem(Item.get(ItemValues.WOOL, 12));
        addCreativeItem(Item.get(ItemValues.WOOL, 11));
        addCreativeItem(Item.get(ItemValues.WOOL, 10));
        addCreativeItem(Item.get(ItemValues.WOOL, 9));
        addCreativeItem(Item.get(ItemValues.WOOL, 8));
        addCreativeItem(Item.get(ItemValues.CARPET, 0));
        addCreativeItem(Item.get(ItemValues.CARPET, 7));
        addCreativeItem(Item.get(ItemValues.CARPET, 6));
        addCreativeItem(Item.get(ItemValues.CARPET, 5));
        addCreativeItem(Item.get(ItemValues.CARPET, 4));
        addCreativeItem(Item.get(ItemValues.CARPET, 3));
        addCreativeItem(Item.get(ItemValues.CARPET, 2));
        addCreativeItem(Item.get(ItemValues.CARPET, 1));
        addCreativeItem(Item.get(ItemValues.CARPET, 15));
        addCreativeItem(Item.get(ItemValues.CARPET, 14));
        addCreativeItem(Item.get(ItemValues.CARPET, 13));
        addCreativeItem(Item.get(ItemValues.CARPET, 12));
        addCreativeItem(Item.get(ItemValues.CARPET, 11));
        addCreativeItem(Item.get(ItemValues.CARPET, 10));
        addCreativeItem(Item.get(ItemValues.CARPET, 9));
        addCreativeItem(Item.get(ItemValues.CARPET, 8));
        //Tools
        //TODO addCreativeItem(Item.get(ItemValues.RAILS, 0));
        //TODO addCreativeItem(Item.get(ItemValues.POWERED_RAILS, 0));
        addCreativeItem(Item.get(ItemValues.TORCH, 0));
        addCreativeItem(Item.get(ItemValues.BUCKET, 0));
        addCreativeItem(Item.get(ItemValues.BUCKET, 1));
        addCreativeItem(Item.get(ItemValues.BUCKET, 8));
        addCreativeItem(Item.get(ItemValues.BUCKET, 10));
        addCreativeItem(Item.get(ItemValues.TNT, 0));
        addCreativeItem(Item.get(ItemValues.IRON_HOE, 0));
        addCreativeItem(Item.get(ItemValues.IRON_SHOVEL, 0));
        addCreativeItem(Item.get(ItemValues.IRON_SWORD, 0));
        addCreativeItem(Item.get(ItemValues.BOW, 0));
        addCreativeItem(Item.get(ItemValues.SHEARS, 0));
        addCreativeItem(Item.get(ItemValues.FLINT_AND_STEEL, 0));
        addCreativeItem(Item.get(ItemValues.CLOCK, 0));
        addCreativeItem(Item.get(ItemValues.COMPASS, 0));
        addCreativeItem(Item.get(ItemValues.MINECART, 0));
        //addCreativeItem(Item.get(ItemValues.SPAWN_EGG, Villager::NETWORK_ID));
        
        //addCreativeItem(Item.get(ItemValues.SPAWN_EGG, 10)); //Chicken
        //addCreativeItem(Item.get(ItemValues.SPAWN_EGG, 11)); //Cow
        //addCreativeItem(Item.get(ItemValues.SPAWN_EGG, 12)); //Pig
        //addCreativeItem(Item.get(ItemValues.SPAWN_EGG, 13)); //Sheep
        //TODO: Wolf
        //TODO: Mooshroom
        //TODO: Creeper
        //TODO: Enderman
        //TODO: Silverfish
        //TODO: Skeleton
        //TODO: Slime
        //addCreativeItem(Item.get(ItemValues.SPAWN_EGG, Zombie::NETWORK_ID));
        
        //TODO: PigZombie
        //addCreativeItem(Item.get(ItemValues.SPAWN_EGG, Squid::NETWORK_ID));
        
        addCreativeItem(Item.get(ItemValues.SNOWBALL, 0));
        //Seeds
        addCreativeItem(Item.get(ItemValues.SUGARCANE, 0));
        addCreativeItem(Item.get(ItemValues.WHEAT, 0));
        addCreativeItem(Item.get(ItemValues.SEEDS, 0));
        addCreativeItem(Item.get(ItemValues.MELON_SEEDS, 0));
        addCreativeItem(Item.get(ItemValues.PUMPKIN_SEEDS, 0));
        addCreativeItem(Item.get(ItemValues.CARROT, 0));
        addCreativeItem(Item.get(ItemValues.POTATO, 0));
        addCreativeItem(Item.get(ItemValues.BEETROOT_SEEDS, 0));
        addCreativeItem(Item.get(ItemValues.EGG, 0));
        addCreativeItem(Item.get(ItemValues.RAW_FISH, 0));
        addCreativeItem(Item.get(ItemValues.RAW_FISH, 1));
        addCreativeItem(Item.get(ItemValues.RAW_FISH, 2));
        addCreativeItem(Item.get(ItemValues.RAW_FISH, 3));
        addCreativeItem(Item.get(ItemValues.COOKED_FISH, 0));
        addCreativeItem(Item.get(ItemValues.COOKED_FISH, 1));
        addCreativeItem(Item.get(ItemValues.DYE, 0));
        addCreativeItem(Item.get(ItemValues.DYE, 7));
        addCreativeItem(Item.get(ItemValues.DYE, 6));
        addCreativeItem(Item.get(ItemValues.DYE, 5));
        addCreativeItem(Item.get(ItemValues.DYE, 4));
        addCreativeItem(Item.get(ItemValues.DYE, 3));
        addCreativeItem(Item.get(ItemValues.DYE, 2));
        addCreativeItem(Item.get(ItemValues.DYE, 1));
        addCreativeItem(Item.get(ItemValues.DYE, 15));
        addCreativeItem(Item.get(ItemValues.DYE, 14));
        addCreativeItem(Item.get(ItemValues.DYE, 13));
        addCreativeItem(Item.get(ItemValues.DYE, 12));
        addCreativeItem(Item.get(ItemValues.DYE, 11));
        addCreativeItem(Item.get(ItemValues.DYE, 10));
        addCreativeItem(Item.get(ItemValues.DYE, 9));
        addCreativeItem(Item.get(ItemValues.DYE, 8));
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
            Block block = new Block(id, metadata, count);
            return block;
        } else {
            return new Item(id, metadata, count);
        }
        return null;
    }

    public static Item get(int id, int metadata){
        if(list.containsKey(id)){
            Class<? extends Item> clazz = list.get(id);
            try {
                return clazz.getConstructor(int.class, short.class, int.class).newInstance(id, (short) metadata, 1);
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
            Block block = new Block(id, (short) metadata, 1);
            return block;
        } else {
            return new Item(id, (short) metadata, 1);
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
