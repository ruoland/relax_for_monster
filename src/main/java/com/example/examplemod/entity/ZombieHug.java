package com.example.examplemod.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ZombieHug extends Zombie {
    private static final EntityDataAccessor<Boolean> DATA_WANT_HUG = SynchedEntityData.defineId(ZombieHug.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HUGGING = SynchedEntityData.defineId(ZombieHug.class, EntityDataSerializers.BOOLEAN);

    public ZombieHug(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_WANT_HUG, true);
        pBuilder.define(DATA_HUGGING, false);
    }


    @Override
    protected boolean isHorizontalCollisionMinor(Vec3 pDeltaMovement) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean wantHug(){
        return entityData.get(DATA_WANT_HUG);
    }


    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }


    @Override
    public boolean doHurtTarget(Entity pEntity) {
        entityData.set(DATA_HUGGING, true);
        entityData.set(DATA_WANT_HUG, false);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0D);

        return false;
    }



    @Override
    public boolean canAttackType(EntityType<?> pType) {
        return super.canAttackType(pType);
    }
}
