package com.land.mymonsters.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EndermanTheScary extends EnderMan {
    int findCooltime= 100;
    public EndermanTheScary(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    public SpawnGroupData onFinalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {

        return null;
    }

    //닭을 들고 있으면 낙뎀 0으로 만들기
    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        if(getCarriedMob() instanceof Chicken)
            fallDistance = 0;
        super.checkFallDamage(pY, pOnGround, pState, pPos);

    }

    @Override
    public void tick() {
        super.tick();

        if(hasCarryMob()){
            LivingEntity mob = getCarriedMob();
            if(mob instanceof Creeper creeper){
                if(getTarget() != null){
                    //공격 타겟이 5 블럭 안에 있다면, 크리퍼를 내려놓고 도망
                    if(getTarget().position().distanceTo(this.position()) < 5){
                        creeper.stopRiding();

                    }
                    else {
                        creeper.stopRiding();
                        throwCreeper(creeper);
                    }
                }
                //크리퍼가 폭발하려면 바로 던진다
                if(creeper.isIgnited() || creeper.getSwellDir() == 1) {
                    mob.stopRiding();
                    throwCreeper(creeper);
                }
            }
            //스켈레톤이 활을 들고 있다면 계속 들고 다니기
            if(mob instanceof Skeleton skeleton){
                if(skeleton.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.BOW) {
                    //계속 들고 다니기
                }
                else{ //활을 들고 있지 않은 스켈레톤이면 그냥 내려놓기
                    if(getTarget() != null){
                        if(getTarget().position().distanceTo(this.position()) < 5){
                            skeleton.stopRiding();
                        }
                    }
                }

            }
        }
        //몬스터 찾기 시간이 0이 됨
        else if(findCooltime == 0){
            findCooltime = 50;
            
            //6 블럭 이내의 몬스터를 탐색
            List<Entity> mobList = level().getEntities(this, this.getBoundingBox().inflate(6,6,6));
            
            for(Entity entity :mobList){
                //타겟이 아무 것도 타고 있지 않다면 엔더맨이 텔레포트 하고 납치함, 밸런스를 위해 태운 엔티티 체력은 절반으로 줄이게 했음
                if(entity.getVehicle() == null){
                    if(entity instanceof Creeper || entity instanceof Skeleton){
                        teleportTo(entity.getX(), entity.getY(), entity.getZ());
                        entity.startRiding(this);
                        ((Monster) entity).setHealth(((Monster) entity).getHealth()/2);
                        return;
                    }
                    //닭 발견시에도 납치함
                    if(entity instanceof Chicken){
                        teleportTo(entity.getX(), entity.getY(), entity.getZ());
                        entity.startRiding(this);
                    }
                }
            }
        }
        findCooltime--;
    }
    //크리퍼를 엔더맨이 보고 있는 방향으로 던지고, 180도 고개를 돌려서 도망침
    public void throwCreeper(Creeper creeper){
        Vec3 livingEntityBack = getLookAngle().multiply(1.01, 0, 1.01).add(0,0.3,0);
        creeper.addDeltaMovement(livingEntityBack);
        creeper.ignite();
        creeper.getPersistentData().putInt("CWThrow", getId());
        turn(getXRot() * 180, getYRot());
        findCooltime = 50;
        teleport();
    }

    public boolean hasCarryMob(){
        return getCarriedMob() != null;
    }
    
    @Nullable
    public LivingEntity getCarriedMob(){
        return getPassengers().isEmpty() ? null : (LivingEntity) getPassengers().get(0);
    }

    @Override
    public int getMaxFallDistance() {
        return getCarriedMob() instanceof Chicken ? 100 : super.getMaxFallDistance();
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

    //엔더맨이 보고 있는 방향으로 엔티티를 배치
    @Override
    public Vec3 getPassengerRidingPosition(Entity pEntity) {

        return super.getPassengerRidingPosition(pEntity).subtract(0,pEntity.getEyeHeight() + 0.2,0).add(getLookAngle().multiply(1.1, 1.05, 1.1));
    }
}
