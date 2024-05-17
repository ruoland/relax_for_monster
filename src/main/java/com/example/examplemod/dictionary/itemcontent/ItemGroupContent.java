package com.example.examplemod.dictionary.itemcontent;

import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

public class ItemGroupContent {
    @SerializedName("그룹 아이디")
    String groupID;
    
    @SerializedName("그룹 설명")
    String dictionary = "여기에 이 그룹에 대한 설명을 넣어주세요.";

    @SerializedName("이 그룹의 아이템들")
    private HashMap<String, ItemContent> itemContentMap = new HashMap<>();

    public ItemGroupContent(String groupID){
        this.groupID =groupID;
    }

    public HashMap<String, ItemContent> getContentMap() {
        return itemContentMap;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void add(ItemStack itemStack){
        itemContentMap.put(itemStack.getDescriptionId(), new ItemContent(itemStack));
    }

    public ItemContent getItemContent(ItemStack stack){
        return itemContentMap.get(stack.getDescriptionId());
    }

    public boolean hasItem(ItemStack itemStack){
        for(ItemContent content : itemContentMap.values()){
            if(itemStack.getDescriptionId().equals(content.itemID))
                return true;
        }
        return false;
    }
    public String getGroupID() {
        return groupID;
    }

}