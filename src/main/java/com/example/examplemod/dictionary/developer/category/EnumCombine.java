package com.example.examplemod.dictionary.developer.category;

import com.example.examplemod.dictionary.developer.EnumCategory;

public enum EnumCombine {
    ARMOR("helmet", "chestplate", "leggings", "boots"),
    TOOLS("axe", "pickaxe", "shovel", "hoe"),
    CONCRETE("concrete", "powder"),
    FENCE("fence", "gate"),
    BANNER("banner", "pattern"),
    PLATE("plate", ""),
    WOOD("wood", "log"),
    WOOL("carpet", "wool"),
    CORAL("coral", "fan"),
    NETHER("crimson", "warped");



    private String[] strings;
    EnumCombine(String... str){
        this.strings = str;
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
