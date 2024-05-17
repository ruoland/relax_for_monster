package com.example.examplemod.dictionary.itemcontent;

import com.example.examplemod.dictionary.LangManager;
import com.example.examplemod.dictionary.developer.category.IContent;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;

public class ItemContent implements IContent {
    private transient ItemStack itemStack; //대상 아이템
    @SerializedName("아이템 아이디")
    String itemID = "";
    @SerializedName("아이템 영어 이름")
    String englishName = LangManager.getEnglishName(itemStack);

    @SerializedName("아이템에 대한 설명")
    String dictionary = "설명";

    ItemContent(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemID = itemStack.getDescriptionId();
    }

    public String getItemID() {
        return itemID;
    }

    public String getDictionary() {
        return dictionary;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

}
