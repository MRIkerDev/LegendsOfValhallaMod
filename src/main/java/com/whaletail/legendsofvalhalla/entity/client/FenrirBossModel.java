package com.whaletail.legendsofvalhalla.entity.client;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.entity.custom.boss.fenrir.FenrirBossEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FenrirBossModel extends GeoModel<FenrirBossEntity> {
    @Override
    public ResourceLocation getModelResource(FenrirBossEntity fenrirBossEntity) {
        return new ResourceLocation(LegendsOfValhalla.MODID, "geo/fenrir.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FenrirBossEntity fenrirBossEntity) {
        return new ResourceLocation(LegendsOfValhalla.MODID, "textures/entity/fenrir.png");

    }

    @Override
    public ResourceLocation getAnimationResource(FenrirBossEntity fenrirBossEntity) {
        return new ResourceLocation(LegendsOfValhalla.MODID, "animations/fenrir.json");
    }

    @Override
    public void setCustomAnimations(FenrirBossEntity animatable, long instanceId, AnimationState<FenrirBossEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if(head != null){
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
