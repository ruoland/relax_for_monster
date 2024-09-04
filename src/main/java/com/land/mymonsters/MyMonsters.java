package com.land.mymonsters;

import com.land.mymonsters.entity.*;
import com.land.mymonsters.entity.creeper.*;
import com.land.mymonsters.entity.render.*;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MyMonsters.MODID)
public class MyMonsters
{

    // Define mod id in a common place for everything to reference
    public static final String MODID = "mymonsters";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public MyMonsters(IEventBus modEventBus, ModContainer modContainer)
    {



        MyEntity.register(modEventBus);
        modEventBus.addListener(this::newEntityAttributes);

        NeoForge.EVENT_BUS.addListener(this::spawnEntityEvent);

    }

    public void newEntityAttributes(EntityAttributeCreationEvent event) {
        MyMonsters.LOGGER.info("안녕");
        event.put(MyEntity.ENDER_CREEPER.get(), EnderCreeper.createAttributes().add(Attributes.MAX_HEALTH).add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
        event.put(MyEntity.SPIDER_CREEPER.get(), Spider.createAttributes().add(Attributes.MAX_HEALTH).add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());
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


    }
    public void spawnEntityEvent(MobSpawnEvent.AllowDespawn event) {
        if (!(event.getLevel() instanceof ServerLevelAccessor)) return;
        ServerLevel level = event.getLevel().getLevel();

        if (event.getEntity() instanceof Creeper) {

            RandomSource rand = event.getEntity().getRandom();
            int select = rand.nextInt(6);

            switch (select) {
                case 0:
                    EnderCreeper enderCreeper = MyEntity.ENDER_CREEPER.get().create(level.getLevel());
                    enderCreeper.moveTo(event.getX(), event.getY(), event.getZ());
                    level.addFreshEntity(enderCreeper);
                    break;
                case 1:
                    MiniCreeper miniCreeper = MyEntity.MINI_CREEPER.get().create(level);
                    miniCreeper.setPos(event.getX(), event.getY(), event.getZ());
                    miniCreeper.setAdult(true);
                    level.addFreshEntity(miniCreeper);

                    break;
                case 2:
                    ZombieCreeper zombieCreeper = MyEntity.ZOMBIE_CREEPER.get().create(level);
                    zombieCreeper.setPos(event.getX(), event.getY(), event.getZ());
                    level.addFreshEntity(zombieCreeper);
                    break;
                case 3:
                    RocketCreeper rocketCreeper = MyEntity.ROCKET_CREEPER.get().create(level);
                    rocketCreeper.setPos(event.getX(), event.getY(), event.getZ());
                    level.addFreshEntity(rocketCreeper);
                    break;
                case 4:
                    MissileCreeper missileCreeper = MyEntity.MISSILE_CREEPER.get().create(level);
                    missileCreeper.setPos(event.getX(), event.getY(), event.getZ());
                    level.addFreshEntity(missileCreeper);
                    break;

                case 5:
                    event.setSpawnCancelled(false);
            }


        }
        if(event.getEntity().getClass() == EnderMan.class){
            event.setSpawnCancelled(true);
            RandomSource rand = event.getEntity().getRandom();
            int select = rand.nextInt(4);
            switch (select) {
                case 0:
                    Endercouple enderCouple = MyEntity.ENDER_COUPLE.get().create(level);
                    enderCouple.setPos(event.getX(), event.getY(), event.getZ());
                    event.getLevel().addFreshEntity(enderCouple);
                    break;
                case 1:
                    EndermanTheKidnap endermanTheKidnap = MyEntity.ENDER_KIDNAP.get().create(level);
                    endermanTheKidnap.setPos(event.getX(), event.getY(), event.getZ());
                    event.getLevel().addFreshEntity(endermanTheKidnap);
                    break;
                case 2:
                    EndermanTheScary endermanTheScary = MyEntity.ENDERMOB.get().create(level);
                    endermanTheScary.setPos(event.getX(), event.getY(), event.getZ());
                    event.getLevel().addFreshEntity(endermanTheScary);
                    break;
                case 3:
                    event.setSpawnCancelled(false);
            }

            }

        if(event.getEntity().getClass() == Spider.class) {
            event.setSpawnCancelled(true);
            RandomSource rand = event.getEntity().getRandom();
            int select = rand.nextInt(2);
            switch (select) {
                case 0:
                    SpiderCreeper spiderCreeper = MyEntity.SPIDER_CREEPER.get().create(level);
                    spiderCreeper.setPos(event.getX(), event.getY(), event.getZ());
                    level.addFreshEntity(spiderCreeper);
                    break;
                case 1:
                    event.setSpawnCancelled(false);
            }
        }
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
            EntityRenderers.register(MyEntity.ENDER_CREEPER.get(), EnderCreeperRender::new);
            EntityRenderers.register(MyEntity.SPIDER_CREEPER.get(), SpiderCreeperRender::new);
            EntityRenderers.register(MyEntity.MINI_CREEPER.get(), MiniCreeperRender::new);
            EntityRenderers.register(MyEntity.ZOMBIE_CREEPER.get(), ZombieCreeperRender::new);

            EntityRenderers.register(MyEntity.ROCKET_CREEPER.get(), RocketCreeperRender2::new);
            EntityRenderers.register(MyEntity.ENDERMOB.get(), EndermanTheScaryRender::new);
            EntityRenderers.register(MyEntity.ENDER_KIDNAP.get(), EndermanTheKidnapRender::new);
            EntityRenderers.register(MyEntity.ENDER_COUPLE.get(), EndercoupleRender::new);
            EntityRenderers.register(MyEntity.MOKOUR_BLOCK.get(), MokourBlockRender::new);
            EntityRenderers.register(MyEntity.MISSILE_CREEPER.get(), MissleCreeperRender::new);
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }



    }

}
