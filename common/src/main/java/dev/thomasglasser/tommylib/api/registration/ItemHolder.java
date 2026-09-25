package dev.thomasglasser.tommylib.api.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Special {@link ExtendedHolder} for {@link Item}s that implements {@link ItemLike}.
 *
 * @param <T> The specific {@link Item} type.
 */
public class ItemHolder<T extends Item> extends ExtendedHolder<Item, T> implements ItemLike {
    /**
     * Constructs a new {@link ItemHolder} pointing to the specified item resource key.
     *
     * @param key the resource key of the item
     */
    protected ItemHolder(ResourceKey<Item> key) {
        super(key);
    }

    /**
     * Creates a new {@link ItemHolder} targeting the {@link Item} with the specified name.
     *
     * @param key The name of the target {@link Item}.
     * @param <T> The type of the target {@link Item}.
     * @return a new {@link ItemHolder} instance.
     */
    public static <T extends Item> ItemHolder<T> createItem(ResourceLocation key) {
        return createItem(ResourceKey.create(Registries.ITEM, key));
    }

    /**
     * Creates a new {@link ItemHolder} targeting the specified {@link Item} key.
     *
     * @param key The resource key of the target {@link Item}.
     * @param <T> The type of the target {@link Item}.
     * @return a new {@link ItemHolder} instance.
     */
    public static <T extends Item> ItemHolder<T> createItem(ResourceKey<Item> key) {
        return new ItemHolder<>(key);
    }

    /**
     * Creates a new {@link ItemStack} with a default count of 1 from this {@link Item}.
     *
     * @return the created {@link ItemStack}.
     */
    public ItemStack toStack() {
        return toStack(1);
    }

    /**
     * Creates a new {@link ItemStack} with the given count from this {@link Item}.
     *
     * @param count The size of the stack to create.
     * @return the created {@link ItemStack}.
     */
    public ItemStack toStack(int count) {
        ItemStack stack = asItem().getDefaultInstance();
        if (stack.isEmpty())
            throw new IllegalStateException("Obtained empty item stack; incorrect getDefaultInstance() call for: " + key());
        stack.setCount(count);
        return stack;
    }

    @Override
    public Item asItem() {
        return get();
    }
}
