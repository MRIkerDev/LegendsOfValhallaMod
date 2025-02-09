package com.whaletail.legendsofvalhalla.entity;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.entity.custom.MjolnirProjectileEntity;
import com.whaletail.legendsofvalhalla.entity.custom.boss.fenrir.FenrirBossEntity;
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

    public static final RegistryObject<EntityType<MjolnirProjectileEntity>> MJOLNIR =
            ENTITY_TYPES.register("mjolnir", () -> EntityType.Builder.<MjolnirProjectileEntity>of(MjolnirProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("mjolnir"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
    public static final RegistryObject<EntityType<FenrirBossEntity>> FENRIR = ENTITY_TYPES.register("fenrir",() -> EntityType.Builder.of(FenrirBossEntity::new, MobCategory.MONSTER).sized(4.0F,4.0F).build("fenrir"));
}