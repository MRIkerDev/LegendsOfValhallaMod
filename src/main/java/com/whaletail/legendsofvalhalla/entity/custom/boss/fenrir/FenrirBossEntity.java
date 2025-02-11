package com.whaletail.legendsofvalhalla.entity.custom.boss.fenrir;

import com.whaletail.legendsofvalhalla.LegendsOfValhalla;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class FenrirBossEntity extends Monster implements GeoEntity {
    private int phase = 1;
    private int chainsRemaining = 3;
    private final Level level;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ArrayList<ArmorStand> chains = new ArrayList<>();
    private final double CHAINS_LIMIT = 5.0F;
    private double chainsMinLimitX;
    private double chainsMaxLimitX;
    private double chainsMinLimitZ;
    private double chainsMaxLimitZ;
    private final int CHAIN_HIT_POINTS = 4;
    private int chainHitPoints = CHAIN_HIT_POINTS;
    private int roarCooldown = 0;

    public FenrirBossEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.level = level;
    }

    private int getPhase(){
        return phase;
    }

    private void setPhase(int new_phase){
        phase = new_phase;
    }

    public static AttributeSupplier setAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 700D).add(Attributes.ATTACK_DAMAGE, 15D).build();
    }

    private void spawnChains(){
        if (chainsRemaining == 0 ) return;

        for(int i = 0; i <= chainsRemaining; i++){
            ArmorStand chain = new ArmorStand(EntityType.ARMOR_STAND, level);
            chain.setPos(this.getX() + i, this.getY(), this.getZ());
            chain.setInvulnerable(true);
            level.addFreshEntity(chain);

            chains.add(chain);
        }
    }

    private void removeChains(){
        if(chains.isEmpty()) return;

        chainHitPoints--;
        if(chainHitPoints == 0) {
            chainsRemaining--;
            ArmorStand chain = chains.remove(0);
            chain.discard();
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.0F);
            chainHitPoints = CHAIN_HIT_POINTS;
        }
    }

    private void applyChainMovement(){
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        BlockPos pos = this.getOnPos();
        if(pos.getX() > 10){
            this.setPos(10, pos.getY(), pos.getZ());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(!this.level.isClientSide && this.getPhase() == 1){
            if(roarCooldown > 0){
                roarCooldown--;
            }else{
                this.performRoar();
                roarCooldown = 200;
            }
        }
    }

    private void performRoar(){
        this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 3.0F, 1.0F);

        double roarRadius = 10.0;
        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(roarRadius));

        for (LivingEntity entity : entities){
            if(entity instanceof ArmorStand){ // Change for chain entity
                continue;
            }
            if (entity != this){
                double dx = entity.getX() - this.getX();
                double dz = entity.getZ() - this.getZ();
                double distance = Math.sqrt(dx * dx + dz * dz);

                if (entity instanceof Player player){
                    player.hurtMarked = true;
                }

                if (distance > 0){
                    double force = 3.5;
                    entity.setDeltaMovement(dx / distance * force, 0.5, dz / distance * force);
                }
            }
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if(getPhase() == 1){
            spawnChains();
            applyChainMovement();

            chainsMinLimitX = this.getX() - CHAINS_LIMIT;
            chainsMaxLimitX = this.getX() + CHAINS_LIMIT;
            chainsMinLimitZ = this.getZ() - CHAINS_LIMIT;
            chainsMaxLimitZ = this.getZ() + CHAINS_LIMIT;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float p_21017_) {
        if (source.getDirectEntity() instanceof Projectile projectile && phase == 1){
            projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-1));
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
            return false;
        }
        removeChains();
        return super.hurt(source, p_21017_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this,Player.class, true));

    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(this.getPhase() == 1 && chainsRemaining >= 0){
           double fenrirPosX = this.getX();
           double fenrirPosZ = this.getZ();

           if (fenrirPosX < chainsMinLimitX) this.setPos(chainsMinLimitX, this.getY(), fenrirPosZ);
           if (fenrirPosX > chainsMaxLimitX) this.setPos(chainsMaxLimitX, this.getY(), fenrirPosZ);
           if (fenrirPosZ < chainsMinLimitZ) this.setPos(fenrirPosX, this.getY(), chainsMinLimitZ);
           if (fenrirPosZ > chainsMaxLimitZ) this.setPos(fenrirPosX, this.getY(), chainsMaxLimitZ);
        }

        if(chainsRemaining < 0 && this.getPhase() == 1){
            enterPhase2();
        }
        if(this.getHealth() < this.getHealth() * 0.33 && this.getPhase() == 2){
            enterPhase3();
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_GROWL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    private void enterPhase2(){
        this.setPhase(2);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(18.0d);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.45D);

    }

    private void enterPhase3(){
        this.setPhase(3);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(21.0d);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState){
        if(tAnimationState.isMoving()){
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.fenrir.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
