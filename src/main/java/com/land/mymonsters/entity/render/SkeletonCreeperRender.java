package com.land.mymonsters.entity.render;

import com.land.mymonsters.entity.SkeletonCreeper;
import com.land.mymonsters.entity.creeper.EnderCreeper;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Skeleton;

public class SkeletonCreeperRender  extends MobRenderer<SkeletonCreeper, SkeletonModel<SkeletonCreeper>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public SkeletonCreeperRender(EntityRendererProvider.Context pContext) {
        super(pContext, new SkeletonModel<>(pContext.bakeLayer(ModelLayers.SKELETON)), 0.5F);

    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonCreeper pEntity) {
        return SKELETON_LOCATION;
    }
}
