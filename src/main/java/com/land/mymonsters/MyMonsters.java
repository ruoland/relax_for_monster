package com.land.mymonsters;

import com.land.mymonsters.entity.*;
import com.land.mymonsters.entity.creeper.*;
import com.land.mymonsters.entity.render.*;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
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
        event.put(MyEntity.MOKOUR_BLOCK.get(), MissileCreeper.createAttributes().add(Attributes.MAX_HEALTH)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0F).build());

        event.put(MyEntity.SKELETON_CREEPER.get(), Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).build());
    }

    @SubscribeEvent
    public void spawnEntityEvent(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevelAccessor)) return;

        Level level = event.getLevel();
        EntityType[] creepers = { MyEntity.ENDER_CREEPER.get(), MyEntity.MINI_CREEPER.get(), MyEntity.MISSILE_CREEPER.get()
        , MyEntity.ROCKET_CREEPER.get(), MyEntity.ZOMBIE_CREEPER.get()};
        EntityType[] endermans = { MyEntity.ENDERMOB.get(), MyEntity.ENDER_COUPLE.get(), MyEntity.ENDER_KIDNAP.get()};
        EntityType[] spiders = { MyEntity.SPIDER_CREEPER.get()};
        EntityType[] skeletons = { MyEntity.SKELETON_CREEPER.get()};

        Vec3 pos = event.getEntity().position();
        RandomSource rand = RandomSource.create();

        if (event.getEntity().getClass() == Creeper.class) {
            int select = rand.nextInt(creepers.length + 1);
            if(select < creepers.length)
                spawnEntity((Mob) creepers[select].create(level), pos);
            else
                event.setCanceled(false);
            }

            if (event.getEntity().getClass() == EnderMan.class) {
                event.setCanceled(true);
                int select = rand.nextInt(endermans.length +1);
                if(select < endermans.length)
                    spawnEntity((Mob) endermans[select].create(level), pos);
                else
                    event.setCanceled(false);
            }

            if (event.getEntity().getClass() == Spider.class) {
                event.setCanceled(true);
                int select = rand.nextInt(spiders.length + 1);
                if(select < spiders.length)
                    spawnEntity((Mob) spiders[select].create(level), pos);
                else
                    event.setCanceled(false);
            }
            if(event.getEntity().getClass() == Skeleton.class){
                event.setCanceled(true);
                int select = rand.nextInt(skeletons.length + 1);
                if( select < skeletons.length)
                    spawnEntity((Mob) skeletons[select].create(level), pos);
                else
                    event.setCanceled(false);
            }
    }

    private void spawnEntity(Mob mob, Vec3 pos){
        mob.moveTo(pos);
        mob.level().addFreshEntity(mob);
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

            EntityRenderers.register(MyEntity.ROCKET_CREEPER.get(), RocketCreeperRender::new);
            EntityRenderers.register(MyEntity.ENDERMOB.get(), EndermanTheScaryRender::new);
            EntityRenderers.register(MyEntity.ENDER_KIDNAP.get(), EndermanTheKidnapRender::new);
            EntityRenderers.register(MyEntity.ENDER_COUPLE.get(), EndercoupleRender::new);
            EntityRenderers.register(MyEntity.MOKOUR_BLOCK.get(), MokourBlockRender::new);
            EntityRenderers.register(MyEntity.MISSILE_CREEPER.get(), MissleCreeperRender::new);
            EntityRenderers.register(MyEntity.SKELETON_CREEPER.get(), SkeletonCreeperRender::new);
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }



    }

}
