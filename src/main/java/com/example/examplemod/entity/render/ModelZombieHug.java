package com.example.examplemod.entity.render;

import com.example.examplemod.entity.ZombieHug;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelZombieHug extends ZombieModel<ZombieHug> {
    public ModelZombieHug(ModelPart pRoot) {
        super(pRoot);
    }

    @Override
    public void setupAnim(ZombieHug pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        attackTime = 0;
        if(pEntity.wantHug()) {
            leftArm.yRot = -1F;
            rightArm.yRot = 1F;
        }else{
            leftArm.yRot = 0.3F;
            rightArm.yRot = -0.3F;
        }
    }

    @Override
    public boolean isAggressive(ZombieHug pEntity) {
        return false;
    }

    @Override
    protected void setupAttackAnimation(ZombieHug pLivingEntity, float pAgeInTicks) {
        //super.setupAttackAnimation(pLivingEntity, pAgeInTicks);
    }
}
