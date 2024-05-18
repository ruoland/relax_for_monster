package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class MiniCreeper extends Creeper {
    private boolean isAdult = true;
    private int waitingTime = 60;
    private float explosionRadius = 3;
    public MiniCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0D).
                add(Attributes.MOVEMENT_SPEED, 0.25).
                add(Attributes.FOLLOW_RANGE, 16);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public boolean isAdult() {
        return isAdult;
    }

    @Override
    public void tick() {
        super.tick();
        if(!isAdult) {
            if (waitingTime > 0) {
                waitingTime--;
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            } else if (waitingTime == 0) {
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
                waitingTime--;
            }
        }
    }


    @Override
    public void die(DamageSource pDamageSource) {


        super.die(pDamageSource);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {

        if(isAdult) {
            MiniCreeper miniCreeper = new MiniCreeper(EntityType.CREEPER, level());
            miniCreeper.moveTo(getX(), getY(), getZ());
            miniCreeper.isAdult = false;

            miniCreeper.explosionRadius = explosionRadius / 2;

            MiniCreeper miniCreeperBrother = new MiniCreeper(EntityType.CREEPER, level());
            miniCreeperBrother.moveTo(getX(), getY(), getZ());
            miniCreeperBrother.isAdult = false;

            miniCreeperBrother.explosionRadius = explosionRadius / 2;
            ExampleMod.LOGGER.info("생성 시도222");
            if(!level().isClientSide()) {
                level().addFreshEntity(miniCreeper);
                level().addFreshEntity(miniCreeperBrother);
                ExampleMod.LOGGER.info("생성 시도");
            }

        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("adult", isAdult);
        pCompound.putFloat("explosionRadius", explosionRadius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        isAdult = pCompound.getBoolean("adult");
        explosionRadius = pCompound.getFloat("explosionRadius");
    }

}
