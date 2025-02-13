package com.whaletail.legendsofvalhalla.item.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.whaletail.legendsofvalhalla.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "legendsofvalhalla", value = Dist.CLIENT)
public class MjolnirFlightHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean isFlying = false; // Para controlar si el jugador está volando
    private static int flightTickCounter = 0; // Contador de ticks para el retraso
    private static int liftTickCounter = 0; // Contador de ticks para la elevación progresiva
    private static Vec3 lastVelocity = Vec3.ZERO; // Última velocidad registrada

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (mc.player != null && mc.level != null) {
            ItemStack mainHandItem = mc.player.getMainHandItem();

            // Verificar si el jugador tiene el martillo en la mano
            if (mainHandItem.is(ModItems.MJOLNIR.get())) {

                if (mc.options.keyJump.isDown() && InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL)) { // Verificar si las teclas de salto (Espacio) y Ctrl están presionadas
                    flightTickCounter++;
                    if (flightTickCounter >= 4) { // Esperar un segundo (4 ticks)
                        // Impulsar hacia adelante con la animación de las elytras
                        if (!mc.player.isFallFlying()) {
                            mc.player.startFallFlying();
                        }
                        double yaw = Math.toRadians(mc.player.getYRot());
                        double pitch = Math.toRadians(mc.player.getXRot());
                        double x = -Math.sin(yaw) * Math.cos(pitch);
                        double y = -Math.sin(pitch);
                        double z = Math.cos(yaw) * Math.cos(pitch);
                        Vec3 velocity = new Vec3(x * 1.5, y * 1.5, z * 1.5); // Ajusta la velocidad del impulso
                        mc.player.setDeltaMovement(velocity);
                        lastVelocity = velocity; // Guardar la última velocidad
                        isFlying = true;
                    }
                } else if (mc.options.keyJump.isDown()) { // Verificar si solo la tecla de salto (Espacio) está presionada
                    liftTickCounter++;
                    if (flightTickCounter >= 4) { // Esperar un segundo (4 ticks)
                        double liftSpeed = Math.min(0.1 * liftTickCounter / 2.0, 12.0); // Incrementar la velocidad de elevación progresivamente, con una velocidad máxima de 2.0
                        mc.player.setDeltaMovement(mc.player.getDeltaMovement().x, liftSpeed, mc.player.getDeltaMovement().z);
                        mc.player.setSprinting(false); // Evitar la animación de correr
                    }
                } else {
                    flightTickCounter = 0; // Reiniciar el contador si se suelta alguna tecla
                    liftTickCounter = 0; // Reiniciar el contador de elevación
                    if (isFlying) {
                        // Detener el vuelo y mantener la velocidad progresiva
                        mc.player.stopFallFlying();
                        mc.player.setDeltaMovement(lastVelocity); // Mantener la última velocidad
                        isFlying = false;
                    }
                }
            } else {
                isFlying = false;
                flightTickCounter = 0; // Reiniciar el contador si no tiene el martillo
                liftTickCounter = 0; // Reiniciar el contador de elevación
                lastVelocity = Vec3.ZERO; // Reiniciar la última velocidad
            }

            // Verificar si el jugador está cerca del suelo para detener el vuelo
            if (isFlying && mc.player.onGround()) {
                mc.player.stopFallFlying();
                mc.player.setDeltaMovement(0, 0, 0); // Detener el movimiento
                isFlying = false;
                flightTickCounter = 0; // Reiniciar el contador al aterrizar
                liftTickCounter = 0; // Reiniciar el contador de elevación
                lastVelocity = Vec3.ZERO; // Reiniciar la última velocidad
            }

            // Verificar si el jugador choca con un bloque
            if (isFlying) {
                Vec3 startVec = mc.player.position();
                Vec3 endVec = startVec.add(mc.player.getDeltaMovement().scale(1.5));
                BlockHitResult hitResult = mc.level.clip(new net.minecraft.world.level.ClipContext(startVec, endVec, net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, mc.player));
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    // Detener el vuelo y regresar al estado normal
                    mc.player.stopFallFlying();
                    mc.player.setDeltaMovement(0, 0, 0); // Detener el movimiento
                    isFlying = false;
                    flightTickCounter = 0; // Reiniciar el contador al chocar
                    liftTickCounter = 0; // Reiniciar el contador de elevación
                    lastVelocity = Vec3.ZERO; // Reiniciar la última velocidad
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ItemStack mainHandItem = player.getMainHandItem();

            // Verificar si el jugador tiene el martillo en la mano
            if (mainHandItem.is(ModItems.MJOLNIR.get())) {
                event.setCanceled(true); // Cancelar el daño por caída
            }
        }
    }
}