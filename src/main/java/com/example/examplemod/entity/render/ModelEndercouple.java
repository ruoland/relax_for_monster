package com.example.examplemod.entity.render;

import com.example.examplemod.entity.Endercouple;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class ModelEndercouple extends EndermanModel {
    public ModelEndercouple(ModelPart pRoot) {
        super(pRoot);
    }

    @Override
    public void setupAnim(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        Endercouple endercouple = (Endercouple) pEntity;

        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

        if(endercouple.isLeftHand())
            leftArm.yRot= -10F;
        else
            rightArm.yRot = 10F;
    }
}
