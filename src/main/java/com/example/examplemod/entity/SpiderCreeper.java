package com.example.examplemod.entity;

import com.example.examplemod.entity.ai.AvoidCreeperGoal;
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
        if(runAwayFromCreeper <= 0)
        {
            runAwayFromCreeper--;
        }
        if(!hasCreeper() && findCooltime-- < 0 ) {
            findCreeper();
        }
        if(hasCreeper()){
            Creeper creeper = (Creeper) getFirstPassenger();
            if(getTarget() == null)
            {
                setTarget(creeper.getTarget());
            }

            if(creeper.isIgnited())
                throwCreeper(creeper);

            if(getTarget() != null && creeper.getTarget() != null) {
                lookAt(getTarget(), 360, 360);
                if (distanceTo(getTarget()) <= 6) {
                    stopRiding();
                    creeper.stopRiding();
                    creeper.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
                    throwCreeper(creeper);

                }
            }
        }
    }

    public boolean hasCreeper(){
        return getFirstPassenger() != null && (getFirstPassenger() instanceof Creeper);
    }

    public void findCreeper(){
        List<Entity> findCreeperList = level().getEntities(this, getBoundingBox().inflate(3, 2, 3));
        for (Entity entity : findCreeperList) {
            if (entity instanceof Creeper creeper) {
                if(!creeper.isIgnited()){
                    if (creeper instanceof EnderCreeper enderCreeper) {
                        //enderCreeper.teleportToSpider(this);
                        enderCreeper.startRiding(this);
                        break;
                    } else if (!(creeper.getRootVehicle() instanceof Spider)) {
                        this.startRiding(creeper);
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
    @Override
    public boolean canAttack(LivingEntity pTarget) {
        return super.canAttack(pTarget);
    }
}
