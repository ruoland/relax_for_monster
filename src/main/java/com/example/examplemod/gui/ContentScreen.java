package com.example.examplemod.gui;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.dictionary.LangManager;
import com.example.examplemod.dictionary.developer.category.ItemManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.tags.TagManager;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Arrays;

public class ContentScreen extends DebugScreen {
    private FormattedText itemName;
    private FormattedText itemEngName;
    private ArrayList<ItemStack> itemList = new ArrayList<>();
    private Screen lastScreen;
    private static final String NEW_LINE = "-NewNewNewLine-";
    private Component[] dictionarySplit = new Component[10];
    private int itemIndex;

    public ContentScreen(Screen lastScreen, ItemStack itemStack) {
        super(Component.literal("도감"));
        itemName = FormattedText.of(itemStack.getDisplayName().getString());
        itemEngName = FormattedText.of(LangManager.getEnglishName(itemStack));
        ExampleMod.LOGGER.info(ItemManager.getTagMap()+"");
        for(ItemManager.ItemContent content : ItemManager.getContentMap(itemStack).values()){

            itemList.add(ItemManager.getItemStackMap().get(content.getItemID()));
        }
        this.lastScreen = lastScreen;

        String content = ItemManager.getContent(itemStack).replace("\\n", NEW_LINE);
        ExampleMod.LOGGER.info(content);
        try {
            String[] contentSplit =content.split(NEW_LINE);
            ExampleMod.LOGGER.info(Arrays.toString(contentSplit));
            dictionarySplit = new Component[contentSplit.length];
            for(int i = 0; i < contentSplit.length;i++){
                dictionarySplit[i] = Component.literal(contentSplit[i]);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            dictionarySplit[0] = Component.literal("데이터를 불러오는 중에 어떤 오류가 발생했습니다.  도감 모드에서 이 아이템을 인식을 못 한 것처럼 같습니다.");
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            dictionarySplit[0] = Component.literal("이 도감 내용에는 줄바꿈이 너무 많아 제대로 표현할 수 없습니다. '\\n' 의 개수를 줄여주세요.");
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        int itemInfoX = 200;
        int engLineY = 0;
        if(font.width(itemName) > 100)
            engLineY += 10;
        //아이템 한국어 이름

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.3F,1.3F,1);
        drawText(3, pGuiGraphics, itemName, guiLeft+itemInfoX , guiTop+15, 180, 0);
        pGuiGraphics.pose().popPose();;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.0F,1.0F,1);
        drawText(2, pGuiGraphics, itemEngName, guiLeft+itemInfoX , guiTop+35+engLineY, 180, 0);
        pGuiGraphics.pose().popPose();

        if(font.width(itemEngName) > 100)
            engLineY += 10;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1F,1F,1);
        int newLine = 0;
        for(int i = 0; i < dictionarySplit.length;i++) {
            Component dictionary = dictionarySplit[i];

            for (FormattedText formattedcharsequence : font.getSplitter().splitLines(dictionary, 240, Style.EMPTY)) {
                drawText(1, pGuiGraphics, formattedcharsequence, guiLeft+itemInfoX, guiTop + 45 +(engLineY + newLine), 180, 0);
                newLine+=10;
            }
            newLine += 10;
        }
        pGuiGraphics.pose().popPose();


        renderItem(pGuiGraphics, guiLeft + 30, guiTop-2, 3.5F, itemList.get(itemIndex), pPartialTick);
    }
    int tick  = 0;
    @Override
    public void tick() {
        super.tick();
        tick++;
        if(itemList.size() <= itemIndex)
        {
            itemIndex = 0;
        }
        else if(tick % 30 == 0)
        {
            itemIndex++;
        }

    }

    public void renderItem(GuiGraphics pGuiGraphics, int resultX, int resultY, float scale, ItemStack item, float pPartialTick){
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale,scale,scale);
        pGuiGraphics.renderItem(item, resultX, resultY);
        pGuiGraphics.pose().popPose();
    }
    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderTransparentBackground(pGuiGraphics);
        float scaleX = 1.5F;
        int posX = 200;

        int left=45, top = -10;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scaleX,1.3F,1);
        pGuiGraphics.blit(BookViewScreen.BOOK_LOCATION, guiLeft + left, guiTop + top, 0, 0, 192, 192);
        pGuiGraphics.pose().popPose();


    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(lastScreen);

    }


}
