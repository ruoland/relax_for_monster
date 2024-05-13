package com.example.examplemod;

import com.example.examplemod.gui.GuiDictionary;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class DictionaryEvent2 {

    final int KEY_CODE = ExampleMod.ClientModEvents.OPEN_DICTIONARY.getKey().getValue();
    
    @SubscribeEvent
    public void onKeyUp(ScreenEvent.KeyPressed.Post keyPressed){
        if(keyPressed.getKeyCode() == KEY_CODE)
            ExampleMod.ClientModEvents.OPEN_DICTIONARY.setDown(true);
    }

    @SubscribeEvent
    public void onKeyDown(ScreenEvent.KeyReleased.Post keyReleased){
        if(keyReleased.getKeyCode() == KEY_CODE)
            ExampleMod.ClientModEvents.OPEN_DICTIONARY.setDown(false);
    }

    @SubscribeEvent
    public void onRenderTooltip(RenderTooltipEvent.Pre event){
        if(ExampleMod.ClientModEvents.OPEN_DICTIONARY.isDown()) {
            ExampleMod.LOGGER.info("툴 팁 렌더링 그리고 키 누르는 중!" + event.getItemStack());

            Minecraft mc = Minecraft.getInstance();

            if(!(mc.screen instanceof GuiDictionary)) {
                GuiDictionary dictionary = new GuiDictionary(mc.screen, event.getItemStack());
                mc.setScreen(dictionary);
            }
        }
    }
}
