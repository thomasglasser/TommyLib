package dev.thomasglasser.tommylib.api.world.item.armor;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animation.AnimatableManager;

/**
 * An interface for GeckoLib armor items
 */
public interface GeoArmorItem extends GeoItem {
    /**
     * Armor has no animation by default
     * 
     * @param controllers The object to register your controller instances to
     */
    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    /**
     * Checks if the armor is skintight for rendering purposes
     * 
     * @return True if the armor is skintight and should hide the player's model when covering
     */
    boolean isSkintight();
}
