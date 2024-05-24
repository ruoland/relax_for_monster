package com.example.examplemod.dictionary.itemcontent;

import java.util.EnumMap;

public enum EnumTag {

    ARMOR("helmet", "chestplate", "leggings", "boots", "armor"),
    TOOLS("axe", "pickaxe", "shovel", "hoe"),
    FOOD("beef", "cookie", "cake", "porkchop", "cooked", "bread", "apple", "chicken", "stew"),
    CONCRETE("concrete", "powder"),
    FENCE("fence", "gate", "wall"),
    BANNER("banner", "pattern"),
    PLATE("plate"),
    WOOD("wood", "log", "leaves", "planks"),
    WOOL("carpet", "wool"),
    CORAL("coral", "fan"),
    NETHER("crimson", "warped", "ghast", "blaze", "nether", "glowstone"),
    MUSIC("music", "disc"),
    ARROW("arrow"),
    FARM("seeds", "beetroot", "wheat", "berries", "potato", "carrot", "melon", "hay", "cane"),
    SAPLING("sapling"),
    BAMBOO("bamboo"),
    SIGN("sign"),
    SLAB("slab", "stairs"),
    DOOR("door", "trapdoor"),
    BED("bed"),
    GLASS("glass", "pane"),
    CANDLE("candle"),
    TERRACOTTA("terracotta"),
    CHORUS("chorus"),
    COPPER("copper", "oxidized", "weathered", "exposed"),
    ORE("ore", "raw", "ingot", "nugget"),
    SPAWN(""),
    DYE("dye"),
    BUCKET("bucket"),
    FISH("salmon", "fish", "pufferfish"),
    SHERD("sherd"),
    SWORD("sword"),
    BOAT("boat"),
    MINECART("minecart", "rail"),
    REDSTONE("button", "plate", "redstone", "lever", "dispenser", "dropper", "observer", "hopper", "piston", "repeater", "daylight", "lilac","lily"),
    BOX("box"),
    SANDSTONE("sandstone", "sand"),
    FLOWER("tulip", "daisy", "flower", "dandelion", "bush", "sunflower", "Allium", "Poppy", "Orchid", "Azure", "cornflower", "lily", "lilac", "rose", "peony"),
    DIRT("dirt", "grass", "gravel", "podzol"),
    POLISHED("polished"),
    POTION("potion"),
    TEMPLATE("template"),
    ORE_BLOCK("redstone_block", "lapis_block", "gold_block", "diamond_block", "copper_block"),
    ORE_ITEMS("raw", "coal_block"),
    ETC;

    private static final EnumMap<EnumTag, String> tagDictionary = new EnumMap<>(EnumTag.class);
    private final String[] strings;
    private ItemTag itemTag;

    EnumTag(String... str){
        this.strings = str;
    }

    public boolean containsKey(String keyword){
        for(String key : strings){
            if(keyword.equals(key))
                return true;
        }
        return false;
    }

    public ItemTag getItemTag(){
        if(itemTag == null)
            itemTag = new ItemTag(this);
        return itemTag;
    }

    public String getTagDictionary(){
        tagDictionary.put(ARMOR, "방어구입니다. 방어구는 가죽, 철, 금(내구성, 방어력 낮음), 다이아몬드, 네더라이트(최종 아이템) 아이템으" +
                "로 모자, 갑옷, 바지, 신발을 제작할 수 있으며, 세상을 모험하다 보면 사슬 갑옷도 얻을 수 있습니다.\\n" +
                "가죽 갑옷은 염료 아이템과 조합하여 염색할 수 있습니다. (가마솥에 물을 채우고, 우클릭 하면 원래대로 돌릴 수 있습니다)\\n" +
                "투구의 경우 방어력이 낮고, 거북 등딱지란 투구도 존재합니다.(거북 인갑으로 제작합니다.). \\n" +
                "갑옷은 방어력이 가장 높고, 또 재료도 많이 들어갑니다.\\n" +
                "바지의 경우 신속한 잠행이란 마법 부여를 가질 수 있습니다.(웅크린 상태에서 빠르게 이동하는 마법.)\\n" +
                " 신발의 경우 가벼운 착지나 물갈퀴, 차가운 걸음, 등 다양한 마법 부여를 가질 수 있습니다");

        tagDictionary.put(TOOLS, "게임에 필수적인 도구들입니다.");
        return "";


    }

}
