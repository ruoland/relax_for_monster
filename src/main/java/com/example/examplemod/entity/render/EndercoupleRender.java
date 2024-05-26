package com.example.examplemod.entity.render;

import com.example.examplemod.entity.Endercouple;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.EnderMan;

public class EndercoupleRender extends EndermanRenderer {
    public EndercoupleRender(EntityRendererProvider.Context p_173992_) {
        super(p_173992_);
        model = new ModelEndercouple(p_173992_.bakeLayer(ModelLayers.ENDERMAN));
    }

    @Override
    public void render(EnderMan pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        getModel().carrying = true;
        Endercouple leftHandEnder = (Endercouple) pEntity;
        if(pEntity.getVehicle() instanceof Endercouple rightHandEnder) {
            rightHandEnder.setLeftHand(false);
            super.render(rightHandEnder, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
        else {
            leftHandEnder.setLeftHand(true);
            super.render(leftHandEnder, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }
        getModel().carrying = true;
    }
}
