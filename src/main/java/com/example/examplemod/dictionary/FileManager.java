package com.example.examplemod.dictionary;

import com.example.examplemod.dictionary.developer.category.Data;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class FileManager implements Data {
    private static ArrayList<String> BLACK_LIST = new ArrayList<>();
    private static final FileManager FILE_MANAGER = new FileManager();
    public static FileManager getInstance() {
        return FILE_MANAGER;
    }
    @Override
    public void init() {
        Data.super.init();

    }



    public static boolean checkBlackList(ItemStack itemStack){
        return BLACK_LIST.contains(itemStack.getDescriptionId());
    }

}
