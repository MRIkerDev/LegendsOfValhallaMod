package com.whaletail.legendsofvalhalla.entity.custom;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.item.custom.ThrownMjolnir;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    // Registro de entidades personalizado para el mod
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LegendsOfValhalla.MODID);

    // Registro de la entidad ThrownMjolnir
   /* public static final RegistryObject<EntityType<ThrownMjolnir>> THROWN_MJOLNIR =
            ENTITY_TYPES.register("thrown_mjolnir", () ->
                    EntityType.Builder.<ThrownMjolnir>of(ThrownMjolnir::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)  // Tamaño de la entidad
                            .clientTrackingRange(8) // Rango de renderizado
                            .updateInterval(20) // Intervalo de actualización en ticks
                            .build("thrown_mjolnir"));
*/

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}