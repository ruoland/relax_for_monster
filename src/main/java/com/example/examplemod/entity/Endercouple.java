package com.example.examplemod.entity;

import com.example.examplemod.MyEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Endercouple extends EnderMan {
    public static final EntityDataAccessor<Boolean> DATA_LEFT_HAND = SynchedEntityData.defineId(Endercouple.class, EntityDataSerializers.BOOLEAN);
    public Endercouple(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_LEFT_HAND, true);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        if(this.level() instanceof ServerLevelAccessor) {

            Endercouple endercouple = new Endercouple(MyEntity.ENDER_COUPLE.value(), level());
            endercouple.moveTo(position());
            level().addFreshEntity(endercouple);
            endercouple.setLeftHand(!isLeftHand());
            endercouple.startRiding(this);
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity pEntity) {

        return super.getPassengerRidingPosition(pEntity);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setLeftHand(pCompound.getBoolean("lefthand"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("lefthand", isLeftHand());
    }

    public boolean isLeftHand(){
        return entityData.get(DATA_LEFT_HAND);
    }

    public void setLeftHand(boolean leftHand){
        entityData.set(DATA_LEFT_HAND, leftHand);
    }

}
