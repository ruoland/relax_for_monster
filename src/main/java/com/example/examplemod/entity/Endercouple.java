package com.example.examplemod.entity;

import com.example.examplemod.MyEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.jetbrains.annotations.Nullable;

public class Endercouple extends EnderMan implements IEntityWithComplexSpawn {
    private boolean isLeftHand = true;

    public Endercouple(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);

    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        Endercouple endercouple = MyEntity.ENDER_COUPLE.get().create(level());
        if (endercouple != null) {
            endercouple.moveTo(position());
            endercouple.setLeftHand(!isLeftHand());
            level().addFreshEntity(endercouple);
            endercouple.startRiding(this);
        }
        return super.mobInteract(pPlayer, pHand);
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

        }

    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pPartialTick) {
        Vec3 lookRightAngle = this.calculateViewVector(this.getXRot(), this.getYRot()+90).multiply(1.05F,1,1.05F).subtract(0,2,0);

        return super.getPassengerAttachmentPoint(pEntity, pDimensions, pPartialTick).add(lookRightAngle);
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
