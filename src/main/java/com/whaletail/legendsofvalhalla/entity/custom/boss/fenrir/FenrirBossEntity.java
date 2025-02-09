package com.whaletail.legendsofvalhalla.entity.custom.boss.fenrir;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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


public class FenrirBossEntity extends Monster implements GeoEntity {
    private int phase = 1;
    private int chainsRemaining = 3;
    private Level level;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ArrayList<ArmorStand> chains = new ArrayList<>();
    public FenrirBossEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.level = level;
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

        ArmorStand chain = chains.remove(0);
        chain.discard();

        chainsRemaining--;
    }

    private void applyChainMovement(){
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        BlockPos pos = this.getOnPos();
        if(pos.getX() > 10){
            this.setPos(10, pos.getY(), pos.getZ());
        }
    }


    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        spawnChains();
        applyChainMovement();
    }

    @Override
    public boolean hurt(DamageSource source, float p_21017_) {
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

        if(this.getHealth() < this.getHealth() * 0.66 && phase == 1){
            enterPhase2();
        }
        if(this.getHealth() < this.getHealth() * 0.33 && phase == 2){
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
        phase = 2;
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(18.0d);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.45D);

    }

    private void enterPhase3(){
        phase = 3;
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
