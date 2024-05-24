package com.example.examplemod.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;

import java.util.EnumMap;

//파일 이름
public class ItemTag {
    @SerializedName("이 태그에 존재하는 아이템들")
    private final EnumMap<EnumTag, SubData> tagSubMap = new EnumMap<>(EnumTag.class);
    //파일 이름
    transient String thisName;

    EnumTag tag;
    public ItemTag(EnumTag tag){
        this.thisName = tag.name();
        this.tag = tag;

        tagSubMap.put(tag, new SubData(tag));
        System.out.println(tag+ "의 서브 데이터" + getSubData());
    }

    public String getTagName() {
        return thisName;
    }

    public SubData getSubData() {
        return tagSubMap.get(tag);
    }
}
