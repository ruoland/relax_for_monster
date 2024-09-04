package com.land.mymonsters.entity.creeper;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class MissileCreeper extends Creeper {
    //공격 타겟의 좌표
    private static final EntityDataAccessor<Vector3f> DATA_TARGET_POS = SynchedEntityData.defineId(MissileCreeper.class, EntityDataSerializers.VECTOR3);
    //올라가고 있는 상태인지(발사된 상태인지)
    private static final EntityDataAccessor<Boolean> DATA_IS_LAUNCH = SynchedEntityData.defineId(MissileCreeper.class, EntityDataSerializers.BOOLEAN);
    //목표 높이까지 올라선 후, 떨어지고 있는지?
    private static final EntityDataAccessor<Boolean> DATA_IS_FALL = SynchedEntityData.defineId(MissileCreeper.class, EntityDataSerializers.BOOLEAN);
    //발사 딜레이
    private int launchDelay = 0;
    public MissileCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        super.setTarget(pTarget);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_TARGET_POS, new Vector3f(0,0,0));
        pBuilder.define(DATA_IS_LAUNCH, false);
        pBuilder.define(DATA_IS_FALL, false);
    }

    @Override
    public void tick() {
        super.tick();
        //타겟 발견시
        if(getTarget() != null){
            entityData.set(DATA_TARGET_POS, getTarget().position().toVector3f());
            setLaunch();
        }

        //발사 시작
        if(isLaunch()){
            //제자리에 고정하게
            getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0F);
            
            if(!entityData.get(DATA_IS_FALL)) {
                //타겟의 Y + 10이 될 때까지 올라가게 하기
                if (position().y < getPosition().y + 10) {
                    launchDelay++;
                    setDeltaMovement(0, 1D / 5D, 0);
                    playAmbientSound();
                    entityData.set(DATA_IS_FALL, false);
                    level().addParticle(ParticleTypes.LARGE_SMOKE, position().x, position().y, position().z, 0,0,00);
                    if(launchDelay > 40)
                        ignite();
                } else {
                    //타겟의 Y + 10에 도달하면 DATA_IS_FALL을 true로 설정합니다
                    entityData.set(DATA_IS_FALL, true);
                    moveTo(getPosition().add(0, 10, 0));

                }
            }
            else{
                //떨어지고 있다면 폭발시키게 하기
                ignite();
            }


        }
    }

    public Vec3 getPosition(){
        return new Vec3(entityData.get(DATA_TARGET_POS));
    }

    public void setLaunch(){
        entityData.set(DATA_IS_LAUNCH, true);
    }

    public boolean isLaunch(){
        return entityData.get(DATA_IS_LAUNCH);
    }

    public boolean isFall(){
        return entityData.get(DATA_IS_FALL);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("isLaunch", isLaunch());
        pCompound.putBoolean("isFall", entityData.get(DATA_IS_FALL));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_IS_LAUNCH, pCompound.getBoolean("isLaunch"));
        entityData.set(DATA_IS_FALL, pCompound.getBoolean("isFall"));
    }
}
