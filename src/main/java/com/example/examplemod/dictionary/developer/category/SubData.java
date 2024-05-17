package com.example.examplemod.dictionary.developer.category;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.TreeMap;

public class SubData {
    @SerializedName("태그")
    private EnumTag tag;
    @SerializedName("태그 설명")
    private String subDictionary = "이 태그에 대한 설명을 여기에 적어주세요.";

    @SerializedName("아이템 목록")
    private TreeMap<String, ItemContent> itemContentList = new TreeMap<>();

    SubData(EnumTag tag){
        this.tag = tag;
    }
    SubData(EnumTag tag, ItemStack itemStack){
        this(tag);;
        addItemContent(itemStack);
    }

    public String getSubDictionary() {
        return subDictionary;
    }

    public void addItemContent(ItemStack itemStack){
        this.itemContentList.put(itemStack.getDescriptionId(), new ItemContent(itemStack));
    }
    public void addItemContent(Map<String , ItemContent> map){
        itemContentList.putAll(map);
    }

    public boolean hasItem(ItemStack itemStack){
        return itemContentList.containsKey(itemStack.getDescriptionId());
    }

    public ItemContent getItemContent(ItemStack itemStack){
        return itemContentList.get(itemStack.getDescriptionId());
    }

    public TreeMap<String, ItemContent> getItemContentList() {
        return itemContentList;
    }
}
