package com.land.mymonsters.entity.render;

import com.land.mymonsters.entity.creeper.RocketCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RocketCreeperRender extends MobRenderer<RocketCreeper, CreeperModel<RocketCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RocketCreeperRender(EntityRendererProvider.Context pContext) {
        super(pContext, new CreeperModel<>(pContext.bakeLayer(ModelLayers.CREEPER)), 0.5F);
    }

    @Override
    public void render(RocketCreeper pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        int rotate = pEntity.getRotate();
        pPoseStack.pushPose();
        if(rotate > 0) {
            pPoseStack.translate(0, Math.min(pEntity.getTranslate(), 1), 0);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(Math.min(rotate, 90)));
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(pEntity.yHeadRot));
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90 + pEntity.getRotateY()));
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(RocketCreeper pEntity) {
        return CREEPER_LOCATION;
    }
}
