package com.example.examplemod.entity.ai;

import com.example.examplemod.entity.SpiderCreeper;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;

public class AvoidCreeperGoal extends AvoidEntityGoal {
    SpiderCreeper spiderCreeper = (SpiderCreeper) mob;
    private Creeper avoidCreeper ;
    public AvoidCreeperGoal(PathfinderMob pMob, Class pEntityClassToAvoid, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier) {
        super(pMob, pEntityClassToAvoid, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier);

    }


    @Override
    public boolean canUse() {
        SpiderCreeper spiderCreeper = (SpiderCreeper) mob;
        if(spiderCreeper.hasCreeper())
            avoidCreeper = (Creeper) spiderCreeper.getFirstPassenger();
        else
            return false;
        return spiderCreeper.runAwayFromCreeper > 0;
    }

    @Override
    public boolean canContinueToUse() {

        return spiderCreeper.runAwayFromCreeper > 0;
    }

    @Override
    public void tick() {
        if(avoidCreeper != null && spiderCreeper.runAwayFromCreeper > 0) {
            this.toAvoid = avoidCreeper;
            super.tick();
        }
    }
}
