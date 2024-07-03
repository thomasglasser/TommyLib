package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

/**
 * An item that has custom rendering via a {@link BlockEntityWithoutLevelRenderer}.
 */
public interface ModeledItem {
    BlockEntityWithoutLevelRenderer getBEWLR();
}
