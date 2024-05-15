package com.example.examplemod.dictionary;

import net.minecraft.world.item.ItemStack;
import org.apache.commons.codec.language.bm.Lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class ItemSubData {
    //이 클래스가 관리하는 카테고리 이름
    private final String subName;
    private String categorySummary = "카테고리 설명을 여기에 넣기";


    //아이템의 이름과 설명이 여기에 담김
    private final TreeMap<String, ItemContent> itemDictionaryMap = new TreeMap<>();


    public ItemSubData(String subName){
        this.subName = subName;
    }

    public void addItem(ItemStack itemStack, String dictionaryContent){
        ItemContent content = new ItemContent(itemStack);
        content.setContent(dictionaryContent);
        itemDictionaryMap.put(itemStack.getDescriptionId(), content);

    }

    public void addItem(ItemStack itemStack){
        addItem(itemStack, "등록된 정보 없음");
    }

    public String getSubName() {
        return subName;
    }

    public String getCategorySummary() {
        return categorySummary;
    }

    public String getContent(ItemStack itemStack){
        return itemDictionaryMap.get(itemStack.getDescriptionId()).getContent();
    }
    public boolean contains(ItemStack itemStack){
        return itemDictionaryMap.containsKey(itemStack.getDescriptionId());
    }

    @Override
    public String toString() {
        return "ItemSubData{" +
                "subName='" + subName + '\'' +
                ", itemDictionaryMap=" + itemDictionaryMap +
                ", categorySummary='" + categorySummary + '\'' +
                '}';
    }

    public void setDefaultCategory(){

    }

    class ItemContent{
        private String englishName;
        private String content;
        public ItemContent(ItemStack itemStack){
            this.englishName = LangManager.getEnglishName(itemStack);
        }

        public void setContent(String content){
            this.content = content;
        }

        public String getContent(){
            return content;
        }

        @Override
        public String toString() {
            return "ItemContent{" +
                    "displayName='" + englishName + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
