package dev.thomasglasser.tommylib.api.world.item.armor;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

/**
 * Must use Neo mixin to override initializeClient method or use {@link BaseGeoArmorItem}
 */
public interface GeoArmorItem extends GeoItem
{
    GeoArmorRenderer<?> newRenderer();

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    AnimatableInstanceCache getAnimatableInstanceCache();

    boolean isSkintight();
}
