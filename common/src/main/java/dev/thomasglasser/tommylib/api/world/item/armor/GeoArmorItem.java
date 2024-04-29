package dev.thomasglasser.tommylib.api.world.item.armor;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public interface GeoArmorItem extends GeoItem
{
    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    boolean isSkintight();
}
