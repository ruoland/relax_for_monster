package com.example.examplemod.dictionary;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.HashMap;

public class DictionaryCategoryJson {
    private CreativeModeTab creativeModeTab;

    private JsonObject jsonType = new JsonObject();

    private JsonObject jsonDictionary = new JsonObject();
    private JsonObject jsonItem = new JsonObject();
    private JsonObject jsonContent = new JsonObject();
    public DictionaryCategoryJson(CreativeModeTab creativeModeTab){
        this.creativeModeTab = creativeModeTab;
        jsonDictionary.add("item", jsonItem);
        jsonDictionary.add("content", jsonContent);
        jsonType.add("type", jsonDictionary);
    }

    /**
     * 아이템에
     */
    public void putItem(Item item){
        jsonObject.addProperty();
    }

    /**
     * 해당 아이템에 대한 도감 설명을 불러오기
     */
    public String get(Item item){

    }

}
