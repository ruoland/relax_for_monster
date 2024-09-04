package com.land.mymonsters;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnderTheMusician extends EnderMan {
    public EnderTheMusician(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public BlockState getCarriedBlock() {
        return Blocks.JUKEBOX.defaultBlockState();
    }

    @Override
    public void tick() {
        super.tick();
        if(getTarget() != null)
        {
        }

    }
}
