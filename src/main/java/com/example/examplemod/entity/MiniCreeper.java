package com.example.examplemod.entity;

import com.example.examplemod.MyEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.network.payload.AdvancedAddEntityPayload;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MiniCreeper extends Creeper implements IEntityExtension, IEntityWithComplexSpawn {
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


        return super.hurt(pSource, pAmount);
    }

    @Override
    public void die(DamageSource pDamageSource) {
        System.out.println("소환 어른인가"+isAdult());
        if(isAdult()) {
            System.out.println("소환 어른임");
            MiniCreeper miniCreeper = MyEntity.MINI_CREEPER.get().create(level());
            miniCreeper.moveTo(getX(), getY(), getZ());
            miniCreeper.explosionRadius = explosionRadius / 2;
            MiniCreeper miniCreeperBrother = MyEntity.MINI_CREEPER.get().create(level());
            miniCreeperBrother.moveTo(getX(), getY(), getZ());

            miniCreeperBrother.explosionRadius = explosionRadius / 2;
            miniCreeper.setAdult(false);
            miniCreeperBrother.setAdult(false);
            level().addFreshEntity(miniCreeper);
            level().addFreshEntity(miniCreeperBrother);

                System.out.println("소환 ");


        }
        super.die(pDamageSource);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {

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

    }

    public void setAdult(boolean adult){
        isAdult = adult;
    }

    public boolean isAdult(){
        return isAdult;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeBoolean(isAdult);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        setAdult(additionalData.readBoolean());
        System.out.println("데이터 바음"+isAdult);
    }


}
