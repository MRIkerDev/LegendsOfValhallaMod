package com.whaletail.legendsofvalhalla.items;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item>ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, LegendsOfValhalla.MODID);

    public static final RegistryObject<Item> MJOLNIR=ITEMS.register("mjolnir",()->new MjolnirItem(new Item.Properties()));

    public static void register(IEventBus eventBus){ITEMS.register(eventBus);}
}
