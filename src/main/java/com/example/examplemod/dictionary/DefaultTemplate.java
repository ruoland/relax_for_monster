package com.example.examplemod.dictionary;

import java.util.Set;

public class DefaultTemplate {

    public DefaultTemplate(){
        Set<String> subNameSet = ItemDataManager.getSubNames();
        String[] subNames = new String[subNameSet.size()];
        ItemDataManager.getSubNames().toArray(subNames);

    }
}
