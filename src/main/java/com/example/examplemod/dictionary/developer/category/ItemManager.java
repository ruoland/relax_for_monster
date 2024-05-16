package com.example.examplemod.dictionary.developer.category;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.dictionary.LangManager;
import com.google.gson.annotations.SerializedName;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ItemManager {
    private static final HashMap<String, Sub> TAG_MAP = new HashMap<>();
    private static final ArrayList<ItemStack> ITEM_LIST = new ArrayList<>();
    private static final HashMap<String, ItemStack> ITEM_STACK_MAP = new HashMap<>();

    public static ItemStack[] loadItemStacks() {
        Field[] itemFields = Items.class.getFields();
        ItemStack[] itemStacks = new ItemStack[itemFields.length];
        TagManager.presetTag();
        try {
            for (int i = 0; i < itemFields.length; i++) {
                if (itemFields[i].getType() == Item.class) {
                    ItemStack itemStack = new ItemStack((Item) itemFields[i].get(null));
                    ITEM_STACK_MAP.put(itemStack.getDescriptionId(), itemStack);
                    ITEM_LIST.add(itemStack);
                    if (!FileManager.checkBlackList(itemStack)) {
                        if(!TagManager.hasTag(itemStack)) {
                            itemStacks[i] = itemStack;
                            if(itemStack.getItem() == Items.AIR || itemStack == null){
                                ExampleMod.LOGGER.info(itemStack+"없는 아이템!");
                                continue;
                            }

                            TagManager.addAutoTag(itemStacks[i]);

                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        TagManager.tagging();
        return itemStacks;
    }

    public static HashMap<String, ItemStack> getItemStackMap() {
        return ITEM_STACK_MAP;
    }

    public static HashMap<String, Sub> getTagMap() {
        return TAG_MAP;
    }
    public static TreeMap<String, ItemContent> getContentMap(ItemStack itemStack){
        return getTagMap().get(TagManager.getTag(itemStack)).getItemContentList();
    }

    public static void save()
    {
        for(String tag : TAG_MAP.keySet()){
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag+".json"));
            try {
                tagFile.toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Data.saveJson(tagFile, TAG_MAP.get(tag));
        }
    }

    public static void load()
    {
        for(String tag : TAG_MAP.keySet()){
            Path tagFile = Data.DIRECTORY_PATH.resolve(Paths.get(tag+".json"));
            if(Files.exists(tagFile))
                Data.readJson(tagFile, TAG_MAP.getClass());
        }
    }
    public static String getContent(ItemStack itemStack){
        Sub sub = TagManager.getItemSub(itemStack);
        ItemContent content = sub.getItemContent(itemStack);
        ExampleMod.LOGGER.info(itemStack+" - "+ sub +" - "+content);
        return content.getDictionary();
    }

    public static class TagManager {

        public static Sub getItemSub(ItemStack itemStack){
                return TAG_MAP.get(findTag(itemStack));
        }
        public static String getTag(ItemStack itemStack){
            return findTag(itemStack);
        }
        public static void presetTag() {
            for(EnumCombine enumCombine : EnumCombine.values()) {
                Sub sub = new Sub(enumCombine.name());
                TAG_MAP.put(enumCombine.name(), sub);
            }
        }
        public static void addAutoTag(ItemStack itemStack) {
            String foundTag = findTag(itemStack);
            Sub sub = TAG_MAP.containsKey(foundTag) ? TAG_MAP.get(foundTag) : new Sub(foundTag);
            if(!sub.hasItem(itemStack))
                sub.addItemContent(itemStack);
            TAG_MAP.put(foundTag, sub);
        }
        private static String getID(ItemStack itemStack){
            String itemID = itemStack.getDescriptionId();

            itemID = itemID.substring(itemID.indexOf("minecraft.") + 10);

            if(itemID.contains("_")) {
                String[] split = itemID.split("_");
                if(getCombine(itemStack)!= null)
                    return Objects.requireNonNull(getCombine(itemStack)).tag;
                return split[split.length-1];
            }
            else
                ExampleMod.LOGGER.info(itemID);
            return itemID;
        }

        public static void tagging(){
            for (ItemStack itemStack : ITEM_LIST) {
                String findingTag = getID(itemStack);
                Sub sub = TAG_MAP.get(findingTag);
                if (sub == null) {
                    TAG_MAP.put(findingTag, new Sub(findingTag));
                } else if (!sub.hasItem(itemStack)) {
                    String itemID = itemStack.getDescriptionId();

                    if(getCombine(itemStack) != null)
                        return;
                    if(itemID.contains("crimson") || itemID.contains("quartz"))
                        TAG_MAP.get(EnumCombine.NETHER.name()).addItemContent(itemStack);
                    else if(itemID.contains("mangrove"))
                        TAG_MAP.get(EnumCombine.WOOD.name()).addItemContent(itemStack);
                    else if(itemID.contains("coral") || itemID.contains("kelp"))
                        TAG_MAP.get(EnumCombine.CORAL.name()).addItemContent(itemStack);
                    else
                        TAG_MAP.get(findingTag).addItemContent(itemStack);
                }
            }
            makeETC();

        }

        private static Sub getCombine(ItemStack itemStack){
            for(EnumCombine enumCombine : EnumCombine.values()){
                for(String str : enumCombine.getTypes()) {
                    return TAG_MAP.get(enumCombine.name());

                }
                break;
            }

            return null;
        }


        /**
         * 3개 이하의 아이템은 ETC로 옮김
         */
        private static void makeETC(){
            ArrayList<String> removeETC = new ArrayList<>();
            for(String key : TAG_MAP.keySet()){
                Sub sub = TAG_MAP.get(key);
                int itemCount = sub.itemContentList.keySet().size();
                if(itemCount <= 3)
                {
                    removeETC.add(key);
                }
            }

            for(String key : removeETC){
                Sub sub = TAG_MAP.get(key);
                TAG_MAP.remove(key);
                sub.tag = "ETC";
                if(TAG_MAP.containsKey("ETC"))
                    TAG_MAP.get("ETC").addItemContent(sub.itemContentList);
                else
                    TAG_MAP.put("ETC", sub);
            }
        }
        /**
         *
         */
        private static void makeCombine(EnumCombine combine){
            HashMap<String, Sub> makeCombine = new HashMap<>();
            for(String key : TAG_MAP.keySet()){
                Sub sub = TAG_MAP.get(key);
                String nameTag = sub.tag;
                for(EnumCombine enumCombine : EnumCombine.values()) {
                    if(enumCombine == combine) {

                        makeCombine.put(key, TAG_MAP.get(key));
                        break;
                    }
                }
            }


                Sub sub = TAG_MAP.get(key);
                sub.tag = combine.name();
                sub.addItemContent(makeCombine);
                if(TAG_MAP.containsKey(combine.name()))
                    TAG_MAP.remove(key);

        }
        public static String findTag(ItemStack itemStack){
            Sub sub = getTagMap().get(getID(itemStack));
            if(sub == null) {
                ExampleMod.LOGGER.info("태그를 찾으려 했지만 태그를 찾을 수 없습니다." + itemStack);
                return "ETC 없는 아이템";
            }

            return sub.tag;
        }

        public static boolean hasTag(ItemStack itemStack) {
            return (TAG_MAP.containsKey(findTag(itemStack)));
        }
    }
    public static class Sub{
        @SerializedName("태그")
        private String tag;
        @SerializedName("태그 설명")
        private String tagText = "이 태그에 대한 설명을 여기에 적어주세요.";

        @SerializedName("아이템 목록")
        private TreeMap<String, ItemContent> itemContentList = new TreeMap<>();

        Sub(String tag){
            this.tag = tag;
        }
        Sub(String tag, ItemStack itemStack){
            this.tag = tag;
            addItemContent(itemStack);
        }
        public void addItemContent(ItemStack itemStack){
            this.itemContentList.put(itemStack.getDescriptionId(), new ItemContent(itemStack));
        }
        public void addItemContent(Map<String , ItemContent> map){
            itemContentList.putAll(map);
        }

        public boolean hasItem(ItemStack itemStack){
            return itemContentList.containsKey(itemStack.getDescriptionId());
        }

        public ItemContent getItemContent(ItemStack itemStack){
            return itemContentList.get(itemStack.getDescriptionId());
        }

        public TreeMap<String, ItemContent> getItemContentList() {
            return itemContentList;
        }
    }
    public static class ItemContent {
        private transient ItemStack itemStack; //대상 아이템
        @SerializedName("아이템 아이디")
        String itemID = "";
        @SerializedName("아이템 영어 이름")
        String englishName = LangManager.getEnglishName(itemStack);
        @SerializedName("아이템에 대한 설명")
        String dictionary = "설명";
        ItemContent(ItemStack itemStack){
            this.itemStack = itemStack;
            itemID = itemStack.getDescriptionId();

        }

        public String getItemID() {
            return itemID;
        }

        public String getDictionary() {
            return dictionary;
        }
    }
}
