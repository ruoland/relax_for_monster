package com.example.examplemod.dictionary.developer.category;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TagManager {
    private static final EnumMap<EnumTag, ItemTag> tagEnumMap = new EnumMap<>(EnumTag.class);
    private static final TagManager TAG_MANAGER;
    static {
        TAG_MANAGER = new TagManager();
    }

    public static TagManager getTagManager() {
        return TAG_MANAGER;
    }

    public void saveTag() throws IOException {
        for (EnumTag tag : EnumTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            tagFile.toFile().createNewFile();
            Data.saveJson(tagFile, getTagManager().getItemTag(tag));
        }
    }
    public void loadTag() {
        for (EnumTag tag : EnumTag.values()) {
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag + ".json"));
            if (Files.exists(tagFile))
                tagEnumMap.put(tag, (ItemTag) Data.readJson(tagFile, ItemTag.class));
        }
        if(tagEnumMap.isEmpty()){
            for(EnumTag tag : EnumTag.values()){
                tagEnumMap.put(tag, new ItemTag(tag));
            }
            tagging();
        }
    }

    public void tagging(){
        for (ItemStack itemStack : ItemManager.getItemList()) {
            ItemTag itemTag = getItemTag(getTag(itemStack));
            SubData sub = itemTag.getSubData();
            ExampleMod.LOGGER.info(itemTag.getTagName()+"");
            if (!sub.hasItem(itemStack)) {
                sub.addItemContent(itemStack);
            }
        }
    }

    public ItemTag getItemTag(EnumTag enumTag){
        return tagEnumMap.get(enumTag);
    }

    public ItemTag getItemTag(ItemStack itemStack){
        return getItemTag(getTag(itemStack));
    }
    public SubData getItemSub(ItemStack itemStack){
        return getItemTag(getTag(itemStack)).getSubData();
    }
    public SubData getItemSub(EnumTag enumTag){
        return getItemTag(enumTag).getSubData();
    }


    public EnumTag getTag(ItemStack itemStack){
        String itemID = itemStack.getDescriptionId();
        itemID = itemID.substring(itemID.indexOf("minecraft.") + 10);
        String[] split = itemID.split("_");
        if(split.length > 0) {
            for(EnumTag enumCombine : EnumTag.values()) {
                if(itemID.contains("crimson") || itemID.contains("quartz"))
                    return EnumTag.NETHER;
                else if(itemID.contains("mangrove"))
                    return EnumTag.WOOD;
                else if(itemID.contains("coral") || itemID.contains("kelp"))
                    return EnumTag.CORAL;
                if (enumCombine.containsKey(getItemID(itemStack))) {
                    return enumCombine;
                }
            }
        }

        return EnumTag.ETC;
    }

    /**
     * 앞 글자나 뒷 글자로 아이디 가져오기.
     * */
    public String getItemID(ItemStack itemStack) {
        String itemID = itemStack.getDescriptionId();
        itemID = itemID.substring(itemID.indexOf("minecraft.") + 10);
        String[] split = itemID.split("_");
        if (split.length > 0) {
            String postfix = split[split.length-1];
            String prefix = split[0];
            for(EnumTag enumTag : EnumTag.values()){
                if(enumTag.containsKey(postfix))
                    return postfix;
                else if(enumTag.containsKey(prefix))
                    return prefix;
                return getTag(itemStack).name();
            }
        }
        return itemID;
    }

}