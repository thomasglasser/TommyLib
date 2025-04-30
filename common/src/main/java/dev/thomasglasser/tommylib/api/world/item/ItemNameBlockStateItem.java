package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A {@link BlockStateItem} that has a different description ID than the block.
 *
 * @deprecated No longer needed in 1.21.5+
 */
@Deprecated(forRemoval = true, since = "31.0.0")
public class ItemNameBlockStateItem extends BlockStateItem {
    public ItemNameBlockStateItem(BlockState block, Item.Properties properties) {
        super(block, properties);
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
}
