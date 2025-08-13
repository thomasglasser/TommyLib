package dev.thomasglasser.tommylib.api.world.item;

import dev.thomasglasser.tommylib.api.client.renderer.BewlrProvider;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

/**
 * An item that has custom rendering via a {@link BlockEntityWithoutLevelRenderer}.
 *
 * @deprecated No longer needed in 1.21.5+
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public interface ModeledItem {
    void createBewlrProvider(Consumer<BewlrProvider> provider);
}
