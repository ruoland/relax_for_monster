package com.land.mymonsters.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EndermanTheKidnap extends EnderMan{
    private int kidnapCooltime = 100, teleportKidnap = 60;
    public EndermanTheKidnap(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void setTarget(@Nullable LivingEntity pLivingEntity) {
        super.setTarget(pLivingEntity);
    }


    @Override
    public void tick() {
        super.tick();
        //공격 타겟이 있다면
        if(getTarget() != null) {
            LivingEntity target = getTarget();
            //납치 쿨타임이 0 이하이며
            if (teleportKidnap <= 0 && kidnapCooltime <= 0) {
                //타겟이 4블럭보다 멀리 있는 경우
                if(getTarget().position().distanceTo(this.position()) > 4) {
                    //타겟 근처로 다가가기
                    randomTeleport(target.getX() + Math.random(), target.getY() + Math.random(), target.getZ(),true);
                    teleportKidnap = 20;//텔레포트 후 납치할 때 잠깐의 딜레이
                }
                //타겟이 4블럭 안에 있다면, kidnapCooltime이 0이라면 납치하기
                else if(getTarget().position().distanceTo(this.position()) < 4) {
                    getTarget().startRiding(this);
                    kidnapCooltime = 100;
                }
            }
        }
        //납치한 개체가 없다면 계속 쿨타임 감소
        if(getCarriedMob() == null) {
            kidnapCooltime--;
            teleportKidnap--;
        }
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Nullable
    @Override
    public BlockState getCarriedBlock() {
        return getCarriedMob() != null ? Blocks.AIR.defaultBlockState() : null;
    }

    @Override
    public void setCarriedBlock(@Nullable BlockState pState) {
        //super.setCarriedBlock(pState);
    }

    @Nullable
    public LivingEntity getCarriedMob(){
        return getPassengers().isEmpty() ? null : (LivingEntity) getPassengers().get(0);
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity pEntity) {
        return super.getPassengerRidingPosition(pEntity).subtract(0,1,0).add(getLookAngle().multiply(1.05, 1.05, 1.05));
    }
}
