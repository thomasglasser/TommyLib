package dev.thomasglasser.tommylib.api.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
     * Constructs a new {@link BlockHolder} pointing to the specified block resource key.
     *
     * @param key the resource key of the block
     */
    protected BlockHolder(ResourceKey<Block> key) {
        super(key);
    }

    /**
     * Creates a new {@link BlockHolder} targeting the {@link Block} with the specified name.
     *
     * @param key The name of the target {@link Block}.
     * @param <T> The type of the target {@link Block}.
     * @return a new {@link BlockHolder} instance.
     */
    public static <T extends Block> BlockHolder<T> createBlock(ResourceLocation key) {
        return createBlock(ResourceKey.create(Registries.BLOCK, key));
    }

    /**
     * Creates a new {@link BlockHolder} targeting the specified {@link Block} key.
     *
     * @param key The resource key of the target {@link Block}.
     * @param <T> The type of the target {@link Block}.
     * @return a new {@link BlockHolder} instance.
     */
    public static <T extends Block> BlockHolder<T> createBlock(ResourceKey<Block> key) {
        return new BlockHolder<>(key);
    }

    /**
     * Creates a new {@link ItemStack} with a default count of 1 from this {@link Block}.
     *
     * @return the created {@link ItemStack}.
     */
    public ItemStack toStack() {
        return toStack(1);
    }

    /**
     * Creates a new {@link ItemStack} with the given count from this {@link Block}.
     *
     * @param count The size of the stack to create.
     * @return the created {@link ItemStack}.
     */
    public ItemStack toStack(int count) {
        ItemStack stack = asItem().getDefaultInstance();
        if (stack.isEmpty())
            throw new IllegalStateException("Block does not have a corresponding item: " + key());
        stack.setCount(count);
        return stack;
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }
}
