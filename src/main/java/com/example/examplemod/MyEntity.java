package com.example.examplemod;

import com.example.examplemod.entity.EnderCreeper;
import com.example.examplemod.entity.SpiderCreeper;
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
            ENTITY_TYPES.register("endercreeper", () -> EntityType.Builder.of(EnderCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 1.0F).canSpawnFarFromPlayer().build("myrelax:endercreeper"));

    public static final DeferredHolder<EntityType<?>, EntityType<SpiderCreeper>> SPIDER =
            ENTITY_TYPES.register("spidercreeper", () -> EntityType.Builder.of(SpiderCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 1.0F).canSpawnFarFromPlayer().build("myrelax:spidercreeper"));

    public static void register(IEventBus iEventBus){
        ENTITY_TYPES.register(iEventBus);
    }



}