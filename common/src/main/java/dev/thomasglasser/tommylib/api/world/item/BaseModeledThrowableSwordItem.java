package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.Tier;

/**
 * Base class of {@link ModeledItem} for {@link ThrowableSwordItem}s that performs NeoForge display setup automatically.
 */
public abstract class BaseModeledThrowableSwordItem extends ThrowableSwordItem implements ModeledItem {
    protected BaseModeledThrowableSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
}
