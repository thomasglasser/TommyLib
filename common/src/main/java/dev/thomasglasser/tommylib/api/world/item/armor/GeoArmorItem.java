package dev.thomasglasser.tommylib.api.world.item.armor;

import dev.thomasglasser.tommylib.api.world.item.FabricGeoItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

/**
 * Must use Neo mixin to override initializeClient method or use {@link BaseGeoArmorItem}
 */
public interface GeoArmorItem extends GeoItem, FabricGeoItem
{
    GeoArmorRenderer newRenderer();

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    AnimatableInstanceCache getAnimatableInstanceCache();

    boolean isSkintight();
}
