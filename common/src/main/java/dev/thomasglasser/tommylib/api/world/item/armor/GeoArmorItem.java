package dev.thomasglasser.tommylib.api.world.item.armor;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

/**
 * An interface for GeckoLib armor items
 */
public interface GeoArmorItem extends GeoItem {
    /**
     * Armor has no animation by default
     * 
     * @param controllerRegistrar The controller registrar to register the controllers to
     */
    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    /**
     * Checks if the armor is skintight for rendering purposes
     * 
     * @return True if the armor is skintight and should hide the player's model when covering
     */
    boolean isSkintight();
}
