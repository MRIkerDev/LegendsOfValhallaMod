package com.whaletail.legendsofvalhalla.items;

import com.whaletail.legendsofvalhalla.items.client.MjolnirRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

public class MjolnirItem extends Item implements GeoItem {
private  AnimatableInstanceCache cache= new SingletonAnimatableInstanceCache(this);
    public MjolnirItem(Properties properties) {
        super(properties);
    }
    private PlayState predicate(AnimationState animationState){
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
         return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    controllerRegistrar.add(new AnimationController(this,"controller",0,this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override
    public double getTick(Object itemStack){
    return GeoItem.super.getTick(itemStack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer){
         consumer.accept(new IClientItemExtensions(){
        private MjolnirRenderer renderer;

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer(){
          if(this.renderer==null){
              renderer=new MjolnirRenderer();
          }
          return this.renderer;
        }
     });
    }
}
