package com.whaletail.legendsofvalhalla.item.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "legendsofvalhalla", value = Dist.CLIENT)
public class MjolnirFlightHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean isFlying = false; // Para controlar si el jugador está volando

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (mc.player != null && mc.level != null) {
            ItemStack mainHandItem = mc.player.getMainHandItem();

            //  jugador tiene el martillo en la mano
            if (mainHandItem.is(ModItems.MJOLNIR.get())) {
                mc.player.fallDistance = 0.0f;

                if (mc.options.keyJump.isDown() && InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL)) { // Espacio y Ctrl
                    // animación de las elytras
                    if (!mc.player.isFallFlying()) {
                        mc.player.startFallFlying();
                    }
                    double yaw = Math.toRadians(mc.player.getYRot());
                    double pitch = Math.toRadians(mc.player.getXRot());
                    double x = -Math.sin(yaw) * Math.cos(pitch);
                    double y = -Math.sin(pitch);
                    double z = Math.cos(yaw) * Math.cos(pitch);
                    mc.player.setDeltaMovement(x * 1.5, y * 1.5, z * 1.5); //velocidad del impulso
                    isFlying = true;
                } else if (isFlying) {
                    // Detener el vuelo y regresar al estado normal
                    mc.player.stopFallFlying();
                    mc.player.setDeltaMovement(0, 0, 0); // Detener el movimiento
                    isFlying = false;
                }
            } else {
                isFlying = false;
            }
        }
    }
}