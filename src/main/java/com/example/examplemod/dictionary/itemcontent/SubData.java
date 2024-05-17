package com.example.examplemod.dictionary.itemcontent;

import com.example.examplemod.dictionary.developer.category.TagManager;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;

import java.util.TreeMap;

public class SubData {
    //@SerializedName("태그")
    private transient EnumTag tag;
    @SerializedName("아이템들 설명")
    private String subDictionary = "이 태그에 대한 설명을 여기에 적어주세요.";

    @SerializedName("모든 설명을 태그 설명으로 대체")
    private boolean canReplace=true;

    @SerializedName("아이템 목록")
    private TreeMap<String, ItemGroupContent> itemGroupContentMap = new TreeMap<>();

    SubData(EnumTag tag){
        this.tag = tag;
    }

    /**
     중간 카테고리의 설명
     */
    public String getSubDictionary() {
        return subDictionary;
    }

    public boolean isReplace() {
        return canReplace;
    }

    public void addItemContent(ItemStack itemStack){
        String itemGroupCutID = TagManager.getTagManager().getItemCutID(itemStack);
        ItemGroupContent groupItemContent = itemGroupContentMap.get(itemGroupCutID);

        if(groupItemContent == null) {
            groupItemContent = new ItemGroupContent(itemGroupCutID);
            itemGroupContentMap.put(itemGroupCutID, groupItemContent);
        }

        //그룹 아이템 내에서는 아이템 스택 아이디로
        groupItemContent.add(itemStack);
    }

    public boolean hasGroup(ItemStack itemStack){
        return getItemGroup(itemStack) != null;
    }
    public boolean hasItem(ItemStack itemStack){
        ItemGroupContent groupContent = getItemGroup(itemStack);
        if(groupContent == null)
            return false;
        return groupContent.hasItem(itemStack);
    }



    public ItemGroupContent getItemGroup(ItemStack itemStack){
        return itemGroupContentMap.get(TagManager.getTagManager().getItemCutID(itemStack));
    }
    public TreeMap<String, ItemGroupContent> getGroupMap() {
        return itemGroupContentMap;
    }
}
