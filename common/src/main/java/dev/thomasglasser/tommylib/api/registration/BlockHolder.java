package dev.thomasglasser.tommylib.api.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

/// Special [ExtendedHolder] for [Block]s that implements [ItemLike].
///
/// @param <T> The specific [Block] type.
public class BlockHolder<T extends Block> extends ExtendedHolder<Block, T> implements ItemLike {
    /// Constructs a new [BlockHolder] pointing to the specified block resource key.
    ///
    /// @param key the resource key of the block
    protected BlockHolder(ResourceKey<Block> key) {
        super(key);
    }

    /// Creates a new [BlockHolder] targeting the [Block] with the specified name.
    ///
    /// @param key The name of the target [Block].
    /// @param <T> The type of the target [Block].
    /// @return a new [BlockHolder] instance.
    public static <T extends Block> BlockHolder<T> createBlock(Identifier key) {
        return createBlock(ResourceKey.create(Registries.BLOCK, key));
    }

    /// Creates a new [BlockHolder] targeting the specified [Block] key.
    ///
    /// @param key The resource key of the target [Block].
    /// @param <T> The type of the target [Block].
    /// @return a new [BlockHolder] instance.
    public static <T extends Block> BlockHolder<T> createBlock(ResourceKey<Block> key) {
        return new BlockHolder<>(key);
    }

    /// Creates a new [ItemStack] with a default count of 1 from this [Block].
    ///
    /// @return the created [ItemStack].
    public ItemStack toStack() {
        return toStack(1);
    }

    /// Creates a new [ItemStack] with the given count from this [Block].
    ///
    /// @param count The size of the stack to create.
    /// @return the created [ItemStack].
    public ItemStack toStack(int count) {
        ItemStack stack = asItem().getDefaultInstance();
        if (stack.isEmpty())
            throw new IllegalStateException("Block does not have a corresponding item: " + key);
        stack.setCount(count);
        return stack;
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
