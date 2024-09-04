package com.land.mymonsters.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MokourBlock extends Mob {
    private static final EntityDataAccessor<Boolean> DATA_IS_VOID = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.BOOLEAN);
    private final BlockState blockState = Blocks.STONE.defaultBlockState();
    private float avoidDirection;
    public MokourBlock(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_VOID, false);
    }

    public BlockState getBlockState() {
        return blockState;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return true;
    }



    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("is_void", entityData.get(DATA_IS_VOID));

    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_IS_VOID, pCompound.getBoolean("is_void"));
    }

    public boolean isVoid(){
        return entityData.get(DATA_IS_VOID);
    }
}
