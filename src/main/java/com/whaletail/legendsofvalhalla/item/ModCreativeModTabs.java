package com.whaletail.legendsofvalhalla.item;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS=
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LegendsOfValhalla.MODID);

public static final RegistryObject<CreativeModeTab> LEGENDS_OF_VALHALLA_TAB=CREATIVE_MODE_TABS.register("legends_of_valhalla",
                                        //aqui se cambia el icono
        ()->CreativeModeTab.builder().icon(()->new ItemStack(ModItems.MJOLNIR.get()))
                .title(Component.translatable("creativetab.legends_of_valhalla_tab"))
                .displayItems((pParameters, pOutput) ->{
                    //se agrega el mjolnir
                    pOutput.accept(ModItems.MJOLNIR.get());
                    //pOutput.accept(Items.DIAMOND); //asi se agregaria items vanilla
                } )
                .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
