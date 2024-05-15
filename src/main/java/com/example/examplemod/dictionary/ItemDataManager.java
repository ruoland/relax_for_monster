package com.example.examplemod.dictionary;

import com.example.examplemod.ExampleMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.stats.RecipeBook;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ItemDataManager {
    //아이템을 소분류로 나눠서
    private static TreeMap<String, ItemSubData> itemSubDataMap = new TreeMap<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path DIRECTORY_PATH = Paths.get("./dictionary");

    public static void saveSubData(){
        Set<String> subNames = itemSubDataMap.keySet();
        try {
            for(String subName : subNames) {

                Path subDataFile = DIRECTORY_PATH.resolve("./"+subName+".json");
                subDataFile.toFile().createNewFile();

                BufferedWriter writer = Files.newBufferedWriter(subDataFile);
                writer.write(GSON.toJson(itemSubDataMap.get(subName)));
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadSubData(){
        try {
            for(Path subDataFile : Files.list(DIRECTORY_PATH).toList()){
                GSON.fromJson(Files.readString(subDataFile).toString(), TreeMap.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Set<String> getSubNames(){
        return itemSubDataMap.keySet();
    }
    public static void loadMinecraftItem(){
        ItemStack[] itemStacks = findItemStacks();

        for(ItemStack itemStack : itemStacks) {
            String simpleName = getItemType(itemStack);
            addSub(simpleName);
        }
        for(ItemStack itemStack : itemStacks) {
            String simpleName = getItemType(itemStack);

            if(simpleName.equals(""))
                continue;

            getItemSubData(simpleName).addItem(itemStack);
        }
    }

    private static void addSub(String sub){
        ItemSubData subData = itemSubDataMap.containsKey(sub) ? itemSubDataMap.get(sub) :  new ItemSubData(sub);
        itemSubDataMap.put(sub, subData);
    }

    public static ItemSubData getItemSubData(String subName){
        return itemSubDataMap.get(subName);
    }
    public static String getItemContent(ItemStack itemStack){
        return getItemSubData(getItemType(itemStack)).getContent(itemStack);
    }

    public static String getItemType(ItemStack itemStack){
        return getItemType(itemStack.getItem());
    }

    public static String getItemType(Item item){
        String name = getClassName(item);
        int itemIndex = getStringIndex(item);
        if(itemIndex == -1)
            return "";
        return checkItem(name.substring(0, itemIndex), item).name().toLowerCase();
    }

    private static String getClassName(Item item) {
        if(item instanceof BlockItem) {
            String name;
            BlockItem blockItem = (BlockItem) item;

            name = blockItem.getBlock().getClass().getSimpleName();
            return name;
        }
        return item.getClass().getSimpleName();
    }

    private static int getStringIndex(Item item) {
        if(getClassName(item).equals(""))
            return -1;
        int indexI = getClassName(item).lastIndexOf("Item");
        int indexB = getClassName(item).lastIndexOf("Block");
        return indexI != -1 ? indexI : indexB;
    }

    private static ItemStack[] findItemStacks(){
        Field[] itemFields = Items.class.getFields();
        ItemStack[] itemStack = new ItemStack[itemFields.length];
        try {
            for(int i = 0; i < itemFields.length; i++){
                if(itemFields[i].getType() == Item.class){
                    itemStack[i] = new ItemStack((Item) itemFields[i].get(null));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return itemStack;
    }

    private static EnumCategory checkItem(String itemType, Item item){

        EnumCategory[] values = EnumCategory.values();
        for(EnumCategory enumCategory : values){
            if(enumCategory.containsKey(itemType)){
                return enumCategory;
            }
        }

        return EnumCategory.ETC;
    }
}
