package com.land.mymonsters.entity.render;


import com.land.mymonsters.entity.MokourBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class MokourBlockRender extends EntityRenderer<MokourBlock> {
    private final BlockRenderDispatcher dispatcher;
    public MokourBlockRender(EntityRendererProvider.Context pContext) {
        super(pContext);
        dispatcher = pContext.getBlockRenderDispatcher();
    }

    @Override
    public ResourceLocation getTextureLocation(MokourBlock pEntity) {
        return null;
    }

    public void render(MokourBlock pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = pEntity.getBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = pEntity.level();
            if (blockstate != level.getBlockState(pEntity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                pPoseStack.pushPose();
                BlockPos blockpos = BlockPos.containing(pEntity.getX(), pEntity.getBoundingBox().maxY, pEntity.getZ());
                pPoseStack.translate(-0.5, 0.0, -0.5);
                var model = this.dispatcher.getBlockModel(blockstate);
                for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(pEntity.getOnPos())), net.neoforged.neoforge.client.model.data.ModelData.EMPTY))
                    this.dispatcher
                            .getModelRenderer()
                            .tesselateBlock(
                                    level,
                                    this.dispatcher.getBlockModel(blockstate),
                                    blockstate,
                                    blockpos,
                                    pPoseStack,
                                    pBuffer.getBuffer(net.neoforged.neoforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType)),
                                    false,
                                    RandomSource.create(),
                                    blockstate.getSeed(pEntity.getOnPos()),
                                    OverlayTexture.NO_OVERLAY,
                                    net.neoforged.neoforge.client.model.data.ModelData.EMPTY,
                                    renderType
                            );
                pPoseStack.popPose();
                super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
            }
        }
    }

}
