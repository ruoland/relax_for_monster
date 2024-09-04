package com.land.mymonsters.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;

public class SkeletonCreeper extends Skeleton {
    public SkeletonCreeper(EntityType<? extends Skeleton> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

}
