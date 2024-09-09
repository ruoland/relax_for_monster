package com.land.mymonsters.entity;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class SkeletonCreeper extends Skeleton {
    public SkeletonCreeper(EntityType<? extends Skeleton> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected AbstractArrow getArrow(ItemStack pArrowStack, float pVelocity) {
        AbstractArrow arrow =super.getArrow(pArrowStack, pVelocity);
        if(!level().isClientSide()) {
            Creeper creeper = EntityType.CREEPER.create(this.level());
            creeper.moveTo(position());
            level().addFreshEntity(creeper);
            creeper.startRiding(arrow);
            System.out.println("화살 소환, 크리퍼 소환");
        }
        return arrow;
    }


}
