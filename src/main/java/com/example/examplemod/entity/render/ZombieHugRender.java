package com.example.examplemod.entity.render;

import com.example.examplemod.entity.ZombieHug;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ZombieHugRender extends AbstractZombieRenderer<ZombieHug, ModelZombieHug> {
    public ZombieHugRender(EntityRendererProvider.Context pContext) {
        this(pContext, ModelLayers.ZOMBIE, ModelLayers.ZOMBIE_INNER_ARMOR, ModelLayers.ZOMBIE_OUTER_ARMOR);

    }
    public ZombieHugRender(EntityRendererProvider.Context pContext, ModelLayerLocation pZombieLayer, ModelLayerLocation pInnerArmor, ModelLayerLocation pOuterArmor) {
        super(
                pContext,
                new ModelZombieHug(pContext.bakeLayer(pZombieLayer)),
                new ModelZombieHug(pContext.bakeLayer(pInnerArmor)),
                new ModelZombieHug(pContext.bakeLayer(pOuterArmor))
        );
    }
    @Override
    public void render(ZombieHug pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    protected float getAttackAnim(ZombieHug pLivingBase, float pPartialTickTime) {
        return 0;
    }
}
