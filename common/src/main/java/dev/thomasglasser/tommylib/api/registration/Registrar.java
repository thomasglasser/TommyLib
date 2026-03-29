package dev.thomasglasser.tommylib.api.registration;

import dev.thomasglasser.tommylib.impl.services.TommyLibServices;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jspecify.annotations.Nullable;

/**
 * A helper class to aid in registering objects to modded and {@linkplain BuiltInRegistries vanilla registries} and
 * provide deferred suppliers to access those objects.
 *
 * <p>This class maintains a list of all suppliers for entries and registers them during the proper time,
 * after being initialized with static init.
 *
 * <p>Suppliers should return <em>new</em> instances every time they are invoked.
 *
 * <p>To create an instance of this helper class, use any of the three factory methods: {@link #create(Registry, String)},
 * {@link #create(ResourceKey, String)}, or {@link #create(Identifier, String)}. There are also specialized
 * subclasses of this helper for {@link Block}s and {@link Item}s, created through {@link #createBlocks(String)} and
 * {@link #createItems(String)} respectively. (Be sure to <em>store the concrete type</em> of those subclasses, rather than
 * storing them generically as {@code Registrar<Block>} or {@code Registrar<Item>}.)
 *
 * <p>Here are some common examples for using this class:
 *
 * <pre>{@code
 * private static final Registrar.Items ITEMS = Registrar.createItems(MODID);
 * private static final Registrar.Blocks BLOCKS = Registrar.createBlocks(MODID);
 * private static final Registrar<BlockEntityType<?>> BLOCK_ENTITIES = Registrar.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
 *
 * // If you don't care about the actual Block class, use the simple variants
 * public static final BlockHolder<Block> ROCK_BLOCK = BLOCKS.registerSimpleBlock("rock", Block.Properties.create(Material.ROCK));
 * public static final ItemHolder<BlockItem> ROCK_ITEM = ITEMS.registerSimpleBlockItem(ROCK_BLOCK, new Item.Properties());
 *
 * // Otherwise, use the regular (non-'simple') variants
 * public static final BlockHolder<SpecialRockBlock> SPECIAL_ROCK_BLOCK = BLOCKS.registerBlock("special_rock",
 *         SpecialRockBlock::new, Block.Properties.create(Material.ROCK));
 * // (#registerSimpleBlock does not have a non-'simple' variant -- register an item in the usual way)
 * public static final ItemHolder<SpecialRockItem> SPECIAL_ROCK_ITEM = ITEMS.registerItem("special_rock",
 *         () -> new SpecialRockItem(SPECIAL_ROCK_BLOCK.get(), new Item.Properties()))
 *
 * // (Can be ExtendedHolder<BlockEntityType<?>, BlockEntityType<RockBlockEntity>> if you prefer)
 * public static final Supplier<BlockEntityType<RockBlockEntity>> ROCK_BLOCK_ENTITY = BLOCK_ENTITIES.register("rock",
 *         () -> BlockEntityType.Builder.of(RockBlockEntity::new, ROCK_BLOCK.get()).build(null));
 *
 * public ExampleMod() {
 *     ModItems.init();
 *     ModBlocks.init();
 *     ModBlockEntities.init();
 * }
 * }</pre>
 *
 * @param <T> the base registry type
 *
 * @see Registrar.Blocks
 * @see Registrar.Items
 */
public abstract class Registrar<T> {
    /**
     * Registrar factory for modded registries or {@linkplain BuiltInRegistries vanilla registries} to lookup based on the provided registry key. Supports both registries that already exist or do not exist yet.
     * <p>
     * If the registry is never created, any {@link ExtendedHolder}s made from this Registrar will throw an exception.
     *
     * @param key       the key of the registry to reference. May come from another Registrar through {@link #getRegistryKey()}.
     * @param namespace the namespace for all objects registered to this Registrar
     * @see #create(Registry, String)
     * @see #create(Identifier, String)
     * @see #createItems(String)
     * @see #createBlocks(String)
     */
    public static <T> Registrar<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return TommyLibServices.REGISTRATION.create(key, namespace);
    }

    /**
     * Registrar factory for modded registries or {@linkplain BuiltInRegistries vanilla registries}.
     * <p>
     * If the registry is never created, any {@link ExtendedHolder}s made from this Registrar will throw an exception.
     *
     * @param registry  the registry to register to
     * @param namespace the namespace for all objects registered to this Registrar
     * @see #create(ResourceKey, String)
     * @see #create(Identifier, String)
     * @see #createItems(String)
     * @see #createBlocks(String)
     */
    public static <T> Registrar<T> create(Registry<T> registry, String namespace) {
        return create(registry.key(), namespace);
    }

    /**
     * Registrar factory for modded registries or {@link BuiltInRegistries vanilla registries} to lookup based on the provided registry name. Supports both registries that already exist or do not exist yet.
     * <p>
     * If the registry is never created, any {@link ExtendedHolder}s made from this Registrar will throw an exception.
     *
     * @param registryName The name of the registry, should include namespace. May come from another Registrar through {@link #getRegistryName()}.
     * @param namespace    The namespace for all objects registered to this Registrar
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #createItems(String)
     * @see #createBlocks(String)
     */
    public static <B> Registrar<B> create(Identifier registryName, String namespace) {
        return create(ResourceKey.createRegistryKey(registryName), namespace);
    }

    /**
     * Factory for a specialized {@link Registrar} for {@link Item}s.
     *
     * @param namespace The namespace for all objects registered to this {@link Registrar}
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #create(Identifier, String)
     * @see #createBlocks(String)
     */
    public static Registrar.Items createItems(String namespace) {
        return TommyLibServices.REGISTRATION.createItems(namespace);
    }

    /**
     * Factory for a specialized Registrar for {@link Block Blocks}.
     *
     * @param namespace The namespace for all objects registered to this Registrar
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #create(Identifier, String)
     * @see #createItems(String)
     */
    public static Registrar.Blocks createBlocks(String namespace) {
        return TommyLibServices.REGISTRATION.createBlocks(namespace);
    }

    /**
     * Factory for a specialized Registrar for {@link DataComponentType DataComponentTypes}.
     *
     * @param registryKey The key for the data component type registry, like {@link Registries#DATA_COMPONENT_TYPE} for item data components
     * @param namespace   The namespace for all objects registered to this Registrar
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #create(Identifier, String)
     * @see #createItems(String)
     */
    public static DataComponents createDataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
        return TommyLibServices.REGISTRATION.createDataComponents(registryKey, namespace);
    }

    /**
     * Factory for a specialized Registrar for {@link EntityType EntityTypes}.
     *
     * @param namespace The namespace for all objects registered to this Registrar
     * @see #create(Registry, String)
     * @see #create(ResourceKey, String)
     * @see #create(Identifier, String)
     */
    public static Entities createEntities(String namespace) {
        return TommyLibServices.REGISTRATION.createEntities(namespace);
    }

    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String namespace;

    @Nullable
    private RegistryHolder<T> registryHolder;

    protected Registrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this.registryKey = Objects.requireNonNull(registryKey);
        this.namespace = Objects.requireNonNull(namespace);
    }

    /**
     * Adds a new entry to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created entry automatically.
     *
     * @param name The new entry's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param sup  A factory for the new entry. The factory should not cache the created entry.
     * @return A {@link ExtendedHolder} that will track updates from the registry for this entry.
     */
    public <I extends T> ExtendedHolder<T, I> register(final String name, final Supplier<? extends I> sup) {
        return this.register(name, _ -> sup.get());
    }

    /**
     * Adds a new entry to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created entry automatically.
     *
     * @param name The new entry's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param func A factory for the new entry. The factory should not cache the created entry.
     * @return A {@link ExtendedHolder} that will track updates from the registry for this entry.
     */
    public abstract <I extends T> ExtendedHolder<T, I> register(final String name, final Function<Identifier, ? extends I> func);

    /**
     * Create a {@link ExtendedHolder} or an inheriting type to be stored.
     *
     * @param registryKey The key of the registry.
     * @param key         The resource location of the entry.
     * @return The new instance of {@link ExtendedHolder} or an inheriting type.
     * @param <I> The specific type of the entry.
     */
    protected <I extends T> ExtendedHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, Identifier key) {
        return ExtendedHolder.create(registryKey, key);
    }

    /**
     * Returns a supplier for the {@link Registry} linked to this deferred register. For vanilla registries, this will always return a non-null registry. For modded registries, a non-null registry will only be returned after the registry is created.
     * <p>
     * To register additional Registrars for custom modded registries, use {@link #create(ResourceKey, String)} which can take a registry key from {@link #getRegistryKey()}.
     */
    public Supplier<Registry<T>> getRegistry() {
        if (this.registryHolder == null)
            this.registryHolder = new RegistryHolder<>(this.registryKey);

        return this.registryHolder;
    }

    /**
     * Creates a tag key based on the current namespace and provided path as the location and the registry name linked to this Registrar. To control the namespace, use {@link #createTagKey(Identifier)}.
     *
     * @see #createTagKey(Identifier)
     */
    public TagKey<T> createTagKey(String path) {
        Objects.requireNonNull(path);
        return createTagKey(Identifier.fromNamespaceAndPath(this.namespace, path));
    }

    /**
     * Creates a tag key based on the provided identifier and the registry name linked to this Registrar. To use the {@linkplain #getNamespace() current namespace} as the tag key namespace automatically, use {@link #createTagKey(String)}.
     *
     * @see #createTagKey(String)
     */
    public TagKey<T> createTagKey(Identifier id) {
        Objects.requireNonNull(id);
        return TagKey.create(this.registryKey, id);
    }

    /**
     * @return The unmodifiable view of registered entries. Useful for bulk operations on all values.
     */
    public abstract Collection<ExtendedHolder<T, ? extends T>> getEntries();

    /**
     * @return The registry key stored in this deferred register. Useful for creating new deferred registers based on an existing one.
     */
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    /**
     * @return The registry name stored in this deferred register. Useful for creating new deferred registers based on an existing one.
     */
    public Identifier getRegistryName() {
        return this.registryKey.identifier();
    }

    /**
     * {@return the modid/namespace associated with this deferred register}
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * Specialized Registrar for {@link Block}s that uses the specialized {@link BlockHolder} as the return type for {@link #registerSimple}.
     */
    public abstract static class Blocks extends Registrar<Block> {
        protected Blocks(String namespace) {
            super(Registries.BLOCK, namespace);
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         *
         * @param name The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func A factory for the new block. The factory should not cache the created block.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         */
        @Override
        public abstract <B extends Block> BlockHolder<B> register(String name, Function<Identifier, ? extends B> func);

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         *
         * @param name The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param sup  A factory for the new block. The factory should not cache the created block.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         */
        @Override
        public <B extends Block> BlockHolder<B> register(String name, Supplier<? extends B> sup) {
            return this.register(name, _ -> sup.get());
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         *
         * @param name       The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func       A factory for the new block. The factory should not cache the created block.
         * @param properties The supplied properties for the created block.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public <B extends Block> BlockHolder<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> func, Supplier<BlockBehaviour.Properties> properties) {
            return this.register(name, key -> func.apply(properties.get().setId(ResourceKey.create(Registries.BLOCK, key))));
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         *
         * @param name       The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func       A factory for the new block. The factory should not cache the created block.
         * @param properties The unary operator, which is passed a new {@link BlockBehaviour.Properties} for the created block.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         * @see #register(String, Function, Supplier)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public <B extends Block> BlockHolder<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> func, UnaryOperator<BlockBehaviour.Properties> properties) {
            return this.register(name, func, () -> properties.apply(BlockBehaviour.Properties.of()));
        }

        /**
         * Adds a new block to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         * This method uses the default {@link BlockBehaviour.Properties}.
         *
         * @param name The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func A factory for the new block. The factory should not cache the created block.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public <B extends Block> BlockHolder<B> registerSimple(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
            return this.register(name, func, UnaryOperator.identity());
        }

        /**
         * Adds a new simple {@link Block} with the given {@link BlockBehaviour.Properties properties} to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         *
         * @param name       The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param properties The supplied properties for the created block.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public BlockHolder<Block> registerSimple(String name, Supplier<BlockBehaviour.Properties> properties) {
            return this.register(name, Block::new, properties);
        }

        /**
         * Adds a new simple {@link Block} with the given {@link BlockBehaviour.Properties properties} to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         *
         * @param name       The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param properties The unary operator, which is passed a new {@link BlockBehaviour.Properties} for the created block.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String)
         */
        public BlockHolder<Block> registerSimple(String name, UnaryOperator<BlockBehaviour.Properties> properties) {
            return this.register(name, Block::new, properties);
        }

        /**
         * Adds a new simple {@link Block} with the default {@link BlockBehaviour.Properties properties} to the list of entries to be registered and returns a {@link ExtendedHolder} that will be populated with the created block automatically.
         *
         * @param name The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @return A {@link ExtendedHolder} that will track updates from the registry for this block.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         */
        public BlockHolder<Block> registerSimple(String name) {
            return this.registerSimple(name, UnaryOperator.identity());
        }

        @Override
        protected <I extends Block> BlockHolder<I> createHolder(ResourceKey<? extends Registry<Block>> registryKey, Identifier key) {
            return BlockHolder.createBlock(ResourceKey.create(registryKey, key));
        }
    }

    /**
     * Specialized Registrar for {@link Item}s that uses the specialized {@link ItemHolder} as the return type for {@link #registerSimple}.
     */
    public abstract static class Items extends Registrar<Item> {
        protected Items(String namespace) {
            super(Registries.ITEM, namespace);
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func A factory for the new item. The factory should not cache the created item.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #register(String, Supplier)
         */
        @Override
        public abstract <I extends Item> ItemHolder<I> register(String name, Function<Identifier, ? extends I> func);

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param sup  A factory for the new item. The factory should not cache the created item.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #registerSimple(String, Function)
         */
        @Override
        public <I extends Item> ItemHolder<I> register(String name, Supplier<? extends I> sup) {
            return this.register(name, key -> sup.get());
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name       The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param block      The supplier for the block to create a {@link BlockItem} for.
         * @param properties The supplied properties for the created {@link BlockItem}.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #registerSimpleBlock(String, Supplier, UnaryOperator)
         * @see #registerSimpleBlock(String, Supplier)
         * @see #registerSimpleBlock(Holder, Supplier)
         * @see #registerSimpleBlock(Holder, UnaryOperator)
         * @see #registerSimpleBlock(Holder)
         */
        public ItemHolder<BlockItem> registerSimpleBlock(String name, Supplier<? extends Block> block, Supplier<Item.Properties> properties) {
            return this.register(name, props -> new BlockItem(block.get(), props), () -> properties.get().useBlockDescriptionPrefix());
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name       The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param block      The supplier for the block to create a {@link BlockItem} for.
         * @param properties The unary operator, which is passed a new {@link Item.Properties} for the created {@link BlockItem}.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #registerSimpleBlock(String, Supplier, Supplier)
         * @see #registerSimpleBlock(String, Supplier)
         * @see #registerSimpleBlock(Holder, Supplier)
         * @see #registerSimpleBlock(Holder, UnaryOperator)
         * @see #registerSimpleBlock(Holder)
         */
        public ItemHolder<BlockItem> registerSimpleBlock(String name, Supplier<? extends Block> block, UnaryOperator<Item.Properties> properties) {
            return this.registerSimpleBlock(name, block, () -> properties.apply(new Item.Properties()));
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         * This method uses the default {@link Item.Properties}.
         *
         * @param name  The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param block The supplier for the block to create a {@link BlockItem} for.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #registerSimpleBlock(String, Supplier, Supplier)
         * @see #registerSimpleBlock(String, Supplier, UnaryOperator)
         * @see #registerSimpleBlock(Holder, Supplier)
         * @see #registerSimpleBlock(Holder, UnaryOperator)
         * @see #registerSimpleBlock(Holder)
         */
        public ItemHolder<BlockItem> registerSimpleBlock(String name, Supplier<? extends Block> block) {
            return this.registerSimpleBlock(name, block, UnaryOperator.identity());
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         * Where the name is determined by the name of the given block.
         *
         * @param block      The {@link ExtendedHolder} of the {@link Block} for the {@link BlockItem}.
         * @param properties The supplied properties for the created {@link BlockItem}.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #registerSimpleBlock(String, Supplier, Supplier)
         * @see #registerSimpleBlock(String, Supplier, UnaryOperator)
         * @see #registerSimpleBlock(String, Supplier)
         * @see #registerSimpleBlock(Holder, UnaryOperator)
         * @see #registerSimpleBlock(Holder)
         */
        public ItemHolder<BlockItem> registerSimpleBlock(Holder<Block> block, Supplier<Item.Properties> properties) {
            return this.registerSimpleBlock(block.unwrapKey().orElseThrow().identifier().getPath(), block::value, properties);
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         * Where the name is determined by the name of the given block.
         *
         * @param block      The {@link ExtendedHolder} of the {@link Block} for the {@link BlockItem}.
         * @param properties The unary operator, which is passed a new {@link Item.Properties} for the created {@link BlockItem}.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #registerSimpleBlock(String, Supplier, Supplier)
         * @see #registerSimpleBlock(String, Supplier, UnaryOperator)
         * @see #registerSimpleBlock(String, Supplier)
         * @see #registerSimpleBlock(Holder, Supplier)
         * @see #registerSimpleBlock(Holder)
         */
        public ItemHolder<BlockItem> registerSimpleBlock(Holder<Block> block, UnaryOperator<Item.Properties> properties) {
            return this.registerSimpleBlock(block, () -> properties.apply(new Item.Properties()));
        }

        /**
         * Adds a new simple {@link BlockItem} for the given {@link Block} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         * Where the name is determined by the name of the given block and uses the default {@link Item.Properties}.
         *
         * @param block The {@link ExtendedHolder} of the {@link Block} for the {@link BlockItem}.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #registerSimpleBlock(String, Supplier, Supplier)
         * @see #registerSimpleBlock(String, Supplier, UnaryOperator)
         * @see #registerSimpleBlock(String, Supplier)
         * @see #registerSimpleBlock(Holder, Supplier)
         * @see #registerSimpleBlock(Holder, UnaryOperator)
         */
        public ItemHolder<BlockItem> registerSimpleBlock(Holder<Block> block) {
            return this.registerSimpleBlock(block, UnaryOperator.identity());
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name       The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func       A factory for the new item. The factory should not cache the created item.
         * @param properties The supplied properties for the created item.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public <I extends Item> ItemHolder<I> register(String name, Function<Item.Properties, ? extends I> func, Supplier<Item.Properties> properties) {
            return this.register(name, key -> func.apply(properties.get().setId(ResourceKey.create(Registries.ITEM, key))));
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name       The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func       A factory for the new item. The factory should not cache the created item.
         * @param properties The unary operator, which is passed a new {@link Item.Properties} for the created item.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #register(String, Function, Supplier)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public <I extends Item> ItemHolder<I> register(String name, Function<Item.Properties, ? extends I> func, UnaryOperator<Item.Properties> properties) {
            return this.register(name, func, () -> properties.apply(new Item.Properties()));
        }

        /**
         * Adds a new item to the list of entries to be registered and returns a {@link ItemHolder} that will be populated with the created item automatically.
         * This method uses the default {@link Item.Properties}.
         *
         * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param func A factory for the new item. The factory should not cache the created item.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public <I extends Item> ItemHolder<I> registerSimple(String name, Function<Item.Properties, ? extends I> func) {
            return this.register(name, func, UnaryOperator.identity());
        }

        /**
         * Adds a new simple {@link Item} with the given {@link Item.Properties properties} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name       The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param properties The supplied properties for the created item.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, UnaryOperator)
         * @see #registerSimple(String)
         */
        public ItemHolder<Item> registerSimple(String name, Supplier<Item.Properties> properties) {
            return this.register(name, Item::new, properties);
        }

        /**
         * Adds a new simple {@link Item} with the given {@link Item.Properties properties} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name       The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param properties The unary operator, which is passed a new {@link Item.Properties} for the created item.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String)
         */
        public ItemHolder<Item> registerSimple(String name, UnaryOperator<Item.Properties> properties) {
            return this.register(name, Item::new, properties);
        }

        /**
         * Adds a new simple {@link Item} with the default {@link Item.Properties properties} to the list of entries to be registered and
         * returns a {@link ItemHolder} that will be populated with the created item automatically.
         *
         * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @return A {@link ItemHolder} that will track updates from the registry for this item.
         * @see #register(String, Function, Supplier)
         * @see #register(String, Function, UnaryOperator)
         * @see #registerSimple(String, Function)
         * @see #registerSimple(String, Supplier)
         * @see #registerSimple(String, UnaryOperator)
         */
        public ItemHolder<Item> registerSimple(String name) {
            return this.register(name, Item::new, UnaryOperator.identity());
        }

        @Override
        protected <I extends Item> ItemHolder<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, Identifier key) {
            return ItemHolder.createItem(ResourceKey.create(registryKey, key));
        }
    }

    /**
     * Specialized Registrar for {@link DataComponentType}s.
     */
    public abstract static class DataComponents extends Registrar<DataComponentType<?>> {
        protected DataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        /**
         * Convenience method that constructs a builder for use in the operator. Use this to avoid inference issues.
         *
         * @param name    The name for this data component type. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param builder The unary operator, which is passed a new builder for user operations, then builds it upon registration.
         * @return A {@link ExtendedHolder} which reflects the data that will be registered.
         */
        public <D> ExtendedHolder<DataComponentType<?>, DataComponentType<D>> registerSimple(String name, UnaryOperator<DataComponentType.Builder<D>> builder) {
            return this.register(name, () -> builder.apply(DataComponentType.builder()).build());
        }
    }

    /**
     * Specialized Registrar for {@link EntityType}s.
     */
    public abstract static class Entities extends Registrar<EntityType<?>> {
        protected Entities(String namespace) {
            super(Registries.ENTITY_TYPE, namespace);
        }

        /**
         * Convenience method that constructs a builder. Use this to avoid inference issues.
         *
         * @param name     The name for this entity type. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param factory  The factory used to typically construct the entity when using an existing helper from the type.
         * @param category The category of the entity, typically {@link MobCategory#MISC} for non-living entities, or one of the others for living entities.
         * @return A {@link ExtendedHolder} which reflects the data that will be registered.
         * @param <E> the type of the entity
         */
        public <E extends Entity> ExtendedHolder<EntityType<?>, EntityType<E>> register(String name, EntityType.EntityFactory<E> factory, MobCategory category) {
            return this.register(name, factory, category, UnaryOperator.identity());
        }

        /**
         * Convenience method that constructs a builder for use in the operator. Use this to avoid inference issues.
         *
         * @param name     The name for this entity type. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
         * @param factory  The factory used to typically construct the entity when using an existing helper from the type.
         * @param category The category of the entity, typically {@link MobCategory#MISC} for non-living entities, or one of the others for living entities.
         * @param builder  The unary operator, which is passed a new builder for user operators, then builds it upon registration.
         * @return A {@link ExtendedHolder} which reflects the data that will be registered.
         * @param <E> the type of the entity
         */
        public <E extends Entity> ExtendedHolder<EntityType<?>, EntityType<E>> register(String name, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> builder) {
            return this.register(name, key -> builder.apply(EntityType.Builder.of(factory, category)).build(ResourceKey.create(Registries.ENTITY_TYPE, key)));
        }
    }

    private static class RegistryHolder<V> implements Supplier<Registry<V>> {
        private final ResourceKey<? extends Registry<V>> registryKey;
        private Registry<V> registry = null;

        private RegistryHolder(ResourceKey<? extends Registry<V>> registryKey) {
            this.registryKey = registryKey;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public @Nullable Registry<V> get() {
            // Keep looking up the registry until it's not null
            if (this.registry == null)
                this.registry = (Registry<V>) BuiltInRegistries.REGISTRY.getValueOrThrow((ResourceKey) this.registryKey);

            return this.registry;
        }
    }
}
