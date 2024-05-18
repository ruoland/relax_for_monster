package com.example.examplemod.dictionary.developer.category;

import net.minecraft.world.item.ItemStack;

import java.nio.file.Path;
import java.util.ArrayList;

public class FileManager implements Data{
    private static final Path BLACK_LIST_FILE = DIRECTORY_PATH.resolve("blacklist.json");
    private static ArrayList<String> BLACK_LIST = new ArrayList<>();

    @Override
    public void init() {
        Data.super.init();
        DIRECTORY_PATH.toFile().mkdir();
        createFile(BLACK_LIST_FILE);

    }


    public static void blackListLoad(){
        BLACK_LIST = (ArrayList<String>) Data.readJson(BLACK_LIST_FILE, ArrayList.class);
    }

    public static boolean checkBlackList(ItemStack itemStack){
        return BLACK_LIST.contains(itemStack.getDescriptionId());
    }

}
