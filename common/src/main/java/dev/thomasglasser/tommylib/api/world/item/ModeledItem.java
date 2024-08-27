package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

/**
 * An item that has custom rendering via a {@link BlockEntityWithoutLevelRenderer}.
 */
public interface ModeledItem {
    void createBewlrProvider(Consumer<BewlrProvider> provider);
}
