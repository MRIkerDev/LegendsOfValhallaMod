package com.whaletail.legendsofvalhalla;


import com.mojang.logging.LogUtils;

import com.whaletail.legendsofvalhalla.entity.client.FenrirBossModel;
import com.whaletail.legendsofvalhalla.entity.client.FenrirBossRenderer;
import com.whaletail.legendsofvalhalla.entity.client.MjolnirProjectileRenderer;
import com.whaletail.legendsofvalhalla.entity.ModEntities;
import com.whaletail.legendsofvalhalla.item.ModCreativeModTabs;
import com.whaletail.legendsofvalhalla.item.ModItems;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(LegendsOfValhalla.MODID)
public class LegendsOfValhalla
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "legendsofvalhalla";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();


    //clase main
    public LegendsOfValhalla()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        GeckoLib.initialize();

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.MJOLNIR.get(), MjolnirProjectileRenderer::new);
            EntityRenderers.register(ModEntities.FENRIR.get(), FenrirBossRenderer::new);
        }

    }
}
