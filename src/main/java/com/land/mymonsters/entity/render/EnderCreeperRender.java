package com.land.mymonsters.entity.render;

import com.land.mymonsters.entity.creeper.EnderCreeper;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnderCreeperRender  extends MobRenderer<EnderCreeper, CreeperModel<EnderCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper.png");

    public EnderCreeperRender(EntityRendererProvider.Context pContext) {
        super(pContext, new CreeperModel<>(pContext.bakeLayer(ModelLayers.CREEPER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(EnderCreeper pEntity) {
        return CREEPER_LOCATION;
    }
}
