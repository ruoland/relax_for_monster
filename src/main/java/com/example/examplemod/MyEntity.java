package com.example.examplemod;

import com.example.examplemod.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class MyEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ExampleMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EnderCreeper>> CREEPER =
            ENTITY_TYPES.register("ender_creeper", () -> EntityType.Builder.of(EnderCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).canSpawnFarFromPlayer().build("myrelax:endercreeper"));

    public static final DeferredHolder<EntityType<?>, EntityType<SpiderCreeper>> SPIDER =
            ENTITY_TYPES.register("spider_creeper", () -> EntityType.Builder.of(SpiderCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).canSpawnFarFromPlayer().build("myrelax:spidercreeper"));


    public static final DeferredHolder<EntityType<?>, EntityType<MiniCreeper>> MINI_CREEPER =
            ENTITY_TYPES.register("mini_creeper", () -> EntityType.Builder.of(MiniCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).canSpawnFarFromPlayer().build("myrelax:minicreeper"));


    public static final DeferredHolder<EntityType<?>, EntityType<ZombieCreeper>> ZOMBIE_CREEPER =
            ENTITY_TYPES.register("zombie_creeper", () -> EntityType.Builder.of(ZombieCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).canSpawnFarFromPlayer().build("myrelax:zombiecreeper"));

    public static final DeferredHolder<EntityType<?>, EntityType<RocketCreeper>> ROCKET_CREEPER =
            ENTITY_TYPES.register("rocket_creeper", () -> EntityType.Builder.of(RocketCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).canSpawnFarFromPlayer().build("myrelax:rocketcreeper"));
    public static final DeferredHolder<EntityType<?>, EntityType<MokourBlock>> MOKOUR_BLOCK =
            ENTITY_TYPES.register("mokour_block", () -> EntityType.Builder.of(MokourBlock::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F).canSpawnFarFromPlayer().build("myrelax:mokourblock"));
    public static void register(IEventBus iEventBus){
        ENTITY_TYPES.register(iEventBus);
    }



}