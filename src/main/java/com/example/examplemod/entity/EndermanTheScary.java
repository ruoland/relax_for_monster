package com.example.examplemod.entity;

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
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {

        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }


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
            if(mob instanceof Chicken){

            }
            if(mob instanceof Creeper creeper){
                if(getTarget() != null){
                    if(getTarget().position().distanceTo(this.position()) < 5){
                        creeper.stopRiding();
                        throwCreeper(creeper);
                    }
                }
                if(creeper.isIgnited() || creeper.getSwellDir() == 1) {
                    mob.stopRiding();
                    throwCreeper(creeper);
                }
            }
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
        else if(findCooltime == 0){
            findCooltime = 100;
            List<Entity> mobList = level().getEntities(this, this.getBoundingBox().inflate(6,6,6));
            for(Entity entity :mobList){
                if(entity instanceof Creeper || entity instanceof Skeleton){
                    teleportTo(entity.getX(), entity.getY(), entity.getZ());
                    entity.startRiding(this);
                    ((Monster) entity).setHealth(((Monster) entity).getHealth()/2);
                    return;
                }
                if(entity instanceof Chicken){
                    teleportTo(entity.getX(), entity.getY(), entity.getZ());
                    entity.startRiding(this);
                }
            }
        }
        findCooltime--;
    }
    public void throwCreeper(Creeper creeper){
        Vec3 livingEntityBack = getLookAngle().multiply(1.01, 0, 1.01).add(0,0.3,0);
        creeper.addDeltaMovement(livingEntityBack);
        creeper.ignite();
        creeper.getPersistentData().putInt("CWThrow", getId());
        turn(getXRot() * 180, getYRot());
        findCooltime = 100;
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

    @Override
    public Vec3 getPassengerRidingPosition(Entity pEntity) {
        return super.getPassengerRidingPosition(pEntity).subtract(0,1,0).add(getLookAngle().multiply(1.1, 1.05, 1.1));
    }
}
