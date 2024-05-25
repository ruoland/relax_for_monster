package com.example.examplemod.entity.render;

import com.example.examplemod.entity.RocketCreeper;
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

public class RocketCreeperRender2 extends MobRenderer<RocketCreeper, CreeperModel<RocketCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");




    public RocketCreeperRender2(EntityRendererProvider.Context pContext) {
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

    public Vec3 shootFromRotation(float pX, float pY, float pZ) {
        float f = -Mth.sin(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((pX + pZ) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        return new Vec3(f,f1,f2);
    }

    @Override
    public ResourceLocation getTextureLocation(RocketCreeper pEntity) {
        return CREEPER_LOCATION;
    }
}
