package com.example.examplemod.entity;

import com.example.examplemod.MyEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;

public class MiniCreeper extends Creeper {

    private int waitingTime = 60;
    private float explosionRadius = 3;
    private static final EntityDataAccessor<Boolean> DATA_ADULT = SynchedEntityData.defineId(MiniCreeper.class, EntityDataSerializers.BOOLEAN);

    public MiniCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0D).
                add(Attributes.MOVEMENT_SPEED, 0.25).
                add(Attributes.FOLLOW_RANGE, 16);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_ADULT, true);

    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        if(isAdult() ) {
            MiniCreeper creeper = new MiniCreeper(MyEntity.MINI_CREEPER.get(), level());
            creeper.moveTo(getX(), getY(), getZ());
            creeper.setAdult(false);
            creeper.explosionRadius = 1.5F;
            level().addFreshEntity(creeper);
            creeper.getDimensions(creeper.getPose()).scale(0.5F);
            creeper.getAttributes().getInstance(Attributes.SCALE).setBaseValue(0.5F);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {

        return new ClientboundAddEntityPacket(this);

    }


    public void setAdult(boolean isAdult){
        entityData.set(DATA_ADULT, isAdult);

    }

    public boolean isAdult(){
        return entityData.get(DATA_ADULT);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("adult", isAdult());
        pCompound.putFloat("explosionRadius", explosionRadius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        explosionRadius = pCompound.getFloat("explosionRadius");
        setAdult(entityData.get(DATA_ADULT));
    }

}
