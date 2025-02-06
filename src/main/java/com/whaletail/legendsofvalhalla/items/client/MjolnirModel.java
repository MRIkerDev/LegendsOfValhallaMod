package com.whaletail.legendsofvalhalla.items.client;

import com.whaletail.legendsofvalhalla.items.MjolnirItem;
import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MjolnirModel extends GeoModel<MjolnirItem> {
    @Override
    public ResourceLocation getModelResource(MjolnirItem mjolnirItem) {
        return new ResourceLocation(LegendsOfValhalla.MODID,"geo/mjolnir.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MjolnirItem mjolnirItem) {
        return new ResourceLocation(LegendsOfValhalla.MODID,"textures/item/mjolnir.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MjolnirItem mjolnirItem) {
        return new ResourceLocation(LegendsOfValhalla.MODID,"animations/mjolnir.animation.json");
    }
}
