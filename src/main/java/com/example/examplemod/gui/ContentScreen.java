package com.example.examplemod.gui;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.dictionary.ItemDataManager;
import com.example.examplemod.dictionary.LangManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class ContentScreen extends Screen {
    private FormattedText itemName;
    private FormattedText itemEngName;
    private ItemStack currentItem;
    private Screen lastScreen;
    private static final String NEW_LINE = "-NewNewNewLine-";
    private Component[] dictionarySplit = new Component[10];

    public ContentScreen(Screen lastScreen, ItemStack itemStack) {
        super(Component.literal("도감"));
        itemName = FormattedText.of(itemStack.getDisplayName().getString());
        itemEngName = FormattedText.of(LangManager.getEnglishName(itemStack));

        this.currentItem = itemStack;
        this.lastScreen = lastScreen;

        String content = ItemDataManager.getItemContent(itemStack).replace("\\n", NEW_LINE);
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
        int posY = 15;
        int posX = this.width / 4 - 25;
        int engLineY = 0;
        if(font.width(itemName) > 100)
            engLineY += 10;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.2F,1.2F,1);
        pGuiGraphics.drawWordWrap(this.font, itemName, posX + 30, posY, 180,0);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(0.9F,0.9F,1);

        pGuiGraphics.drawWordWrap(this.font, itemEngName,  posX + 55 , posY + 20 + engLineY , 180,0);

        pGuiGraphics.pose().popPose();
        if(font.width(itemEngName) > 100)
            engLineY += 10;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1F,1F,1);
        int newLine = 0;
        for(int i = 0; i < dictionarySplit.length;i++) {
            Component dictionary = dictionarySplit[i];

            for (FormattedCharSequence formattedcharsequence : font.split(dictionary, 240)) {
                pGuiGraphics.drawString(this.font, formattedcharsequence, posX + 45, posY + 35 + engLineY + newLine, 0, false);
                newLine+=10;
            }
            newLine += 10;
        }
        pGuiGraphics.pose().popPose();

        int resultX = this.width / 2 - 160;
        int resultY = 10;
        renderItem(pGuiGraphics, resultX, resultY, 1.5F, currentItem, pPartialTick);
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
        float scaleX = 2F;
        int posX = 200;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scaleX,1.3F,1);
        pGuiGraphics.blit(BookViewScreen.BOOK_LOCATION, (this.width) / 2 - posX, 2, 0, 0, 192, 192);
        pGuiGraphics.pose().popPose();


    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(lastScreen);

    }


}
