package com.example.examplemod.dictionary;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public class PlayerDictionaryManager {

    public static boolean addNewItem(ServerPlayer player, Item item){
        if(hasItem(player, item))
            return false;
        else
            getDictionaryTag(player).putBoolean(item.getDescriptionId(), true);

        return true;
    }

    public static boolean hasItem(ServerPlayer player, Item item){
        return getDictionaryTag(player).getBoolean(item.getDescriptionId());
    }

    private static CompoundTag getDictionaryTag(ServerPlayer player){
        CompoundTag playerTag = player.getPersistentData();
        CompoundTag dictonaryTag = playerTag.contains("dictionary") ? playerTag.getCompound("dictionary") : new CompoundTag();
        playerTag.put("dictionary", dictonaryTag);
        dictonaryTag = playerTag.getCompound("dictionary");
        return dictonaryTag;
    }
}