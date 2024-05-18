package com.example.examplemod.entity.render;

import com.example.examplemod.entity.SpiderCreeper;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpiderCreeperRender extends MobRenderer<SpiderCreeper, SpiderModel<SpiderCreeper>> {
    private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");

    public SpiderCreeperRender(EntityRendererProvider.Context pContext) {
        super(pContext, new SpiderModel<>(pContext.bakeLayer(ModelLayers.SPIDER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderCreeper pEntity) {
        return SPIDER_LOCATION;
    }
}
