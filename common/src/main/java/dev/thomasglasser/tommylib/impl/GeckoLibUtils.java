package dev.thomasglasser.tommylib.impl;

import dev.thomasglasser.tommylib.api.world.item.armor.GeoArmorItem;
import net.minecraft.world.item.Item;

/**
 * Uses of GeckoLib classes that will crash if GeckoLib isn't installed
 */
public class GeckoLibUtils {
    /**
     * Checks if an item should disable outer model rendering.
     * 
     * @param item The item to check
     * @return Whether the outer model rendering should be disabled
     */
    public static boolean isSkintight(Item item) {
        return item instanceof GeoArmorItem geoArmorItem && geoArmorItem.isSkintight();
    }
}
