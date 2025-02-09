package com.whaletail.legendsofvalhalla.datagen;

import com.whaletail.legendsofvalhalla.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModelItemModelProvider extends ItemModelProvider {
    public ModelItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ModItems.FENRIR_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}
