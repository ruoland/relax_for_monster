package com.example.examplemod.entity;

import com.example.examplemod.MyEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.Nullable;

public class Endercouple extends EnderMan implements IEntityWithComplexSpawn {
    private boolean isLeftHand = true;

    private int heartCooltime = 20;

    public Endercouple(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
        Endercouple endercouple = MyEntity.ENDER_COUPLE.get().create(level());

        if (endercouple != null) {
            endercouple.moveTo(position());
            endercouple.setLeftHand(!isLeftHand());
            level().addFreshEntity(endercouple);
            endercouple.startRiding(this);
        }
        return null;
    }

    public boolean isLeftHand(){
        return isLeftHand;
    }

    public void setLeftHand(boolean leftHand){
        isLeftHand = leftHand;
    }



    @Override
    public void tick() {
        super.tick();
        if(!getPassengers().isEmpty()) {
            Endercouple endercouple = (Endercouple) getPassengers().get(0);
            endercouple.setXRot(getXRot());
            endercouple.setYRot(getYRot());
            endercouple.setYBodyRot(yBodyRot);
            endercouple.yBodyRotO = yBodyRotO;
            endercouple.setYHeadRot(getYHeadRot());
            endercouple.setTarget(getTarget());
            endercouple.setLeftHand(!isLeftHand);
            if(heartCooltime <= 0) {
                double d0 = this.random.nextGaussian() * 0.02;
                double d1 = this.random.nextGaussian() * 0.02;
                double d2 = this.random.nextGaussian() * 0.02;
                this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
                heartCooltime = 20;
            }
            heartCooltime--;
        }
    }

    @Nullable
    @Override
    public BlockState getCarriedBlock() {
        return null;
    }

    @Override
    public void setCarriedBlock(@Nullable BlockState pState) {
        //super.setCarriedBlock(pState);
    }

    @Override
    public void setBeingStaredAt() {
        //super.setBeingStaredAt();
    }

    @Override
    public void die(DamageSource pDamageSource) {

        if(getVehicle() instanceof Endercouple partner)
            partner.setHealth(0);
        else if(!getPassengers().isEmpty() && getPassengers().get(0) instanceof Endercouple endercouple)
            endercouple.setHealth(0);
        super.die(pDamageSource);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pPartialTick) {
        Vec3 lookRightAngle = this.calculateViewVector(this.getXRot(), this.getYRot()+90).multiply(1.05F,1,1.05F).subtract(0,2,0);

        return super.getPassengerAttachmentPoint(pEntity, pDimensions, pPartialTick).add(lookRightAngle);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {

        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void onInsideBlock(BlockState pState) {
        super.onInsideBlock(pState);
        if(pState.canOcclude()) {
            if (getVehicle() instanceof Endercouple endercouple) {
                endercouple.teleport();
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("isLeft", isLeftHand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        isLeftHand = pCompound.getBoolean("isLeft");
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeBoolean(isLeftHand);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        isLeftHand = additionalData.readBoolean();
    }
}
