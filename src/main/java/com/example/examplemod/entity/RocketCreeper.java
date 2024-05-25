package com.example.examplemod.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RocketCreeper extends Creeper {
    private static final EntityDataAccessor<Boolean> DATA_IS_FIRE = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_LAUNCH = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_FIXED_HEAD = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_FIXED_BODY = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.FLOAT);
    private final int rotTime = 0;//
    public Direction launchDirection;
    Vec3 livingEntityBack;
    private int rotateCooldown = 80;

    public RocketCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0D).
                add(Attributes.MOVEMENT_SPEED, 0.25).
                add(Attributes.FOLLOW_RANGE, 32);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_FIRE,false);
        pBuilder.define(DATA_IS_LAUNCH, false);
        pBuilder.define(DATA_FIXED_HEAD,-1F);
        pBuilder.define(DATA_FIXED_BODY, -1F);
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if(pPlayer.getItemInHand(pHand) .getItem() == Items.PUMPKIN){
            this.setYHeadRot(pPlayer.yHeadRot);
            this.setYBodyRot(pPlayer.yBodyRot);
            setYHeadRot(pPlayer.yHeadRot);
            setYBodyRot(pPlayer.yBodyRot);
            entityData.set(DATA_FIXED_HEAD, pPlayer.yHeadRot);
            entityData.set(DATA_FIXED_HEAD, pPlayer.yBodyRot);
            launchDirection =Direction.fromYRot(yHeadRot);

            System.out.println("설정 우클릭 "+pPlayer.getDirection() +" - "+Direction.fromYRot(yHeadRot) +" - "+yHeadRot +" - "+yBodyRot);
        }

        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public void tick() {
        super.tick();

        if(getTarget() != null && !isLaunch()){
            if(rotateCooldown >= 30)
                lookControl.setLookAt(getTarget());
            if(!entityData.get(DATA_IS_LAUNCH)) {
                rotateCooldown--;
                if(rotateCooldown <= 0) {
                    entityData.set(DATA_IS_LAUNCH, true);
                    entityData.set(DATA_FIXED_HEAD, yHeadRot);
                    entityData.set(DATA_FIXED_BODY, yBodyRot);
                    launchDirection = Direction.fromYRot(yHeadRot);
                    livingEntityBack = getLookAngle().add(0, 0.5, 0).multiply(1.001, 0, 1.001);
                    System.out.println(launchDirection+ " 방향");
                }
            }
        }
        if(livingEntityBack != null && entityData.get(DATA_IS_LAUNCH)){
            setDeltaMovement(livingEntityBack);

        }

    }

    public float getFixBody(){
        return entityData.get(DATA_FIXED_BODY);
    }

    public float getFixHead(){
        return entityData.get(DATA_FIXED_HEAD);
    }


    @Override
    public boolean canAttack(LivingEntity pTarget) {
        if(position().distanceTo(pTarget.position()) > 8)
            return true;
        return false;
    }

    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        if(pTarget != getTarget()) {
            if(pTarget == null){
                ignite();
                return;
            }

            if(livingEntityBack == null) {
                getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0);

            }
        }
        super.setTarget(pTarget);
    }

    @Override
    protected float getMaxHeadRotationRelativeToBody() {
        return 100;
    }

    @Override
    public int getHeadRotSpeed() {
        return 100;
    }

    public boolean isLaunch(){
        return entityData.get(DATA_IS_LAUNCH);
    }

    public void launch(){
        entityData.set(DATA_IS_LAUNCH, true);
    }
    public boolean isFire(){
        return entityData.get(DATA_IS_FIRE);
    }

    public void ignite(){
        entityData.set(DATA_IS_FIRE, true);
        super.ignite();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("IS_FIRE", isFire());
        pCompound.putBoolean("IS_LAUNCHED", isLaunch());
        pCompound.putShort("Fuse", (short)999);
        pCompound.putFloat("Head", entityData.get(DATA_FIXED_HEAD));
        pCompound.putFloat("Body", entityData.get(DATA_FIXED_BODY));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_IS_FIRE, pCompound.getBoolean("IS_FIRE"));
        entityData.set(DATA_IS_LAUNCH, pCompound.getBoolean("IS_LAUNCH"));
        entityData.set(DATA_FIXED_HEAD, pCompound.getFloat("Head"));
        entityData.set(DATA_FIXED_BODY, pCompound.getFloat("Body"));

    }
}
