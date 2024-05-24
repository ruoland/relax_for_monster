package com.example.examplemod.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class RocketCreeper extends Creeper {
    private int rotTime = 0;//
    private Direction direction;
    private static final EntityDataAccessor<Boolean> DATA_IS_FIRE = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_LAUNCH = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);

    public RocketCreeper(EntityType<? extends Creeper> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createLivingAttributes().add(Attributes.KNOCKBACK_RESISTANCE, 0D).
                add(Attributes.MOVEMENT_SPEED, 0.25).
                add(Attributes.FOLLOW_RANGE, 32).add(Attributes.JUMP_STRENGTH, 2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_FIRE,false);
        pBuilder.define(DATA_IS_LAUNCH, false);

    }
    private float fixHead, fixBody;
    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        this.setYHeadRot(pPlayer.yHeadRot);
        this.setYBodyRot(pPlayer.yBodyRot);
        fixHead = pPlayer.yHeadRot;
        fixBody = pPlayer.yBodyRot;
        return super.mobInteract(pPlayer, pHand);
    }

    public int distance;
    Vec3 livingEntityBack = new Vec3(1,0,1);


    @Override
    public void tick() {
        super.tick();
        this.setYHeadRot(fixHead);
        this.setYBodyRot(fixBody);
        if(getTarget() != null ){

            if(!entityData.get(DATA_IS_LAUNCH)) {
                getLookControl().setLookAt(getTarget());
                livingEntityBack = getLookAngle().multiply(1.3, 0, 1.3);
                entityData.set(DATA_IS_LAUNCH, true);
            }
        }
        if(livingEntityBack != null && entityData.get(DATA_IS_LAUNCH)){
            setDeltaMovement(livingEntityBack);
            ignite();

        }
    }

    @Override
    public boolean isColliding(BlockPos pPos, BlockState pState) {
        if(super.isColliding(pPos, pState) && isLaunch()) {
            ignite();
            return true;
        }else
            return super.isColliding(pPos, pState);
    }

    public Vec3 shootFromRotation(float pX, float pY, float pZ) {
        float f = -Mth.sin(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((pX + pZ) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        return new Vec3(f,f1,f2);
    }


    public int getRotTime() {
        return rotTime;
    }

    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        super.setTarget(pTarget);
        if(pTarget != null) {
            lookAt(pTarget, 360, 360);
            getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0);

        }
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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_IS_FIRE, pCompound.getBoolean("IS_FIRE"));
        entityData.set(DATA_IS_LAUNCH, pCompound.getBoolean("IS_LAUNCH"));
    }
}
