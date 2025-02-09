package com.whaletail.legendsofvalhalla.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.entity.custom.boss.fenrir.FenrirBossEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FenrirBossRenderer extends GeoEntityRenderer<FenrirBossEntity> {
    public FenrirBossRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FenrirBossModel());
    }

    @Override
    public ResourceLocation getTextureLocation(FenrirBossEntity animatable) {
        return new ResourceLocation(LegendsOfValhalla.MODID, "textures/entity/fenrir.png");
    }

    @Override
    public void render(FenrirBossEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.scale(4F, 4F, 4F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
