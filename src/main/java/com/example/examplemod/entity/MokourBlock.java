package com.example.examplemod.entity;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MokourBlock extends Mob {
    private BlockState blockState = Blocks.STONE.defaultBlockState();
    public MokourBlock(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    public BlockState getBlockState() {
        return blockState;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return true;
    }



}
