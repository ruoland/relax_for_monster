package com.example.examplemod.dictionary.developer.category;

import com.example.examplemod.dictionary.LangManager;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.ItemStack;

public class ItemGroupContent implements IContent{
    private transient ItemStack itemStack; //대상 아이템
    @SerializedName("아이템 그룹")
    String itemGroup = "";
    @SerializedName("아이템 그룹에 대한 설명")
    String dictionary = "설명";

    ItemGroupContent(ItemStack itemStack) {
        this.itemStack = itemStack;

        TagManager.getTagManager().getItemID(itemStack);
    }

    public String getItemID() {
        return itemID;
    }

    public String getDictionary() {
        return dictionary;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    class Item{
        @SerializedName("아이템 이름")
        private String name = ItemManager.getItemStackMap().get(itemStack).getItem().getDescriptionId();

        @SerializedName("아이템 설명")
        private String dictionary = "설명";

        public String getDictionary() {
            return dictionary;
        }

        public String getName() {
            return name;
        }
    }
}
