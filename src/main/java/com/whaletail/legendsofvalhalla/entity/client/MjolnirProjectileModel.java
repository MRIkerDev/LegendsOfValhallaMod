package com.whaletail.legendsofvalhalla.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.entity.custom.MjolnirProjectileEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;


public class MjolnirProjectileModel extends EntityModel<MjolnirProjectileEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(LegendsOfValhalla.MODID, "mjolnir"), "main");

    private final ModelPart mjolnir;

    public MjolnirProjectileModel(ModelPart root) {
        this.mjolnir = root.getChild("mjolnir");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("mjolnir", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0009F, 1.7982F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5009F, 7.7982F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5009F, -3.2018F, -2.0F, 3.0F, 5.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(0, 0).addBox(-1.2509F, -4.3018F, -2.0F, 2.5F, 1.1F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(0, 0).addBox(2.9991F, -3.7018F, -2.25F, 1.25F, 6.0F, 4.5F, new CubeDeformation(0.01F))
                .texOffs(0, 0).addBox(-4.5009F, -3.7018F, -2.25F, 1.5F, 6.0F, 4.5F, new CubeDeformation(0.01F))
                .texOffs(0, 0).addBox(0.9991F, -2.9518F, -2.0F, 2.0F, 4.5F, 4.0F, new CubeDeformation(0.02F))
                .texOffs(0, 0).addBox(-3.0009F, -2.9518F, -2.0F, 2.0F, 4.5F, 4.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0009F, 14.2018F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.3F, -0.25F, -2.0F, 2.3F, 1.5F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9268F, -3.9768F, 0.0F, 0.0F, 0.0F, -0.5672F));

        PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.3F, -0.25F, -2.0F, 2.3F, 1.5F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9991F, -2.7018F, 0.0F, 0.0F, 0.0F, 0.5672F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(MjolnirProjectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        mjolnir.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}