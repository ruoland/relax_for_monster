package com.example.examplemod.dictionary.developer;

public enum EnumCategory {

    BATTLE("Sword", "Trident", "Shield", "Bow", "Arrow", "SpectralArrow", "TippedArrow", "Crossbow", "Armor", "ArmorStand"),
    TOOL("Pickaxe", "Axe", "Hoe", "Shovel", "FishingRod", "FlintAndSteel", "Shears", "Lead", "Saddle", "NameTag", "FoodOnAStick", "Spyglass"),
    BLOCK("BannerPattern", "Banner", "Sign", "HangingSign", "Block", "Bed", "DoubleHighBlock", "HangingEntity", "ItemNameBlock", "StandingAndWallBlock"),
    ADVENTURE("Skull", "Potion", "SplashPotion", "Map", "EmptyMap", "Compass", "Boat"),
    NATURE("SugarCane","BambooStalk", "Mushroom","VineBlock","WaterlilyBlock", "GlowInkSac", "Snowball", "Brush", "InkSac", "BoneMeal", "PlaceOnWaterBlock", "Flower"
    , "BigDripleaf", "HangingRoots", "Kelp", "TwistingVines", "WeepingVines", "SporeBlossom", "Seagrass", "DeadBush", "Azalea", "TallGrass"
    , "Leaves", "MangroveLeaves", "CherryLeaves", "MangroveRoots", "Sapling", "Mud", "Grass", "Beehive", "CaveVines"
    , "Cherry"),
    COLOR("contains", "Red", "Blue", "Magenta", "Cherry"),
    FARM("SugarCane"),
    DECORATION("Slab", "ChiseledBookShelf", "Fence", "Ladder", "Stair", "PressurePlate", "Door", "DecoratedPotBlock"
    , "RotatedPillar", "Wall", "Candle", "Campfire", "Lantern", "Bell"),
    WOOL("Wool", "WoolCarpet", "Carpet", "Bed"),
    SIGN("StandingSign", "CeilingHangingSign"),
    NETHER("Nether_"),
    ETC("GameMaster", "EndCrystal", "EnderEye", "Enderpearl", "ChorusFruit", "Elytra", "Instrument", "DiscFragment", "Record", "FireworkRocket", "FireworkStar", "MilkBucket", "Bucket", "MobBucket", "Book", "WritableBook", "EnchantedBook", "Egg", "SpawnEgg", "PlayerHead", "FireCharge", "AnimalArmor", "ExperienceBottle", "SuspiciousStew", "SmithingTemplate", "ScaffoldingBlock", "Bottle");

    private String[] strings;
    EnumCategory(String... strings) {
        this.strings = strings;
    }

    String[] getTypes(){
        return strings;
    }

    public boolean containsKey(String keyword){
        for(String key : strings){
            if(key.equals(keyword))
                return true;
        }
        return false;
    }
}
