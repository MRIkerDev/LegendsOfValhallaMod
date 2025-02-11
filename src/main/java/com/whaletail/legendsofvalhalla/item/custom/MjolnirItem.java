package com.whaletail.legendsofvalhalla.item.custom;

import com.whaletail.legendsofvalhalla.entity.custom.MjolnirProjectileEntity;
import com.whaletail.legendsofvalhalla.item.client.MjolnirRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

public class MjolnirItem extends ElytraItem implements GeoItem {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

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