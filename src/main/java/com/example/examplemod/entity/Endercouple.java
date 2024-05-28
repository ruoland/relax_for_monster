package com.example.examplemod.entity;

import com.example.examplemod.MyEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.Nullable;

public class Endercouple extends EnderMan implements IEntityWithComplexSpawn {
    private static final EntityDataAccessor<Boolean> DATA_HAS_COUPLE = SynchedEntityData.defineId(Endercouple.class, EntityDataSerializers.BOOLEAN);
    private boolean isLeftHand = true;
    private int heartCooltime = 20;

    public Endercouple(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_HAS_COUPLE, false);
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
        if(!entityData.get(DATA_HAS_COUPLE)){
            Endercouple endercouple = MyEntity.ENDER_COUPLE.get().create(level());
            endercouple.moveTo(position());
            endercouple.setCouple();
            level().addFreshEntity(endercouple);
            this.startRiding(endercouple);
            endercouple.setLeftHand(!isLeftHand);
            entityData.set(DATA_HAS_COUPLE, true);
        }
    }

    public void setCouple(){
        entityData.set(DATA_HAS_COUPLE, true);
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
        Vec3 lookRightAngle = this.calculateViewVector(this.getXRot(), this.getYRot()+90).multiply(1.05F,1,1.05F).subtract(0,2.7,0);

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
        pCompound.putBoolean("hasCouple", entityData.get(DATA_HAS_COUPLE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        isLeftHand = pCompound.getBoolean("isLeft");
        entityData.set(DATA_HAS_COUPLE, pCompound.getBoolean("hasCouple"));
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
