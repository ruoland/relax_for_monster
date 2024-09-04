package com.land.mymonsters.entity.creeper;

import com.land.mymonsters.MyEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IEntityExtension;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

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
            //대기시간에는 크리퍼가 움직이지 않게 했음, 미니 크리퍼가 탄생하자마자 터져버릴 수 있기 때문에
            if (waitingTime > 0) {
                waitingTime--;
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            } else if (waitingTime == 0) {
                //시간이 지나면 크리퍼 움직임
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25F);
                waitingTime--;
            }
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {

        //어른 크리퍼인 경우
        if(isAdult()) {
            //폭발력은 1/2를 가진 미니 크리퍼 둘을 생성하기
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

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("isAdult", isAdult);
        pCompound.putFloat("explosionRadius", explosionRadius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        isAdult = pCompound.getBoolean("isAdult");
        explosionRadius = pCompound.getFloat("explosionRadius");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);

    }


    public boolean isAdult(){
        return isAdult;
    }

    public void setAdult(boolean adult){
        isAdult = adult;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeBoolean(isAdult);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        setAdult(additionalData.readBoolean());

    }


}
