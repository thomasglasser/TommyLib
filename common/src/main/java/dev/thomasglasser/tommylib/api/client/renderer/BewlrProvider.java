package dev.thomasglasser.tommylib.api.client.renderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

/**
 * Provides a side safe way to provide a {@link BlockEntityWithoutLevelRenderer} instance.
 */
public interface BewlrProvider {
    BewlrProvider DEFAULT = new BewlrProvider() {};

    default BlockEntityWithoutLevelRenderer getBewlr() {
        return null;
    }
}
