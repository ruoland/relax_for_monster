package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.common.extensions.IEntityExtension;
import org.jetbrains.annotations.Nullable;

public class MiniCreeper extends Creeper implements IEntityExtension {
    private static final EntityDataAccessor<Boolean> DATA_IS_ADULT = SynchedEntityData.defineId(MiniCreeper.class, EntityDataSerializers.BOOLEAN);

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

    @Override
    public void tick() {
        super.tick();
        if(!isAdult()) {
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
    public boolean hurt(DamageSource pSource, float pAmount) {

        if(isAdult()) {

            if(this.level() instanceof ServerLevelAccessor) {
                ServerLevelAccessor level = (ServerLevelAccessor) getCommandSenderWorld();
                MiniCreeper miniCreeper = new MiniCreeper(EntityType.CREEPER, level());
                miniCreeper.moveTo(getX(), getY(), getZ());

                miniCreeper.explosionRadius = explosionRadius / 2;

                MiniCreeper miniCreeperBrother = new MiniCreeper(EntityType.CREEPER, level());
                miniCreeperBrother.moveTo(getX(), getY(), getZ());

                miniCreeperBrother.finalizeSpawn((ServerLevelAccessor) this.level(), level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.JOCKEY, null);
                miniCreeperBrother.explosionRadius = explosionRadius / 2;
                ExampleMod.LOGGER.info("생성 시도222" + level().isClientSide);
                    level().addFreshEntity(miniCreeper);
                    level().addFreshEntity(miniCreeperBrother);
                    ExampleMod.LOGGER.info("생성 시도");

            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        Skeleton skeleton = EntityType.SKELETON.create(this.level());
        if (skeleton != null) {
            skeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
            skeleton.finalizeSpawn(pLevel, pDifficulty, pSpawnType, null);
            skeleton.startRiding(this);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        pCompound.putFloat("explosionRadius", explosionRadius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        explosionRadius = pCompound.getFloat("explosionRadius");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_ADULT, true);
    }

    public boolean isAdult(){
        return this.entityData.get(DATA_IS_ADULT);
    }
}
