"""
Script that takes in a section of PocketMine-MP source code, and creates Java classes from it.
Copyright (C) 2015 PocketMine Team
Copyright (C) 2015 RedstoneLamp Project

The code under the python variable "raw" is from the PocketMine project.
You can find the original source code here: https://github.com/PocketMine/PocketMine-MP/blob/master/src/pocketmine/item/Item.php

PocketMine-MP is released under the GNU LESSER GENERAL PUBLIC LICENSE version 3.
You can find it here: https://github.com/PocketMine/PocketMine-MP/blob/master/LICENSE
"""

raw = """
self::$list[self::SUGARCANE] = Sugarcane::class;
self::$list[self::WHEAT_SEEDS] = WheatSeeds::class;
self::$list[self::PUMPKIN_SEEDS] = PumpkinSeeds::class;
self::$list[self::MELON_SEEDS] = MelonSeeds::class;
self::$list[self::MUSHROOM_STEW] = MushroomStew::class;
self::$list[self::BEETROOT_SOUP] = BeetrootSoup::class;
self::$list[self::CARROT] = Carrot::class;
self::$list[self::POTATO] = Potato::class;
self::$list[self::BEETROOT_SEEDS] = BeetrootSeeds::class;
self::$list[self::SIGN] = Sign::class;
self::$list[self::WOODEN_DOOR] = WoodenDoor::class;
self::$list[self::BUCKET] = Bucket::class;
self::$list[self::IRON_DOOR] = IronDoor::class;
self::$list[self::CAKE] = Cake::class;
self::$list[self::BED] = Bed::class;
self::$list[self::PAINTING] = Painting::class;
self::$list[self::COAL] = Coal::class;
self::$list[self::APPLE] = Apple::class;
self::$list[self::SPAWN_EGG] = SpawnEgg::class;
self::$list[self::DIAMOND] = Diamond::class;
self::$list[self::STICK] = Stick::class;
self::$list[self::SNOWBALL] = Snowball::class;
self::$list[self::BOWL] = Bowl::class;
self::$list[self::FEATHER] = Feather::class;
self::$list[self::BRICK] = Brick::class;
self::$list[self::LEATHER_CAP] = LeatherCap::class;
self::$list[self::LEATHER_TUNIC] = LeatherTunic::class;
self::$list[self::LEATHER_PANTS] = LeatherPants::class;
self::$list[self::LEATHER_BOOTS] = LeatherBoots::class;
self::$list[self::CHAIN_HELMET] = ChainHelmet::class;
self::$list[self::CHAIN_CHESTPLATE] = ChainChestplate::class;
self::$list[self::CHAIN_LEGGINGS] = ChainLeggings::class;
self::$list[self::CHAIN_BOOTS] = ChainBoots::class;
self::$list[self::IRON_HELMET] = IronHelmet::class;
self::$list[self::IRON_CHESTPLATE] = IronChestplate::class;
self::$list[self::IRON_LEGGINGS] = IronLeggings::class;
self::$list[self::IRON_BOOTS] = IronBoots::class;
self::$list[self::GOLD_HELMET] = GoldHelmet::class;
self::$list[self::GOLD_CHESTPLATE] = GoldChestplate::class;
self::$list[self::GOLD_LEGGINGS] = GoldLeggings::class;
self::$list[self::GOLD_BOOTS] = GoldBoots::class;
self::$list[self::DIAMOND_HELMET] = DiamondHelmet::class;
self::$list[self::DIAMOND_CHESTPLATE] = DiamondChestplate::class;
self::$list[self::DIAMOND_LEGGINGS] = DiamondLeggings::class;
self::$list[self::DIAMOND_BOOTS] = DiamondBoots::class;
self::$list[self::IRON_SWORD] = IronSword::class;
self::$list[self::IRON_INGOT] = IronIngot::class;
self::$list[self::GOLD_INGOT] = GoldIngot::class;
self::$list[self::IRON_SHOVEL] = IronShovel::class;
self::$list[self::IRON_PICKAXE] = IronPickaxe::class;
self::$list[self::IRON_AXE] = IronAxe::class;
self::$list[self::IRON_HOE] = IronHoe::class;
self::$list[self::DIAMOND_SWORD] = DiamondSword::class;
self::$list[self::DIAMOND_SHOVEL] = DiamondShovel::class;
self::$list[self::DIAMOND_PICKAXE] = DiamondPickaxe::class;
self::$list[self::DIAMOND_AXE] = DiamondAxe::class;
self::$list[self::DIAMOND_HOE] = DiamondHoe::class;
self::$list[self::GOLD_SWORD] = GoldSword::class;
self::$list[self::GOLD_SHOVEL] = GoldShovel::class;
self::$list[self::GOLD_PICKAXE] = GoldPickaxe::class;
self::$list[self::GOLD_AXE] = GoldAxe::class;
self::$list[self::GOLD_HOE] = GoldHoe::class;
self::$list[self::STONE_SWORD] = StoneSword::class;
self::$list[self::STONE_SHOVEL] = StoneShovel::class;
self::$list[self::STONE_PICKAXE] = StonePickaxe::class;
self::$list[self::STONE_AXE] = StoneAxe::class;
self::$list[self::STONE_HOE] = StoneHoe::class;
self::$list[self::WOODEN_SWORD] = WoodenSword::class;
self::$list[self::WOODEN_SHOVEL] = WoodenShovel::class;
self::$list[self::WOODEN_PICKAXE] = WoodenPickaxe::class;
self::$list[self::WOODEN_AXE] = WoodenAxe::class;
self::$list[self::WOODEN_HOE] = WoodenHoe::class;
self::$list[self::FLINT_STEEL] = FlintSteel::class;
self::$list[self::SHEARS] = Shears::class;
self::$list[self::BOW] = Bow::class;
self::$list[self::RAW_FISH] = Fish::class;
self::$list[self::COOKED_FISH] = CookedFish::class;
"""

items = []

f = open("list.txt", 'r')
lines = f.readlines()
f.close()

for line in lines:
    line = line.replace("\n", "")
    line = line.replace("self::$list[", "")
    line = line.replace("]", "")
    line = line.split(" ")[0].strip()
    line = line.replace("self::", "")
    line = line.lower()
    l = list(line)
    l[0] = l[0].upper()
    counter = 0
    for char in l:
        counter = counter + 1
        if char == "_":
            l[counter] = l[counter].upper()
    line = "".join(l)
    line = line.replace("_", "")

    data = """package redstonelamp.item;

public class """+line+""" extends Item {
    public """+line+"""(int id, short metadata, int count) {
        super(id, metadata, count);
    }
}
"""

    f2 = open("src\\redstonelamp\\item\\"+line+".java", 'w')
    f2.write(data)
    f2.close()
    print "Wrote "+line+".java"

print "=============COMPLETE"
