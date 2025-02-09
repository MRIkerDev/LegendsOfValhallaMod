package com.whaletail.legendsofvalhalla.item;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;

import com.whaletail.legendsofvalhalla.entity.ModEntities;
import com.whaletail.legendsofvalhalla.item.custom.MjolnirItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item>ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, LegendsOfValhalla.MODID);

    public static final RegistryObject<Item> MJOLNIR=ITEMS.register("mjolnir",()->new MjolnirItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FENRIR_SPAWN_EGG = ITEMS.register("fenrir_spawn_egg",()->new ForgeSpawnEggItem(ModEntities.FENRIR, 0xD00000, 0xFFFFFF, new Item.Properties()));
    public static void register(IEventBus eventBus){ITEMS.register(eventBus);}
}