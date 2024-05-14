package com.example.examplemod.dictionary;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DictionaryDataManager {


    public DictionaryDataManager() {

    }

    private static void loadCreativeTabs(){
        try {
            Path dictionaryPath = Paths.get("./dictionary");
            Files.createDirectories(dictionaryPath);
            for(CreativeModeTab creativeModeTab : CreativeModeTabs.allTabs()){
                String tabName = creativeModeTab.getDisplayName().getString();
                Files.createFile(dictionaryPath.resolve(tabName+".json"));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadCategoryList(){

    }

    private static void saveCategoryList(){

    }

}