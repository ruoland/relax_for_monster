package com.land.mymonsters;

import com.land.mymonsters.entity.*;
import com.land.mymonsters.entity.creeper.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class MyEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MyMonsters.MODID);
    public static void getAllRegisteredEntities(String entityType) {
        ENTITY_TYPES.getEntries().forEach(registryObject -> {
            String registryName = registryObject.getId().toString();

            System.out.println("Registered entity: " + registryName);
        });
    }

    public static final DeferredHolder<EntityType<?>, EntityType<EnderCreeper>> ENDER_CREEPER =
            ENTITY_TYPES.register("ender_creeper", () -> EntityType.Builder.of(EnderCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).canSpawnFarFromPlayer().build("myrelax:endercreeper"));

    public static final DeferredHolder<EntityType<?>, EntityType<SpiderCreeper>> SPIDER_CREEPER =
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

    public static final DeferredHolder<EntityType<?>, EntityType<EndermanTheScary>> ENDERMOB =
            ENTITY_TYPES.register("endermob", () -> EntityType.Builder.of(EndermanTheScary::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.8F).canSpawnFarFromPlayer().build("myrelax:endermob"));

    public static final DeferredHolder<EntityType<?>, EntityType<EndermanTheKidnap>> ENDER_KIDNAP =
            ENTITY_TYPES.register("enderkidnap", () -> EntityType.Builder.of(EndermanTheKidnap::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.8F).canSpawnFarFromPlayer().build("myrelax:enderkidnap"));

    public static final DeferredHolder<EntityType<?>, EntityType<MokourBlock>> MOKOUR_BLOCK =
            ENTITY_TYPES.register("mokour_block", () -> EntityType.Builder.of(MokourBlock::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F).canSpawnFarFromPlayer().build("myrelax:mokourblock"));

    public static final DeferredHolder<EntityType<?>, EntityType<Endercouple>> ENDER_COUPLE =
            ENTITY_TYPES.register("endercouple", () -> EntityType.Builder.of(Endercouple::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.8F).canSpawnFarFromPlayer().build("myrelax:endercouple"));
    public static final DeferredHolder<EntityType<?>, EntityType<MissileCreeper>> MISSILE_CREEPER =
            ENTITY_TYPES.register("missile_creeper", () -> EntityType.Builder.of(MissileCreeper::new, MobCategory.MONSTER)
                    .sized(1.0F, 2.0F).canSpawnFarFromPlayer().build("myrelax:missilecreeper"));

    public static void register(IEventBus iEventBus){
        ENTITY_TYPES.register(iEventBus);
    }



}