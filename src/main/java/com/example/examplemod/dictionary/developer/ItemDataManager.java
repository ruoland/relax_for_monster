package com.example.examplemod.dictionary.developer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ItemDataManager {
    //아이템을 소분류로 나눠서
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path DIRECTORY_PATH = Paths.get("./dictionary");




}
