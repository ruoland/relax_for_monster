package com.example.examplemod.entity.render;

import com.example.examplemod.entity.ZombieCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZombieCreeperRender extends MobRenderer<ZombieCreeper, CreeperModel<ZombieCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public ZombieCreeperRender(EntityRendererProvider.Context pContext) {
        super(pContext, new CreeperModel<>(pContext.bakeLayer(ModelLayers.CREEPER)), 0.5F);
    }


    @Override
    public void render(ZombieCreeper pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieCreeper pEntity) {
        return CREEPER_LOCATION;
    }
}
