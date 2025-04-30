package dev.thomasglasser.tommylib.impl.platform.services;

import net.minecraft.world.item.CreativeModeTab;

/**
 * Platform-specific and side-safe client helpers
 */
public interface ClientHelper {
    /**
     * Creates a new {@link CreativeModeTab.Builder}
     * 
     * @return the new {@link CreativeModeTab.Builder}
     */
    CreativeModeTab.Builder tabBuilder();
}
