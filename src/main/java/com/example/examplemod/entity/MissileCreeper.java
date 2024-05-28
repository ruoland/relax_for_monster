package com.example.examplemod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class MissileCreeper extends Creeper {
    private static final EntityDataAccessor<Vector3f> DATA_TARGET_POS = SynchedEntityData.defineId(MissileCreeper.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> DATA_IS_FALL = SynchedEntityData.defineId(MissileCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_LAUNCH = SynchedEntityData.defineId(MissileCreeper.class, EntityDataSerializers.BOOLEAN);
    private int launchDelay = 0;
    public MissileCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        super.setTarget(pTarget);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_TARGET_POS, new Vector3f(0,0,0));
        pBuilder.define(DATA_IS_LAUNCH, false);
        pBuilder.define(DATA_IS_FALL, false);
    }

    @Override
    public void tick() {
        super.tick();
        if(getTarget() != null){
            entityData.set(DATA_TARGET_POS, getTarget().position().toVector3f());
            setLaunch();
        }

        if(isLaunch()){
            getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0F);
            if(!entityData.get(DATA_IS_FALL)) {
                if (position().y < getPosition().y + 10) {
                    launchDelay++;
                    setDeltaMovement(0, 1D / 5D, 0);
                    playAmbientSound();
                    entityData.set(DATA_IS_FALL, false);
                    level().addParticle(ParticleTypes.LARGE_SMOKE, position().x, position().y, position().z, 0,0,00);
                    if(launchDelay > 40)
                        ignite();
                } else {
                    entityData.set(DATA_IS_FALL, true);
                    moveTo(getPosition().add(0, 10, 0));

                }
            }
            else{
                ignite();
            }


        }
    }

    public Vec3 getPosition(){
        return new Vec3(entityData.get(DATA_TARGET_POS));
    }

    public void setLaunch(){
        entityData.set(DATA_IS_LAUNCH, true);
    }

    public boolean isLaunch(){
        return entityData.get(DATA_IS_LAUNCH);
    }

    public boolean isFall(){
        return entityData.get(DATA_IS_FALL);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("isLaunch", isLaunch());
        pCompound.putBoolean("isFall", entityData.get(DATA_IS_FALL));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_IS_LAUNCH, pCompound.getBoolean("isLaunch"));
        entityData.set(DATA_IS_FALL, pCompound.getBoolean("isFall"));
    }
}
