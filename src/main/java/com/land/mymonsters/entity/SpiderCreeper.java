package com.land.mymonsters.entity;

import com.land.mymonsters.entity.ai.AvoidCreeperGoal;
import com.land.mymonsters.entity.creeper.EnderCreeper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpiderCreeper extends Spider {
    public int runAwayFromCreeper = 0;
    int findCooltime = 100;
    public SpiderCreeper(EntityType<? extends Spider> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AvoidCreeperGoal(this, Creeper.class, 10F, 1.2D,1.2D));
    }

    @Override
    public void tick() {
        super.tick();
        //크리퍼로부터 도망치기
        if(runAwayFromCreeper <= 0)
        {
            runAwayFromCreeper--;
        }
        //크리퍼를 들고 있지 않은 거미라면
        if(!hasCreeper() && findCooltime-- < 0 ) {
            //쿨타임 지나고 크리퍼를 찾는다
            findCreeper();
        }
        //크리퍼를 갖고 있는 거미라면
        if(hasCreeper()){
            Creeper creeper = (Creeper) getFirstPassenger();
            if(getTarget() == null)
            {
                //크리퍼의 공격 타겟을 거미의 공격 타겟으로 설정
                setTarget(creeper.getTarget());
            }

            //크리퍼가 불탄다면 바로 던지기
            if(creeper.isIgnited())
                throwCreeper(creeper);

            //타겟이 있다면
            if(getTarget() != null && creeper.getTarget() != null) {
                //타겟을 바라보고, 
                lookAt(getTarget(), 360, 360);
                //타겟이 8블럭 안에 있다면 크리퍼를 던짐
                if (distanceTo(getTarget()) <= 8) {
                    stopRiding();
                    creeper.stopRiding();
                    //던져진 크리퍼의 넉백 저항을 0으로 해서 검으로 받아칠 수 있게 하려 했는데 테스트가 어려워서 작동 되는지는..
                    creeper.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
                    throwCreeper(creeper);

                }
            }
        }
    }

    public boolean hasCreeper(){
        return getFirstPassenger() != null && (getFirstPassenger() instanceof Creeper);
    }

    //주변에서 크리퍼를 찾기
    public void findCreeper(){
        List<Entity> findCreeperList = level().getEntities(this, getBoundingBox().inflate(3, 2, 3));
        for (Entity entity : findCreeperList) {
            if (entity instanceof Creeper creeper) {
                //크리퍼가 불타지 않고 있어야 하고
                if(!creeper.isIgnited()){
                    //엔더크리퍼를 찾으면 엔더크리퍼가 이 거미한테 텔레포트하여 올라탐
                    if (creeper instanceof EnderCreeper enderCreeper) {
                        enderCreeper.teleportToSpider(this);
                        enderCreeper.startRiding(this);
                        break;

                    } else if (creeper.getRootVehicle() == null) {
                        creeper.startRiding(this);
                        break;
                    }

                }
            }
        }
        findCooltime = 100;
    }
    public void throwCreeper(Creeper creeper){
        Vec3 livingEntityBack = getLookAngle().multiply(1.01, 0, 1.01).add(0,0.3,0);
        creeper.addDeltaMovement(livingEntityBack);
        creeper.ignite();
        creeper.getPersistentData().putInt("CWThrow", getId());
        turn(getXRot() * 180, getYRot());
        runAwayFromCreeper = 100;
        findCooltime = 100;
    }
    //엔더맨이 보고 있는 방향으로 엔티티를 배치
    @Override
    public Vec3 getPassengerRidingPosition(Entity pEntity) {

        return super.getPassengerRidingPosition(pEntity).subtract(0,pEntity.getEyeHeight() - 0.2,0);
    }

    @Override
    public boolean canAttack(LivingEntity pTarget) {
        return super.canAttack(pTarget);
    }
}
