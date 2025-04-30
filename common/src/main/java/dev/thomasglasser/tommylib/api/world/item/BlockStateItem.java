package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link BlockItem} that places a {@link BlockState} when used.
 */
public class BlockStateItem extends BlockItem {
    private final BlockState state;

    public BlockStateItem(BlockState state, Properties properties) {
        super(state.getBlock(), properties);
        this.state = state;
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        return this.canPlace(context, state) ? state : null;
    }

    public BlockState getState() {
        return state;
    }
}
