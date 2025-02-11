package com.whaletail.legendsofvalhalla.item.client;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import com.whaletail.legendsofvalhalla.item.ModItems;
import net.minecraft.client.Minecraft;
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

    // Método que se llama cuando el vuelo se activa
    public static void toggleFlight() {
        isFlying = !isFlying;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (mc.player != null && mc.level != null) {
            ItemStack mainHandItem = mc.player.getMainHandItem();

            // Verificar si el jugador tiene el martillo en la mano
            if (mainHandItem.is(ModItems.MJOLNIR.get())) {
                if (isFlying) {
                    // Volar hacia arriba al presionar la tecla
                    mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, 1.0, mc.player.getDeltaMovement().z);
                    mc.player.fallDistance = 0.0f; // Evitar daño por caída
                }
            }
        }
    }
}