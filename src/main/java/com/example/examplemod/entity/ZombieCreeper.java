package com.example.examplemod.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ZombieCreeper extends Creeper {
    private boolean isFakeDeathComplete = false;
    private int fakeDeathCooltime = 20;
    private boolean fakeDeathCooldown = false;
    private boolean fakeDeathStart = false;
    private boolean ressurection = false;

    public ZombieCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createZombieAttribute() {
        return Creeper.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0D)
                .add(Attributes.MAX_HEALTH, 25).add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.FOLLOW_RANGE, 16);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);

    }



    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        System.out.println(getHealth() + " - "+ (getMaxHealth() / 1.4F));
        if (getHealth() < (getMaxHealth() / 1.4F) && !ressurection) {
            fakeDeathStart = true;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void tick() {
        super.tick();
        if(!isFakeDeathComplete) {
            if (fakeDeathStart) {
                if (deathTime <= 20)
                    deathTime++;
                else {
                    fakeDeathCooldown = true;
                    fakeDeathStart = false;
                }
            }
            if (fakeDeathCooldown) {
                fakeDeathCooltime--;
                if (fakeDeathCooltime == 0) {
                    ressurection = true;
                    fakeDeathCooldown = false;
                }
            }
            if (ressurection && deathTime > 0)
                deathTime--;
            else if (ressurection)
                isFakeDeathComplete = true;
        }
        if(isFakeDeath()) {
            setSwellDir(-1);
        }
    }

    @Override
    public boolean isIgnited() {
        return !isFakeDeath() && super.isIgnited();
    }

    @Override
    public void aiStep() {
        if(isFakeDeath() && getDeltaMovement() == Vec3.ZERO)
            return;
        super.aiStep();
    }



    @Override
    public void move(MoverType pType, Vec3 pPos) {
        if(isFakeDeath() && getDeltaMovement() == Vec3.ZERO)
            return;
        super.move(pType, pPos);
    }

    public boolean isFakeDeath()
    {
        return ((ressurection && deathTime > 0) || (fakeDeathCooldown || fakeDeathStart)) && !isFakeDeathComplete;
    }
    @Override
    public boolean canFreeze() {
        return deathTime > 0;
    }
}
