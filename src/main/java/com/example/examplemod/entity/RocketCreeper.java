package com.example.examplemod.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class RocketCreeper extends Creeper {
    private static final EntityDataAccessor<Boolean> DATA_IS_FIRE = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_LAUNCH = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_FIXED_X_ROT = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_FIXED_Y_ROT = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3f> DATA_TARGET_POS = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Integer> DATA_ROTATE = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_TRANSLATE = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_ROTATE_Y = SynchedEntityData.defineId(RocketCreeper.class, EntityDataSerializers.INT);

    private final int rotTime = 0;//
    public Direction launchDirection;

    Vec3 livingEntityBack;

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
        pBuilder.define(DATA_FIXED_X_ROT,-1F);
        pBuilder.define(DATA_FIXED_Y_ROT, -1F);
        pBuilder.define(DATA_TARGET_POS, new Vector3f(0,0,0));
        pBuilder.define(DATA_ROTATE, 0);
        pBuilder.define(DATA_TRANSLATE, 0F);
        pBuilder.define(DATA_ROTATE_Y, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if(getFixXRot() != -1)
        {
            setXRot(getFixXRot());
            setYRot(getFixYRot());
        }
        if(getTarget() != null && !isLaunch()){
            if(!entityData.get(DATA_IS_LAUNCH)) {
                setRotate(getRotate()+(90/30));
                setTranslate(getTranslate()+((float) 1/50));
                setRotateY(getRotateY()+(90/10));
                lookControl.setLookAt(getTarget());

                if(getRotate() >= 120) {
                    entityData.set(DATA_IS_LAUNCH, true);
                    setFixXRot(getFixXRot());
                    setFixYRot(getFixYRot());
                    entityData.set(DATA_TARGET_POS, getTarget().position().toVector3f());
                }
            }
        }
        if(entityData.get(DATA_IS_LAUNCH)){
            Vector3f vector3f = entityData.get(DATA_TARGET_POS).sub(this.position().toVector3f());
            if(livingEntityBack == null)
                livingEntityBack = new Vec3(vector3f.x, vector3f.y, vector3f.z);
            setDeltaMovement(livingEntityBack.normalize().multiply(0.5, 1, 0.5));
            ignite();
        }

    }

    public float getTranslate(){
        return entityData.get(DATA_TRANSLATE);
    }

    public void setTranslate(float f){
        entityData.set(DATA_TRANSLATE, f);
    }

    public float getFixYRot(){
        return entityData.get(DATA_FIXED_Y_ROT);

    }

    public void setFixYRot(float yRot){
        entityData.set(DATA_FIXED_Y_ROT, yRot);
        setXRot(getFixXRot());
        setYRot(getFixYRot());
    }

    public int getRotateY(){
        return entityData.get(DATA_ROTATE_Y);
    }

    public void setRotateY(int rotate){
        entityData.set(DATA_ROTATE_Y, rotate );
    }
    public int getRotate(){
        return entityData.get(DATA_ROTATE);
    }

    public void setRotate(int rotate){
        entityData.set(DATA_ROTATE, rotate );
    }

    public float getFixXRot(){
        return entityData.get(DATA_FIXED_X_ROT);
    }

    public void setFixXRot(float xRot){
        entityData.set(DATA_FIXED_X_ROT, xRot);
        setXRot(getFixXRot());
        setYRot(getFixYRot());
    }

    @Override
    public boolean canAttack(LivingEntity pTarget) {
        return position().distanceTo(pTarget.position()) > 6;
    }

    @Override
    public void setTarget(@Nullable LivingEntity pTarget) {
        if(pTarget != getTarget()) {
            if(pTarget == null){
                ignite();
                System.out.println("타겟 찾을 수 없음!");
                return;
            }

            if(livingEntityBack == null) {
                getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0);

            }
        }
        super.setTarget(pTarget);
    }

    @Override
    public int getMaxHeadYRot() {
        return 360;
    }

    @Override
    public int getMaxHeadXRot() {
        return 360;
    }



    @Override
    protected float getMaxHeadRotationRelativeToBody() {
        return 0;
    }

    @Override
    public int getHeadRotSpeed() {
        return 1000;
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
        pCompound.putFloat("Head", entityData.get(DATA_FIXED_X_ROT));
        pCompound.putFloat("Body", entityData.get(DATA_FIXED_Y_ROT));
        pCompound.putFloat("targetX", entityData.get(DATA_TARGET_POS).x);
        pCompound.putFloat("targetY", entityData.get(DATA_TARGET_POS).y);
        pCompound.putFloat("targetZ", entityData.get(DATA_TARGET_POS).z);
        pCompound.putFloat("Translate", entityData.get(DATA_TRANSLATE));
        pCompound.putInt("Rotate", entityData.get(DATA_ROTATE));
        pCompound.putInt("RotateY", entityData.get(DATA_ROTATE_Y));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_IS_FIRE, pCompound.getBoolean("IS_FIRE"));
        entityData.set(DATA_IS_LAUNCH, pCompound.getBoolean("IS_LAUNCH"));
        entityData.set(DATA_FIXED_X_ROT, pCompound.getFloat("Head"));
        entityData.set(DATA_FIXED_Y_ROT, pCompound.getFloat("Body"));
        entityData.set(DATA_TARGET_POS, new Vector3f(pCompound.getFloat("targetX"), pCompound.getFloat("targetY"), pCompound.getFloat("targetZ")));
        entityData.set(DATA_ROTATE, pCompound.getInt("Rotate"));
        entityData.set(DATA_ROTATE_Y, pCompound.getInt("RotateY"));
        entityData.set(DATA_TRANSLATE, pCompound.getFloat("Translate"));
    }
}