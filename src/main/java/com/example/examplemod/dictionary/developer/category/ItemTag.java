package com.example.examplemod.dictionary.developer.category;

import java.util.EnumMap;

public class ItemTag {
    private final EnumMap<String, SubData> tagSubMap = new EnumMap<>(EnumTag.class);
    transient String thisName;
    transient String tag;
    public ItemTag(EnumTag tag){
        this.thisName = tag.name();
        tagSubMap.put(tag, new SubData(tag));
    }

    public String getTagName() {
        return thisName;
    }

    public SubData getSubData() {
        return tagSubData;
    }
}
