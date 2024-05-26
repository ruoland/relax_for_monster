package com.example.examplemod.entity.render;

import com.example.examplemod.entity.Endercouple;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class ModelEndercouple extends EndermanModel {
    public ModelEndercouple(ModelPart pRoot) {
        super(pRoot);
    }
    float a = -3F;
    @Override
    public void setupAnim(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        Endercouple endercouple = (Endercouple) pEntity;
        this.riding = false;


        if(pEntity.getVehicle() instanceof LivingEntity vehicle)
            super.setupAnim(vehicle, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        else
            super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

        this.riding = false;
        if(endercouple.isLeftHand())
            leftArm.zRot= -0.13F;
        else
            rightArm.zRot = 0.13F;
    }
}
