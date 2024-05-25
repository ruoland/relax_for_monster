package com.example.examplemod.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.EnderMan;

public class EndermanTheScaryRender extends EndermanRenderer {
    public EndermanTheScaryRender(EntityRendererProvider.Context p_173992_) {
        super(p_173992_);
    }

    @Override
    public void render(EnderMan pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        getModel().carrying = true;
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        getModel().carrying = true;
    }
}
