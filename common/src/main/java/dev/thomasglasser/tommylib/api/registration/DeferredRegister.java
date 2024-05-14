/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package dev.thomasglasser.tommylib.api.registration;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

// Copied from NeoForge's implementation

/**
 * A helper class to aid in registering objects to modded and {@linkplain BuiltInRegistries vanilla registries} and
 * provide deferred suppliers to access those objects.
 *
 * <p>This class maintains a list of all suppliers for entries and registers them during the proper
 * event, after being registered to an event bus.
 *
 * <p>Suppliers should return <em>new</em> instances every time they are invoked.
 *
 * <p>To create an instance of this helper class, use any of the three factory methods: {@link #create(Registry, String)},
 * {@link #create(ResourceKey, String)}, or {@link #create(ResourceLocation, String)}. There are also specialized
 * subclasses of this helper for {@link Block}s and {@link Item}s, created through {@link #createBlocks(String)} and
 * {@link #createItems(String)} respectively. (Be sure to <em>store the concrete type</em> of those subclasses, rather than
 * storing them generically as {@code DeferredRegister<Block>} or {@code DeferredRegister<Item>}.)
 *
 * <p>Here are some common examples for using this class:
 *
 * <pre>{@code
 * private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
 * private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
 * private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
 *
 * // If you don't care about the actual Block class, use the simple variants
 * public static final DeferredBlock<Block> ROCK_BLOCK = BLOCKS.registerSimpleBlock("rock", Block.Properties.create(Material.ROCK));
 * public static final DeferredItem<BlockItem> ROCK_ITEM = ITEMS.registerSimpleBlockItem(ROCK_BLOCK, new Item.Properties());
 *
 * // Otherwise, use the regular (non-'simple') variants
 * public static final DeferredBlock<SpecialRockBlock> SPECIAL_ROCK_BLOCK = BLOCKS.registerBlock("special_rock",
 *         SpecialRockBlock::new, Block.Properties.create(Material.ROCK));
 * // (#registerSimpleBlockItem does not have a non-'simple' variant -- register an item in the usual way)
 * public static final DeferredItem<SpecialRockItem> SPECIAL_ROCK_ITEM = ITEMS.register("special_rock",
 *         () -> new SpecialRockItem(SPECIAL_ROCK_BLOCK.get(), new Item.Properties()))
 *
 * // (Can be DeferredHolder<BlockEntityType<?>, BlockEntityType<RockBlockEntity>> if you prefer)
 * public static final Supplier<BlockEntityType<RockBlockEntity>> ROCK_BLOCK_ENTITY = BLOCK_ENTITIES.register("rock",
 *         () -> BlockEntityType.Builder.of(RockBlockEntity::new, ROCK_BLOCK.get()).build(null));
 *
 * public ExampleMod(IEventBus modBus) {
 *     ITEMS.register(modBus);
 *     BLOCKS.register(modBus);
 *     BLOCK_ENTITIES.register(modBus);
 * }
 * }</pre>
 *
 * @param <T> the base registry type
 *
 * @see Blocks
 * @see Items
 */
public abstract class DeferredRegister<T> {
    /**
     * DeferredRegister factory for modded registries or {@linkplain BuiltInRegistries vanilla registries}.
     * <p>
     * If the registry is never created, any {@link DeferredHolder}s made from this DeferredRegister will throw an exception.
     *
     * @param registry  the registry to register to
     * @param namespace the namespace for all objects registered to this DeferredRegister
     * @see #create(ResourceKey, String)
     * @see #create(ResourceLocation, String)
     * @see #createItems(String)
     * @see #createBlocks(String)
     */
    public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
        return Factory.INSTANCE.create(registry, namespace);
    }

    /**
     * DeferredRegister factory for modded registries or {@linkplain BuiltInRegistries vanilla registries} to lookup based on the provided registry key. Supports both registries that already exist or do not exist yet.
     * <p>
     * If the registry is never created, any {@link DeferredHolder}s made from this DeferredRegister will throw an exception.
     *
     * @param key       the key of the registry to reference. May come from another DeferredRegister through {@link #getRegistryKey()}.
     * @param namespace the namespace for all objects registered to this DeferredRegister
     * @see #create(Registry, String)
     * @see #create(ResourceLocation, String)
     * @see #createItems(String)
     * @see #createBlocks(String)
     */
    public static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return Factory.INSTANCE.create(key, namespace);
    }

    /**
     * DeferredRegister factory for custom forge registries or {@link BuiltInRegistries vanilla registries} to lookup based on the provided registry name. Supports both registries that already exist or do not exist yet.
     * <p>
     * If the registry is never created, any {@link DeferredHolder}s made from this DeferredRegister will throw an exception.
     *
     * @param registryName The name of the registry, should include namespace. May come from another DeferredRegister through {@link #getRegistryName()}.
     * @param modid        The namespace for all objects registered to this DeferredRegister
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #createItems(String)
     * @see #createBlocks(String)
     */
    public static <B> DeferredRegister<B> create(ResourceLocation registryName, String modid) {
        return Factory.INSTANCE.create(ResourceKey.createRegistryKey(registryName), modid);
    }

    /**
     * Factory for a specialized {@link DeferredRegister} for {@link Item Items}.
     *
     * @param modid The namespace for all objects registered to this {@link DeferredRegister}
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #create(ResourceLocation, String)
     * @see #createBlocks(String)
     */
    public static Items createItems(String modid) {
        return Factory.INSTANCE.createItems(modid);
    }

    /**
     * Factory for a specialized DeferredRegister for {@link Block Blocks}.
     *
     * @param modid The namespace for all objects registered to this DeferredRegister
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #create(ResourceLocation, String)
     * @see #createItems(String)
     */
    public static Blocks createBlocks(String modid) {
        return Factory.INSTANCE.createBlocks(modid);
    }

    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String namespace;

    @Nullable
    private RegistryHolder<T> registryHolder;

    protected DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this.registryKey = Objects.requireNonNull(registryKey);
        this.namespace = Objects.requireNonNull(namespace);
    }

    /**
     * Adds a new entry to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created entry automatically.
     *
     * @param name The new entry's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param sup  A factory for the new entry. The factory should not cache the created entry.
     * @return A {@link DeferredHolder} that will track updates from the registry for this entry.
     */
    public abstract <I extends T> DeferredHolder<T, I> register(final String name, final Supplier<? extends I> sup);

    /**
     * Create a {@link DeferredHolder} or an inheriting type to be stored.
     *
     * @param registryKey The key of the registry.
     * @param key         The resource location of the entry.
     * @return The new instance of {@link DeferredHolder} or an inheriting type.
     * @param <I> The specific type of the entry.
     */
    protected <I extends T> DeferredHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation key) {
        return DeferredHolder.create(registryKey, key);
    }

    /**
     * Returns a supplier for the {@link Registry} linked to this deferred register.
     * <p>
     * To register additional DeferredRegisters for custom modded registries, use {@link #create(ResourceKey, String)} which can take a registry key from {@link #getRegistryKey()}.
     */
    public Supplier<Registry<T>> getRegistry() {
        if (this.registryHolder == null)
            this.registryHolder = new RegistryHolder<>(this.registryKey);

        return this.registryHolder;
    }

    /**
     * Creates a tag key based on the current namespace and provided path as the location and the registry name linked to this DeferredRegister. To control the namespace, use {@link #createTagKey(ResourceLocation)}.
     *
     * @see #createTagKey(ResourceLocation)
     */
    public TagKey<T> createTagKey(String path) {
        Objects.requireNonNull(path);
        return createTagKey(new ResourceLocation(this.namespace, path));
    }

    /**
     * Creates a tag key based on the provided resource location and the registry name linked to this DeferredRegister. To use the {@linkplain #getNamespace() current namespace} as the tag key namespace automatically, use {@link #createTagKey(String)}.
     *
     * @see #createTagKey(String)
     */
    public TagKey<T> createTagKey(ResourceLocation location) {
        Objects.requireNonNull(location);
        return TagKey.create(this.registryKey, location);
    }

    /**
     * @return The unmodifiable view of registered entries. Useful for bulk operations on all values.
     */
    public abstract Collection<DeferredHolder<T, ? extends T>> getEntries();

    /**
     * @return The registry key stored in this deferred register. Useful for creating new deferred registers based on an existing one.
     */
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    /**
     * @return The registry name stored in this deferred register. Useful for creating new deferred registers based on an existing one.
     */
    public ResourceLocation getRegistryName() {
        return this.registryKey.location();
    }

    /**
     * {@return the modid/namespace associated with this deferred register}
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Specialized DeferredRegister for {@link Block Blocks} that uses the specialized {@link DeferredBlock} as the return type for {@link #register}.
     */
    public abstract static class Blocks extends DeferredRegister<Block> {
        protected Blocks(String namespace) {
            super(Registries.BLOCK, namespace);
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param sup  A factory for the new block. The factory should not cache the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         */
        @Override
        public abstract <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> sup);

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name  The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func  A factory for the new block. The factory should not cache the created block.
         * @param props The properties for the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         * @see #registerSimpleBlock(String, BlockBehaviour.Properties)
         */
        public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
            return this.register(name, () -> func.apply(props));
        }

        /**
         * Adds a new simple {@link Block} to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
         *
         * @param name  The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param props The properties for the created block.
         * @return A {@link DeferredHolder} that will track updates from the registry for this block.
         * @see #registerBlock(String, Function, BlockBehaviour.Properties)
         */
        public DeferredBlock<Block> registerSimpleBlock(String name, BlockBehaviour.Properties props) {
            return this.registerBlock(name, Block::new, props);
        }

        @Override
        protected <I extends Block> DeferredBlock<I> createHolder(ResourceKey<? extends Registry<Block>> registryKey, ResourceLocation key) {
            return DeferredBlock.createBlock(ResourceKey.create(registryKey, key));
        }
    }

    /**
     * Specialized DeferredRegister for {@link Item Items} that uses the specialized {@link DeferredItem} as the return type for {@link #register}.
     */
    public abstract static class Items extends DeferredRegister<Item> {
        public Items(String namespace) {
            super(Registries.ITEM, namespace);
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param sup  A factory for the new item. The factory should not cache the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         */
        @Override
        public abstract <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> sup);

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         * This method uses the default {@link Item.Properties}.
         *
         * @param name  The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param block The supplier for the block to create a {@link BlockItem} for.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerSimpleBlockItem(String, Supplier, Item.Properties)
         * @see #registerSimpleBlockItem(Holder, Item.Properties)
         * @see #registerSimpleBlockItem(Holder)
         */
        public DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties) {
            return this.register(name, () -> new BlockItem(block.get(), properties));
        }

        public DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block)
        {
            return registerSimpleBlockItem(name, block, new Item.Properties());
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         * Where the name is determined by the name of the given block.
         *
         * @param block      The {@link DeferredHolder} of the {@link Block} for the {@link BlockItem}.
         * @param properties The properties for the created {@link BlockItem}.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerSimpleBlockItem(String, Supplier, Item.Properties)
         * @see #registerSimpleBlockItem(Holder)
         */
        public DeferredItem<BlockItem> registerSimpleBlockItem(Holder<Block> block, Item.Properties properties) {
            return this.registerSimpleBlockItem(block.unwrapKey().orElseThrow().location().getPath(), block::value, properties);
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         * Where the name is determined by the name of the given block and uses the default {@link Item.Properties}.
         *
         * @param block The {@link DeferredHolder} of the {@link Block} for the {@link BlockItem}.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerSimpleBlockItem(String, Supplier, Item.Properties)
         * @see #registerSimpleBlockItem(String, Supplier)
         * @see #registerSimpleBlockItem(Holder, Item.Properties)
         */
        public DeferredItem<BlockItem> registerSimpleBlockItem(Holder<Block> block) {
            return this.registerSimpleBlockItem(block, new Item.Properties());
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name  The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func  A factory for the new item. The factory should not cache the created item.
         * @param props The properties for the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function)
         * @see #registerSimpleItem(String, Item.Properties)
         * @see #registerSimpleItem(String)
         */
        public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func, Item.Properties props) {
            return this.register(name, () -> func.apply(props));
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
         * This method uses the default {@link Item.Properties}.
         *
         * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func A factory for the new item. The factory should not cache the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function, Item.Properties)
         * @see #registerSimpleItem(String, Item.Properties)
         * @see #registerSimpleItem(String)
         */
        public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func) {
            return this.registerItem(name, func, new Item.Properties());
        }

        /**
         * Adds a new simple {@link Item} with the given {@link Item.Properties properties} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name  The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param props A factory for the new item. The factory should not cache the created item.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function, Item.Properties)
         * @see #registerItem(String, Function)
         * @see #registerSimpleItem(String)
         */
        public DeferredItem<Item> registerSimpleItem(String name, Item.Properties props) {
            return this.registerItem(name, Item::new, props);
        }

        /**
         * Adds a new simple {@link Item} with the default {@link Item.Properties properties} to the list of entries to be registered and
         * returns a {@link DeferredItem} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @return A {@link DeferredItem} that will track updates from the registry for this item.
         * @see #registerItem(String, Function, Item.Properties)
         * @see #registerItem(String, Function)
         * @see #registerSimpleItem(String, Item.Properties)
         */
        public DeferredItem<Item> registerSimpleItem(String name) {
            return this.registerItem(name, Item::new, new Item.Properties());
        }

        @Override
        protected <I extends Item> DeferredItem<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key) {
            return DeferredItem.createItem(ResourceKey.create(registryKey, key));
        }
    }

    private static class RegistryHolder<V> implements Supplier<Registry<V>> {
        private final ResourceKey<? extends Registry<V>> registryKey;
        private Registry<V> registry = null;

        private RegistryHolder(ResourceKey<? extends Registry<V>> registryKey) {
            this.registryKey = registryKey;
        }

        @SuppressWarnings("unchecked")
        @Override
        public @Nullable Registry<V> get() {
            // Keep looking up the registry until it's not null
            if (this.registry == null)
                this.registry = (Registry<V>) BuiltInRegistries.REGISTRY.get(this.registryKey.location());

            return this.registry;
        }
    }

    /**
     * Factory class for {@link DeferredRegister deferred registers}. <br>
     * This class is loaded using {@link java.util.ServiceLoader Service Loaders}, and only one
     * should exist per mod loader.
     */
    public interface Factory {

        /**
         * The singleton instance of the {@link DeferredRegister.Factory}. This is different on each loader.
         */
        DeferredRegister.Factory INSTANCE = TommyLibServices.load(DeferredRegister.Factory.class);

        /**
         * Creates a {@link DeferredRegister}.
         *
         * @param resourceKey the {@link ResourceKey} of the registry to create this provider for
         * @param modId       the mod id for which the provider will register objects
         * @param <T>         the type of the provider
         * @return the provider
         */
        <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId);

        /**
         * Creates a {@link DeferredRegister}.
         *
         * @param registry the {@link Registry} to create this provider for
         * @param modId    the mod id for which the provider will register objects
         * @param <T>      the type of the provider
         * @return the provider
         */
        default <T> DeferredRegister<T> create(Registry<T> registry, String modId) {
            return this.create(registry.key(), modId);
        }

        /**
         * Creates a {@link DeferredRegister.Items}.
         *
         * @param modId    the mod id for which the provider will register objects
         * @return the provider
         */
        Items createItems(String modId);

        /**
         * Creates a {@link DeferredRegister.Blocks}.
         *
         * @param modId    the mod id for which the provider will register objects
         * @return the provider
         */
        Blocks createBlocks(String modId);
    }
}
