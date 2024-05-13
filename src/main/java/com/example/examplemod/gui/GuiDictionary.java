package com.example.examplemod.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.server.LanguageHook;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GuiDictionary extends Screen {
    private FormattedText itemName;
    private FormattedText itemEngName;
    private ItemStack currentItem;
    private Screen lastScreen;

    private static Map<String, String> languageMap = new HashMap<>();
    public GuiDictionary(Screen lastScreen, ItemStack itemStack) {
        super(Component.literal("도감"));
        getLanguage();
        this.currentItem = itemStack;
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();
        itemName = FormattedText.of(currentItem.getDisplayName().getString());
        itemEngName = FormattedText.of(languageMap.get(currentItem.getDescriptionId()));

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        int posY = 15;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.2F,1.2F,1);
        pGuiGraphics.drawWordWrap(this.font, itemName, this.width / 3 + 10, posY, 100,0);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(0.9F,0.9F,1);
        pGuiGraphics.drawWordWrap(this.font, itemEngName,  this.width / 3 + 58 , posY + 30, 100,0);

        pGuiGraphics.pose().popPose();
        renderItem(pGuiGraphics);
    }

    public void renderItem(GuiGraphics pGuiGraphics){
        int calcWidth = 3;
        int itemX = 70;
        int resultX = this.width / calcWidth - itemX;
        int resultY = 10;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(2,2,2);
        pGuiGraphics.renderItem(currentItem, resultX, resultY);
        pGuiGraphics.pose().popPose();
    }
    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderTransparentBackground(pGuiGraphics);
        float scaleX = 1.2F;
        int posX = 250;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scaleX,1.3F,1);
        pGuiGraphics.blit(BookViewScreen.BOOK_LOCATION, (this.width - posX) / 2, 2, 0, 0, 192, 192);
        pGuiGraphics.pose().popPose();


    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(lastScreen);

    }

    /**
     * init 메서드가 실행 될 때 실행해야 합니다!
     */
    public void getLanguage(){
        try {
            Field tableField = LanguageHook.class.getDeclaredField("defaultLanguageTable");
            tableField.setAccessible(true);
            languageMap = (Map<String, String>) tableField.get(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
