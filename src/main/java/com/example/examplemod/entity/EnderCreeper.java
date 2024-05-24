package com.example.examplemod.entity;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnderCreeper extends Creeper {
    private final boolean canTeleport = false;
    private int teleportDealy = 100;
    public EnderCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {

        return Creeper.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0D).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.FOLLOW_RANGE, 16);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public void tick() {
        super.tick();
        if(getTarget() != null && distanceTo(getTarget()) > 5) {
            if(findSpider() && getVehicle() == null) {
                return;
            }

            if(teleportDealy-- <= 0) {
                teleportDealy = 200;
                if (getTarget() != null && !isIgnited())
                    teleportToTarget();
            }
        }
        else
            teleportDealy = 200;
    }

    public boolean findSpider(){
        List<Entity> spiderCreeperList = level().getEntities(this, getBoundingBox().inflate(3, 2, 3));
        for (Entity entity : spiderCreeperList) {
            if (entity instanceof SpiderCreeper spiderCreeper) {
                    //그 거미가 이미 크리퍼를 데리고 있거나 뭔가를 들고 있는 경우
                    if(spiderCreeper.hasCreeper()) {
                        continue;
                    }
                    if(teleportToSpider(spiderCreeper)){
                        spiderCreeper.lookAt(getTarget(), 360, 360);
                        spiderCreeper.startRiding(this, true);
                        ExampleMod.LOGGER.info(getVehicle()+"");
                        return true;
                }
            }
        }
        return false;
    }
    protected boolean teleportToTarget(){
        LivingEntity livingEntity = getTarget();
        Vec3 livingEntityBack = livingEntity.getLookAngle().multiply(-5, 0, -5);
        Vec3 v = livingEntity.position().add(livingEntityBack);

        return teleport(v.x, v.y, v.z);
    }
    protected boolean teleportToSpider(SpiderCreeper spiderCreeper){
        return teleport(spiderCreeper.getX(), spiderCreeper.getY()+1, spiderCreeper.getZ());
    }

    @Override
    public void ignite() {
        super.ignite();
    }

    protected boolean teleport() {
        if (!this.level().isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
            double d1 = this.getY() + (double)(this.random.nextInt(64) - 32);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
            return this.teleport(d0, d1, d2);
        } else {
            return false;
        }
    }
    private boolean teleport(double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);

        while (blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.blocksMotion();//대나무, 거미줄 같은 블록은 회피
        if (flag) {
            Vec3 vec3 = this.position();
            this.teleportTo(pX, pY, pZ); //해당 자리로 이동해도 되는지(블럭이 있는지 없는지)
            this.level().gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
            if (!this.isSilent()) {
                this.level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }

        return false;

    }
    @Override
    public boolean canAttack(LivingEntity pLivingentity, TargetingConditions pCondition) {
        if(pLivingentity.canBeSeenAsEnemy())
            return true;
        return super.canAttack(pLivingentity, pCondition);
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return super.getTarget();
    }
}
