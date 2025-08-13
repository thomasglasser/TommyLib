package dev.thomasglasser.tommylib.api.client.renderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

/**
 * Provides a side safe way to provide a {@link BlockEntityWithoutLevelRenderer} instance.
 *
 * @deprecated No longer needed in 1.21.5+
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public interface BewlrProvider {
    BlockEntityWithoutLevelRenderer getBewlr();
}
