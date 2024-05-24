package com.example.examplemod.entity.render;

import com.example.examplemod.entity.EnderCreeper;
import com.example.examplemod.entity.RocketCreeper;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RocketCreeperRender extends MobRenderer<RocketCreeper, CreeperModel<RocketCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public RocketCreeperRender(EntityRendererProvider.Context pContext) {
        super(pContext, new CreeperModel<>(pContext.bakeLayer(ModelLayers.CREEPER)), 0.5F);

    }
    private int a = 0;
    @Override
    public void render(RocketCreeper pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        pPoseStack.pushPose();
        Vec3 vec3 = shootFromRotation(pEntity.getXRot(),  pEntity.getYRot(), 90.0F);
        Direction direction = pEntity.getDirection();
        if(a <= 90)
        a++;


        float f1 = sleepDirectionToRotation(direction) ;
        pPoseStack.mulPose(Axis.YP.rotationDegrees(f1));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(a));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(pEntity.yHeadRot + 360));

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        pPoseStack.popPose();
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
