package com.land.mymonsters.entity.render;

import com.land.mymonsters.entity.creeper.MissileCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class MissleCreeperRender extends MobRenderer<MissileCreeper, CreeperModel<MissileCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");




    public MissleCreeperRender(EntityRendererProvider.Context pContext) {
        super(pContext, new CreeperModel<>(pContext.bakeLayer(ModelLayers.CREEPER)), 0.5F);

    }

    private static float sleepDirectionToRotation(Direction pFacing) {
        switch (pFacing) {
            case SOUTH:
                return 90.0F;
            case WEST:
                return 0.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            default:
                return 0.0F;
        }
    }

    @Override
    public void render(MissileCreeper pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        pPoseStack.pushPose();
        if(pEntity.isFall())
            pPoseStack.mulPose(Axis.ZP.rotationDegrees(180));
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MissileCreeper pEntity) {
        return CREEPER_LOCATION;
    }
}
