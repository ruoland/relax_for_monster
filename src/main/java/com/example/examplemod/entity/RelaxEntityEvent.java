package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

public class RelaxEntityEvent {

    @SubscribeEvent
    public void throwCreeper(MobSpawnEvent.FinalizeSpawn event){
        if(event.getEntity() instanceof Slime)
            event.getEntity().discard();
    }
    @SubscribeEvent
    public void throwCreeper(LivingKnockBackEvent event){
        LivingEntity livingEntity = event.getEntity();
        if(livingEntity instanceof Creeper creeper){
            CompoundTag tag = creeper.getPersistentData();
            int id = tag.getInt("CWThrow");
            ExampleMod.LOGGER.info("태그 가짐? "+id);
            if(tag.contains("CWTrow")) {
                ExampleMod.LOGGER.info("반사");
                SpiderCreeper spiderCreeper = (SpiderCreeper) creeper.level().getEntity(id);
                Vec3 vec = new Vec3(creeper.getX() - spiderCreeper.getX(),creeper.getY() -  spiderCreeper.getY(), creeper.getZ() - spiderCreeper.getZ());
                creeper.setDeltaMovement(vec);
                event.setStrength(0);
            }
        }
    }



}
