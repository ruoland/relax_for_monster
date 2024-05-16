package com.example.examplemod.dictionary;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.LanguageHook;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LangManager {
    private static Map<String, String> languageMap = new HashMap<>();

    public static String getEnglishName(ItemStack itemStack){
        if(languageMap.isEmpty())
            loadLanguageMap();
        if(itemStack == null)
            return "?";
        return (languageMap.get(itemStack.getDescriptionId()));
    }
    /**
     * init 메서드가 실행 될 때 실행해야 합니다!
     */
    public static void loadLanguageMap(){
        try {
            Field tableField = LanguageHook.class.getDeclaredField("defaultLanguageTable");
            tableField.setAccessible(true);
            languageMap = (Map<String, String>) tableField.get(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
