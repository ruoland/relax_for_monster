package com.example.examplemod.dictionary;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.gui.ContentScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class DictionaryEvent {

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
    public void onKeyDown(ClientTickEvent.Pre event){

        if(ExampleMod.ClientModEvents.OPEN_DICTIONARY.isDown()){
                //DIctionaryScreen screen = new DIctionaryScreen(Component.literal("Dictionary"));
                //Minecraft.getInstance().setScreen(screen);


        }
    }
    @SubscribeEvent
    public void onRenderTooltip(RenderTooltipEvent.Pre event){
        if(ExampleMod.ClientModEvents.OPEN_DICTIONARY.isDown()) {
            Minecraft mc = Minecraft.getInstance();
            if(!(mc.screen instanceof ContentScreen)) {
                ContentScreen dictionary = new ContentScreen(mc.screen, event.getItemStack());
                mc.setScreen(dictionary);
            }
        }

    }
}
