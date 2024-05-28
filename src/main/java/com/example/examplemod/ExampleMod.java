package com.example.examplemod;

import com.example.examplemod.dictionary.*;
import com.example.examplemod.entity.*;
import com.example.examplemod.entity.render.*;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{

    // Define mod id in a common place for everything to reference
    public static final String MODID = "myrelax";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ExampleMod(IEventBus modEventBus, ModContainer modContainer)
    {


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        //NeoForge.EVENT_BUS.register(this);


        MyEntity.register(modEventBus);
        modEventBus.addListener(this::newEntityAttributes);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        //NeoForge.EVENT_BUS.register(new DictionaryEvent());
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

    }

    public void newEntityAttributes(EntityAttributeCreationEvent event) {
        ExampleMod.LOGGER.info("안녕");
        event.put(MyEntity.CREEPER.get(), EnderCreeper.createAttributes().add(Attributes.MAX_HEALTH).add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.SPIDER.get(), Spider.createAttributes().add(Attributes.MAX_HEALTH).add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.MINI_CREEPER.get(), MiniCreeper.createAttributes().add(Attributes.MAX_HEALTH).build());
        event.put(MyEntity.ZOMBIE_CREEPER.get(), ZombieCreeper.createZombieAttribute().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.ROCKET_CREEPER.get(), RocketCreeper.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.ENDERMOB.get(), EndermanTheScary.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.ENDER_COUPLE.get(), Endercouple.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.ENDER_KIDNAP.get(), EndermanTheKidnap.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.MISSILE_CREEPER.get(), MissileCreeper.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.ZOMBIE_HUG.get(), ZombieHug.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.ENDER_THE_MUSICIAN.get(), EnderTheMusician.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item));


    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
        LangManager.loadLanguageMap();
        FileManager.getInstance().init();
        try {
            ItemManager.loadMinecraftItems();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        TagManager.getTagManager().loadTag();
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event){
        DictionaryCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onPickupItemEvent(PlayerEvent.ItemPickupEvent event){
        Player player = event.getEntity();
        ItemStack pickupStack = event.getStack();
        Item pickupItem = event.getStack().getItem();

        if(PlayerDictionaryManager.addNewItem((ServerPlayer) player, pickupItem)){
            player.sendSystemMessage(pickupStack.getDisplayName());
        }
        else
            player.sendSystemMessage(Component.literal("이미 도감에 등록된 아이템입니다."));



    }

    @SubscribeEvent
    public void onPlayerSaveEvent(PlayerEvent.SaveToFile event){
        if(event.getEntity() instanceof ServerPlayer player) {
            Inventory inventory = player.getInventory();

            for (ItemStack itemStack : inventory.items) {
                PlayerDictionaryManager.addNewItem(player, itemStack.getItem());
            }

            LOGGER.info("Dictionary Saved.");
        }

    }

    @SubscribeEvent
    public void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event){
        try {
            TagManager.getTagManager().saveTag();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @SubscribeEvent
    public void onPlayerLoadEvent(PlayerEvent.LoadFromFile event){

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        public static final KeyMapping OPEN_DICTIONARY = new KeyMapping("key.dictionary.open", InputConstants.KEY_O, "key.categories.dictionary.dictionarycategory");

        @SubscribeEvent
        public static void registerKey(RegisterKeyMappingsEvent event){
            event.register(OPEN_DICTIONARY);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            EntityRenderers.register(MyEntity.CREEPER.get(), EnderCreeperRender::new);
            EntityRenderers.register(MyEntity.SPIDER.get(), SpiderCreeperRender::new);
            EntityRenderers.register(MyEntity.MINI_CREEPER.get(), MiniCreeperRender::new);
            EntityRenderers.register(MyEntity.ZOMBIE_CREEPER.get(), ZombieCreeperRender::new);

            EntityRenderers.register(MyEntity.ROCKET_CREEPER.get(), RocketCreeperRender2::new);
            EntityRenderers.register(MyEntity.ENDERMOB.get(), EndermanTheScaryRender::new);
            EntityRenderers.register(MyEntity.ENDER_KIDNAP.get(), EndermanTheKidnapRender::new);
            EntityRenderers.register(MyEntity.ENDER_COUPLE.get(), EndercoupleRender::new);
            EntityRenderers.register(MyEntity.MOKOUR_BLOCK.get(), MokourBlockRender::new);
            EntityRenderers.register(MyEntity.MISSILE_CREEPER.get(), MissleCreeperRender::new);
            EntityRenderers.register(MyEntity.ZOMBIE_HUG.get(), ZombieHugRender::new);
            EntityRenderers.register(MyEntity.ENDER_THE_MUSICIAN.get(), EnderMusicianRender::new);
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }



    }

}
