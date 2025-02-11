package com.whaletail.legendsofvalhalla.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.item.client.MjolnirFlightHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.glfw.GLFW;

import java.awt.*;

@Mod.EventBusSubscriber(modid = "legendsofvalhalla", value = Dist.CLIENT)
public class ModKeyBindings {

    public static KeyMapping FLY_KEY;

    // Registra la tecla personalizada para el vuelo
    public static void register(IEventBus eventBus) {
        // Usamos KeyMapping para las teclas
        FLY_KEY = new KeyMapping("key.legendsofvalhalla.fly", GLFW.GLFW_KEY_SPACE, "key.categories.gameplay");
        MinecraftForge.EVENT_BUS.addListener(ModKeyBindings::onKeyInput); // Escuchamos las teclas en el bus correcto
    }

    // Detectar la pulsación de teclas
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (FLY_KEY.isDown() && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Fly key pressed with Ctrl!"));
            // Aquí agregas la lógica para activar o desactivar el vuelo
            MjolnirFlightHandler.toggleFlight();
        }
    }
}