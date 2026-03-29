package dev.thomasglasser.tommylib.api.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

/**
 * Special {@link ExtendedHolder} for {@link Block Blocks} that implements {@link ItemLike}.
 *
 * @param <T> The specific {@link Block} type.
 */
public class BlockHolder<T extends Block> extends ExtendedHolder<Block, T> implements ItemLike {
    /**
     * Creates a new {@link ItemStack} with a default size of 1 from this {@link Block}
     */
    public ItemStack toStack() {
        return toStack(1);
    }

    /**
     * Creates a new {@link ItemStack} with the given size from this {@link Block}
     *
     * @param count The size of the stack to create
     */
    public ItemStack toStack(int count) {
        ItemStack stack = asItem().getDefaultInstance();
        if (stack.isEmpty()) throw new IllegalStateException("Block does not have a corresponding item: " + this.key);
        stack.setCount(count);
        return stack;
    }

    /**
     * Creates a new {@link ExtendedHolder} targeting the {@link Block} with the specified name.
     *
     * @param <T> The type of the target {@link Block}.
     * @param key The name of the target {@link Block}.
     */
    public static <T extends Block> BlockHolder<T> createBlock(Identifier key) {
        return createBlock(ResourceKey.create(Registries.BLOCK, key));
    }

    /**
     * Creates a new {@link ExtendedHolder} targeting the specified {@link Block}.
     *
     * @param <T> The type of the target {@link Block}.
     * @param key The resource key of the target {@link Block}.
     */
    public static <T extends Block> BlockHolder<T> createBlock(ResourceKey<Block> key) {
        return new BlockHolder<>(key);
    }

    protected BlockHolder(ResourceKey<Block> key) {
        super(key);
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
