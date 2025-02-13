package com.whaletail.legendsofvalhalla.item.custom;

import com.whaletail.legendsofvalhalla.entity.custom.MjolnirProjectileEntity;
import com.whaletail.legendsofvalhalla.item.client.MjolnirRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = "legendsofvalhalla", value = Dist.CLIENT)
public class MjolnirItem extends ElytraItem implements GeoItem {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final KeyMapping keyShootLightning = new KeyMapping("key.shoot_lightning", GLFW.GLFW_KEY_G, "key.categories.gameplay");

    // Define los valores de daño y retroceso
    private static final float DAMAGE_AMOUNT = 17.0F;
    private static final float KNOCKBACK_STRENGTH = 3.5F;

    public MjolnirItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return true; // Permitir siempre el vuelo
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isFallFlying()) {
                // Ajustar la velocidad y dirección del vuelo
                double yaw = Math.toRadians(player.getYRot());
                double pitch = Math.toRadians(player.getXRot());
                double x = -Math.sin(yaw) * Math.cos(pitch);
                double y = -Math.sin(pitch);
                double z = Math.cos(yaw) * Math.cos(pitch);
                player.setDeltaMovement(x * 1.5, y * 1.5, z * 1.5); // Ajusta la velocidad del impulso
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUseHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUseHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ANVIL_BREAK, SoundSource.NEUTRAL, 1.0F, 0.8F);

        if (!pLevel.isClientSide) {
            MjolnirProjectileEntity mjolnirProjectile = new MjolnirProjectileEntity(pLevel, pPlayer);
            mjolnirProjectile.setPos(pPlayer.getX(), pPlayer.getY() + 1.5, pPlayer.getZ());
            mjolnirProjectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 2.5F, 1.0F);
            pLevel.addFreshEntity(mjolnirProjectile);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));

        if (!pPlayer.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    // Evento para generar un rayo al golpear una entidad y aplicar daño y retroceso
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = player.level(); // Usar getLevel() en lugar de acceder directamente a level
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.getItem() instanceof MjolnirItem && !level.isClientSide && level instanceof ServerLevel serverLevel) {
            BlockPos hitPos = target.blockPosition();

            // Aplicar daño y retroceso
            if (target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) target;
                livingTarget.hurt(player.damageSources().playerAttack(player), DAMAGE_AMOUNT);
                livingTarget.knockback(KNOCKBACK_STRENGTH, player.getX() - target.getX(), player.getZ() - target.getZ());
            }

            // Generar rayo si hay tormenta
            if (serverLevel.isThundering() && serverLevel.canSeeSky(hitPos)) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (lightning != null) {
                    lightning.moveTo(hitPos.getX(), hitPos.getY(), hitPos.getZ());
                    serverLevel.addFreshEntity(lightning);
                }
            }
        }
    }

    // Evento para hacer al jugador inmune a los rayos cuando tenga el martillo en la mano
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if ("lightningBolt".equals(event.getSource().getMsgId())) {
            LivingEntity entity = event.getEntity();
            if (entity instanceof Player) {
                Player player = (Player) entity;
                ItemStack mainHandItem = player.getMainHandItem();
                if (mainHandItem.getItem() instanceof MjolnirItem) {
                    event.setCanceled(true); // Cancelar el daño por rayo
                }
            }
        }
    }

    // Evento para disparar un rayo en la dirección en la que el jugador está mirando al presionar la tecla G
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            while (keyShootLightning.consumeClick()) { // Verificar si la tecla G está presionada
                Player player = mc.player;
                if (player != null) {
                    ItemStack mainHandItem = player.getMainHandItem();
                    if (mainHandItem.getItem() instanceof MjolnirItem && !player.level().isClientSide && player.level() instanceof ServerLevel serverLevel) {
                        Vec3 lookVec = player.getLookAngle();
                        BlockPos targetPos = player.blockPosition().offset((int) (lookVec.x * 10), (int) (lookVec.y * 10), (int) (lookVec.z * 10)); // Ajusta la distancia del rayo
                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                        if (lightning != null) {
                            lightning.moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                            serverLevel.addFreshEntity(lightning);
                        }
                    }
                }
            }
        }
    }

    // Métodos de animación (GeoItem)
    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(
                RawAnimation.begin().then("animation.mjolnir.idle", Animation.LoopType.LOOP)
        );
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return GeoItem.super.getTick(itemStack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private MjolnirRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    renderer = new MjolnirRenderer();
                }
                return this.renderer;
            }
        });
    }
}