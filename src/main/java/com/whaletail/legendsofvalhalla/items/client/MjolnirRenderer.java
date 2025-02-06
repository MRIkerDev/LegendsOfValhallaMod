package com.whaletail.legendsofvalhalla.items.client;

import com.whaletail.legendsofvalhalla.items.MjolnirItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MjolnirRenderer extends GeoItemRenderer<MjolnirItem> {
    public MjolnirRenderer() {
        super(new MjolnirModel());
    }
}
