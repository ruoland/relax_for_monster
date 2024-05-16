package com.example.examplemod.dictionary.developer;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class TypeData {
    private ArrayList<String> valueList = new ArrayList<>();

    public void add(String item)
    {
        valueList.add(item);
    }


    public ArrayList<String > getList() {
        return  valueList;
    }

    public String getOther(ItemStack itemStack) {
        return itemStack.getDescriptionId();
    }
}
