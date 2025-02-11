package com.whaletail.legendsofvalhalla.entity.custom;

import com.whaletail.legendsofvalhalla.entity.ModEntities;
import com.whaletail.legendsofvalhalla.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class MjolnirProjectileEntity extends ThrowableProjectile {
    private float rotation;  // Variable de rotación
    public Vec2 groundedOffset;  // Posición al impactar en el suelo
    private boolean inGround = false;
    public MjolnirProjectileEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public MjolnirProjectileEntity(Level level, LivingEntity shooter) {
        this(ModEntities.MJOLNIR.get(), level);
        this.setOwner(shooter);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();

        if (!this.inGround) {
            rotation += 10.0f;
            if (rotation >= 360) {
                rotation = 0;
            }
        }

        if (shouldReturn()) {
            returnToThrower();
        }
    }

    public float getRenderingRotation() {
        return rotation;
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        // Set inGround to true
        this.inGround = true;

        switch (result.getDirection()) {
            case SOUTH -> groundedOffset = new Vec2(215f, 180f);
            case NORTH -> groundedOffset = new Vec2(215f, 0f);
            case EAST -> groundedOffset = new Vec2(215f, -90f);
            case WEST -> groundedOffset = new Vec2(215f, 90f);
            case DOWN -> groundedOffset = new Vec2(115f, 180f);
            case UP -> groundedOffset = new Vec2(285f, 180f);
        }

        if (!this.level().isClientSide()) {
            BlockPos blockPos = result.getBlockPos();
            this.level().playSound(null, blockPos, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (this.level() instanceof ServerLevel serverLevel && serverLevel.isThundering() && serverLevel.canSeeSky(blockPos)) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (lightning != null) {
                    lightning.moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    serverLevel.addFreshEntity(lightning);
                }
            }

        }
    }


    private boolean shouldReturn() {
        return this.inGround || this.tickCount > 30;
    }

    private void returnToThrower() {
        if (this.getOwner() instanceof LivingEntity owner) {
            Vec3 direction = new Vec3(owner.getX() - this.getX(), owner.getY() + 1.5 - this.getY(), owner.getZ() - this.getZ()).normalize();
            this.setDeltaMovement(direction.scale(1.5));

            // Detectar colisión con hitbox en lugar de solo la distancia
            if (this.getBoundingBox().intersects(owner.getBoundingBox())) {
                this.discard();
                if (owner instanceof Player) {
                    ((Player) owner).getInventory().add(new ItemStack(ModItems.MJOLNIR.get()));
                }
            }
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();

        // Evitar golpear al lanzador
        if (entity == this.getOwner()) {
            return; // No hagas nada si es el lanzador
        }

        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 400);

        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            BlockPos hitPos = result.getEntity().blockPosition();

            if (serverLevel.isThundering() && serverLevel.canSeeSky(hitPos)) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                if (lightning != null) {
                    lightning.moveTo(hitPos.getX(), hitPos.getY(), hitPos.getZ());
                    serverLevel.addFreshEntity(lightning);
                }
            }
        }
    }
}