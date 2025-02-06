
package com.whaletail.legendsofvalhalla.item.client;
import com.whaletail.legendsofvalhalla.item.custom.MjolnirItem;



import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MjolnirRenderer extends GeoItemRenderer<MjolnirItem> {
    public MjolnirRenderer() {
        super(new MjolnirModel());
    }
}

