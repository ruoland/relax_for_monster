package com.land.mymonsters.entity.creeper;

import com.land.mymonsters.entity.SpiderCreeper;
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
    private int teleportDealy = 60;
    private int ridingDelay = 100;
    public EnderCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {

        return Creeper.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0D).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public void tick() {
        super.tick();
        if(getTarget() != null && distanceTo(getTarget()) > 5) {
         
            if(teleportDealy-- <= 0) {
                teleportDealy = 60;
                if (getTarget() != null && !isIgnited()) {
                    teleportToTarget();
                    setHealth(getHealth() / 2);
                }
            }
        }
        else
            teleportDealy = 100;
    }
    public Vec3 findSafeTeleportLocation(LivingEntity targetEntity, int maxAttempts) {
        Vec3 sourcePos = this.position();
        Vec3 targetPos = targetEntity.position();
        Vec3 teleportVec = targetPos.subtract(sourcePos);
        double distance = teleportVec.length();

        // 거리에 따라 텔레포트 범위 조절 (예: 거리의 20%를 범위로 사용)
        double range = distance * 0.2;

        for (int i = 0; i < maxAttempts; i++) {
            double randomX = sourcePos.x + (random.nextDouble() * 2 - 1) * range;
            double randomY = sourcePos.y + (random.nextDouble() * 2 - 1) * range;
            double randomZ = sourcePos.z + (random.nextDouble() * 2 - 1) * range;

            BlockPos blockPos = new BlockPos((int)randomX, (int)randomY, (int)randomZ);

            // 블록이 엔티티의 움직임을 막고, 그 위의 블록은 움직임을 막지 않는지 확인
            if (level().getBlockState(blockPos).blocksMotion() &&
                    !level().getBlockState(blockPos.above()).blocksMotion()) {
                // 적합한 위치를 찾았으므로 해당 위치의 상단 중앙을 반환
                return new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
            }
        }

        // 적합한 위치를 찾지 못한 경우 원래 위치 반환
        return sourcePos;
    }
    protected boolean teleportToTarget(){
        LivingEntity livingEntity = getTarget();
        Vec3 targetPos;
        if(!getTarget().canBeSeenAsEnemy() && random.nextInt(10) == 0) { //, 플레이어가 적을 볼 수 없는 상태라면 낮은 확률로 플레이어 뒤로 이동
            Vec3 livingEntityBack = livingEntity.getLookAngle().multiply(-4, 0, -4);
            targetPos = livingEntity.position().add(livingEntityBack);
        }
        else {
            targetPos = livingEntity.position();
            Vec3 teleportPos = findSafeTeleportLocation(getTarget(), 10);

        }
        return teleport(targetPos.x, targetPos.y, targetPos.z);
    }
    public boolean teleportToSpider(SpiderCreeper spiderCreeper){
        return teleport(spiderCreeper.getX(), spiderCreeper.getY()+1, spiderCreeper.getZ());
    }

    @Override
    public void ignite() {
        super.ignite();
    }

    private boolean teleport(double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);

        while (blockpos$mutableblockpos.getY() > this.level().getMinBuildHeight() && !this.level().getBlockState(blockpos$mutableblockpos).blocksMotion()) {
            blockpos$mutableblockpos.move(Direction.DOWN);
        }

        BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
        boolean canTeleport = blockstate.blocksMotion();//대나무, 거미줄 같은 블록은 회피
        if (canTeleport) {
            Vec3 pos = this.position();
            this.teleportTo(pX, pY, pZ); //해당 자리로 이동해도 되는지(블럭이 있는지 없는지)
            this.level().gameEvent(GameEvent.TELEPORT, pos, GameEvent.Context.of(this));
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
