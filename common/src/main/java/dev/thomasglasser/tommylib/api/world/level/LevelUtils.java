package dev.thomasglasser.tommylib.api.world.level;

import dev.thomasglasser.tommylib.api.tags.ConventionalBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Helpers for world interactions
 */
public final class LevelUtils {
    /**
     * Safely make a block at a position fall if the block is not unbreakable.
     * 
     * @param level The level the block is in
     * @param pos   The position of the block
     */
    public static void safeFall(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(ConventionalBlockTags.UNBREAKABLE_BLOCKS))
            FallingBlockEntity.fall(level, pos, state);
    }

    /**
     * Safely destroy a block at a position if the block is not unbreakable.
     * 
     * @param level The level the block is in
     * @param pos   The position of the block
     * @param drop  Whether to drop the block's loot table
     */
    public static void safeDestroy(Level level, BlockPos pos, boolean drop) {
        if (!level.getBlockState(pos).is(ConventionalBlockTags.UNBREAKABLE_BLOCKS)) {
            level.destroyBlock(pos, drop);
        }
    }
}
