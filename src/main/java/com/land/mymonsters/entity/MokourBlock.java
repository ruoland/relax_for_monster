package com.land.mymonsters.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MokourBlock extends Mob {
    private static final EntityDataAccessor<Boolean> DATA_IS_VOID = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_PLACE_MODE = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Vector3f> DATA_POSITION = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> DATA_CAN_PUSH = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_SPAWN_Y = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(MokourBlock.class, EntityDataSerializers.OPTIONAL_UUID);
    private BlockState blockState = Blocks.STONE.defaultBlockState();
    private float avoidDirection;
    public MokourBlock(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(DATA_IS_VOID, false);
        pBuilder.define(DATA_PLACE_MODE, false);
        pBuilder.define(OWNER, Optional.empty());

        pBuilder.define(DATA_BLOCK_STATE, Blocks.STONE.defaultBlockState());
        pBuilder.define(DATA_POSITION, position().toVector3f());
        pBuilder.define(DATA_CAN_PUSH, true);
        pBuilder.define(DATA_SPAWN_Y, 0F);
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

    @Override
    public void push(Entity pEntity) {
        //super.push(pEntity);
    }

    @Override
    protected void pushEntities() {
        //super.pushEntities();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void playerTouch(Player pPlayer) {
        super.playerTouch(pPlayer);
        if(pPlayer.isShiftKeyDown())
            remove(RemovalReason.DISCARDED);
    }

    @Override
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        if(pPlayer.isCreative()){
            Item handHeld = pPlayer.getItemInHand(pHand).getItem();
            if(handHeld instanceof BlockItem blockItem){
                setBlockState(blockItem.getBlock().defaultBlockState());
            }
            else if(handHeld == Items.STICK){
                setNoGravity(true);
                noPhysics = true;
                if(!isB(DATA_PLACE_MODE)) {
                    setB(DATA_PLACE_MODE, true);
                    entityData.set(DATA_POSITION, position().toVector3f());
                }
                else
                    setB(DATA_PLACE_MODE, false);
                entityData.set(OWNER, Optional.of(pPlayer.getUUID()));
            }
        }
        return super.interactAt(pPlayer, pVec, pHand);
    }

    @Override
    public void tick() {
        super.tick();
        if(isB(DATA_PLACE_MODE))
        {
            Player owner = getOwner();
            if(owner != null){
                System.out.println("서버여부:"+!level().isClientSide());
                System.out.println("플레이어:"+owner);
                Vec3 lookVec = owner.getLookAngle().normalize().multiply(2.5,1,2.5).add(0, owner.getEyeHeight(), 0);
                setPos(owner.position().add(lookVec));
                fallDistance = 0;
                setNoGravity(true);
            }
        }
        if(isB(DATA_CAN_PUSH)){
            List<Entity> entityList = level().getEntities(this, getBoundingBox().inflate(0, 0.3,0));
            for(Entity entity : entityList){
                if(entity instanceof Player player){
                    setDeltaMovement(0,-0.01,0);
                }
            }
            if(entityList.isEmpty()) {
                Vector3f vec3 = entityData.get(DATA_POSITION);
                if(vec3.y >= position().y)
                    setDeltaMovement(0, 0.01, 0);
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;

    }

    public Player getOwner(){
        if(entityData.get(OWNER).isPresent())
            return level().getPlayerByUUID(entityData.get(OWNER).get());
        else
            return null;
    }
    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("is_void", entityData.get(DATA_IS_VOID));
        pCompound.putBoolean("place_mode", entityData.get(DATA_PLACE_MODE));
        if(entityData.get(OWNER).isPresent())
            pCompound.putUUID("owner", entityData.get(OWNER).get());
        pCompound.put("block", NbtUtils.writeBlockState(blockState));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(DATA_IS_VOID, pCompound.getBoolean("is_void"));
        entityData.set(DATA_PLACE_MODE, pCompound.getBoolean("place_mode"));
        if(pCompound.hasUUID("owner"))
            entityData.set(OWNER, Optional.of(pCompound.getUUID("owner")));
        entityData.set(DATA_BLOCK_STATE, NbtUtils.readBlockState(level().holderLookup(Registries.BLOCK), pCompound.getCompound("block")));
    }

    public void setB(EntityDataAccessor<Boolean> dataAccessor, boolean value){
        entityData.set(dataAccessor, value);
    }

    public boolean isB(EntityDataAccessor<Boolean> dataAccessor){
        return entityData.get(dataAccessor);
    }
    public boolean isVoid(){
        return entityData.get(DATA_IS_VOID);
    }

    public boolean isPlace(){
        return entityData.get(DATA_PLACE_MODE);
    }
}
