package com.whaletail.legendsofvalhalla.event;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.entity.client.MjolnirProjectileModel;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= LegendsOfValhalla.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MjolnirProjectileModel.LAYER_LOCATION,MjolnirProjectileModel::createBodyLayer);
    }
}
