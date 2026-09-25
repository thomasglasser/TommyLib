package dev.thomasglasser.tommylib.api.registration;

import dev.thomasglasser.tommylib.api.collection.ImmutableCollectionView;
import dev.thomasglasser.tommylib.impl.services.TommyLibServices;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

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
 * {@link #create(ResourceKey, String)}, or {@link #create(ResourceLocation, String)}. There are also specialized
 * subclasses of this helper for {@link Block}s, {@link Item}s, {@link DataComponentType}s, and {@link EntityType}s.
 *
 * @param <T> the base registry type
 * @see Registrar.Blocks
 * @see Registrar.Items
 * @see Registrar.DataComponents
 * @see Registrar.Entities
 */
public abstract class Registrar<T> {
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final String namespace;

    @Nullable
    private RegistryHolder<T> registryHolder;

    /**
     * Constructs a new {@link Registrar}.
     *
     * @param registryKey the registry key
     * @param namespace   the mod namespace
     */
    protected Registrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        this.registryKey = registryKey;
        this.namespace = namespace;
    }

    /**
     * Registrar factory for modded registries or {@linkplain BuiltInRegistries vanilla registries} to lookup based on the provided registry key.
     *
     * @param key       the key of the registry to reference.
     * @param namespace the namespace for all objects registered to this Registrar.
     * @param <T>       the registry entry type.
     * @return a new {@link Registrar} instance.
     */
    public static <T> Registrar<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return TommyLibServices.REGISTRATION.create(key, namespace);
    }

    /**
     * Registrar factory for modded registries or {@linkplain BuiltInRegistries vanilla registries}.
     *
     * @param registry  the registry to register to.
     * @param namespace the namespace for all objects registered to this Registrar.
     * @param <T>       the registry entry type.
     * @return a new {@link Registrar} instance.
     */
    public static <T> Registrar<T> create(Registry<T> registry, String namespace) {
        return create(registry.key(), namespace);
    }

    /**
     * Registrar factory for modded registries or {@link BuiltInRegistries vanilla registries} to lookup based on the provided registry name.
     *
     * @param registryName The name of the registry, should include namespace.
     * @param namespace    The namespace for all objects registered to this Registrar.
     * @param <T>          the registry entry type.
     * @return a new {@link Registrar} instance.
     */
    public static <T> Registrar<T> create(ResourceLocation registryName, String namespace) {
        return create(ResourceKey.createRegistryKey(registryName), namespace);
    }

    /**
     * Factory for a specialized {@link Registrar} for {@link Item}s.
     *
     * @param namespace The namespace for all objects registered to this {@link Registrar}.
     * @return a new specialized {@link Registrar.Items} instance.
     */
    public static Items createItems(String namespace) {
        return TommyLibServices.REGISTRATION.createItems(namespace);
    }

    /**
     * Factory for a specialized Registrar for {@link Block Blocks}.
     *
     * @param namespace The namespace for all objects registered to this Registrar.
     * @return a new specialized {@link Registrar.Blocks} instance.
     */
    public static Blocks createBlocks(String namespace) {
        return TommyLibServices.REGISTRATION.createBlocks(namespace);
    }

    /**
     * Factory for a specialized Registrar for {@link DataComponentType DataComponentTypes} using the default registry key.
     *
     * @param namespace The namespace for all objects registered to this Registrar.
     * @return a new specialized {@link DataComponents} instance.
     */
    public static DataComponents createDataComponents(String namespace) {
        return createDataComponents(Registries.DATA_COMPONENT_TYPE, namespace);
    }

    /**
     * Factory for a specialized Registrar for {@link DataComponentType DataComponentTypes}.
     *
     * @param registryKey The key for the data component type registry.
     * @param namespace   The namespace for all objects registered to this Registrar.
     * @return a new specialized {@link DataComponents} instance.
     */
    public static DataComponents createDataComponents(ResourceKey<? extends Registry<DataComponentType<?>>> registryKey, String namespace) {
        return TommyLibServices.REGISTRATION.createDataComponents(registryKey, namespace);
    }

    /**
     * Factory for a specialized Registrar for {@link EntityType EntityTypes}.
     *
     * @param namespace The namespace for all objects registered to this Registrar.
     * @return a new specialized {@link Entities} instance.
     */
    public static Entities createEntities(String namespace) {
        return TommyLibServices.REGISTRATION.createEntities(namespace);
    }

    /**
     * Adds a new entry to the list of entries to be registered and returns an {@link ExtendedHolder} that will be populated with the created entry automatically.
     *
     * @param name The new entry's name. It will automatically have the {@linkplain #namespace() namespace} prefixed.
     * @param sup  A factory for the new entry.
     * @param <I>  the specific entry type.
     * @return an {@link ExtendedHolder} tracking updates from the registry for this entry.
     */
    public <I extends T> ExtendedHolder<T, I> register(final String name, final Supplier<? extends I> sup) {
        return register(name, key -> sup.get());
    }

    /**
     * Adds a new entry to the list of entries to be registered and returns an {@link ExtendedHolder} that will be populated with the created entry automatically.
     *
     * @param name The new entry's name. It will automatically have the {@linkplain #namespace() namespace} prefixed.
     * @param func A factory for the new entry, accepting the resolved {@link ResourceLocation}.
     * @param <I>  the specific entry type.
     * @return an {@link ExtendedHolder} tracking updates from the registry for this entry.
     */
    public abstract <I extends T> ExtendedHolder<T, I> register(final String name, final Function<ResourceLocation, ? extends I> func);

    /**
     * Create an {@link ExtendedHolder} or an inheriting type to be stored.
     *
     * @param registryKey The key of the registry.
     * @param key         The resource location of the entry.
     * @param <I>         The specific type of the entry.
     * @return The new instance of {@link ExtendedHolder} or an inheriting type.
     */
    protected <I extends T> ExtendedHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation key) {
        return ExtendedHolder.create(registryKey, key);
    }

    /**
     * Returns a supplier for the {@link Registry} linked to this registrar.
     *
     * @return the registry supplier.
     */
    public Supplier<Registry<T>> registry() {
        if (registryHolder == null)
            registryHolder = new RegistryHolder<>(registryKey);

        return registryHolder;
    }

    /**
     * Creates a tag key based on the current namespace and provided path as the location and the registry name linked to this Registrar.
     *
     * @param path The path of the tag.
     * @return the created {@link TagKey}.
     */
    public TagKey<T> createTagKey(String path) {
        return createTagKey(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    /**
     * Creates a tag key based on the provided identifier and the registry name linked to this Registrar.
     *
     * @param id The tag identifier.
     * @return the created {@link TagKey}.
     */
    public TagKey<T> createTagKey(ResourceLocation id) {
        return TagKey.create(registryKey, id);
    }

    /**
     * Returns an unmodifiable view of registered entries.
     *
     * @return The unmodifiable collection of registered entries.
     */
    public abstract ImmutableCollectionView<ExtendedHolder<T, ? extends T>> entries();

    /**
     * Returns the registry key stored in this registrar.
     *
     * @return The registry key.
     */
    public ResourceKey<? extends Registry<T>> registryKey() {
        return registryKey;
    }

    /**
     * Returns the registry name stored in this registrar.
     *
     * @return The registry name as a {@link ResourceLocation}.
     */
    public ResourceLocation registryName() {
        return registryKey.location();
    }

    /**
     * Returns the modid/namespace associated with this registrar.
     *
     * @return the modid/namespace.
     */
    public String namespace() {
        return namespace;
    }

    /** Specialized Registrar for {@link Block}s that uses the specialized {@link BlockHolder} as the return type. */
    public abstract static class Blocks extends Registrar<Block> {
        /**
         * Constructs a new block registrar.
         *
         * @param namespace the mod namespace
         */
        protected Blocks(String namespace) {
            super(Registries.BLOCK, namespace);
        }

        @Override
        public abstract <B extends Block> BlockHolder<B> register(String name, Function<ResourceLocation, ? extends B> func);

        @Override
        public <B extends Block> BlockHolder<B> register(String name, Supplier<? extends B> sup) {
            return register(name, key -> sup.get());
        }

        /**
         * Registers a block using a factory that accepts {@link BlockBehaviour.Properties} and a properties supplier.
         *
         * @param name       the block entry name
         * @param func       the block factory
         * @param properties the properties supplier
         * @param <B>        the block type
         * @return a {@link BlockHolder} for the registered block
         */
        public <B extends Block> BlockHolder<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> func, Supplier<BlockBehaviour.Properties> properties) {
            return register(name, key -> func.apply(properties.get()));
        }

        /**
         * Registers a block using a factory that accepts {@link BlockBehaviour.Properties} and a properties operator.
         *
         * @param name       the block entry name
         * @param func       the block factory
         * @param properties the properties operator
         * @param <B>        the block type
         * @return a {@link BlockHolder} for the registered block
         */
        public <B extends Block> BlockHolder<B> register(String name, Function<BlockBehaviour.Properties, ? extends B> func, UnaryOperator<BlockBehaviour.Properties> properties) {
            return register(name, func, () -> properties.apply(BlockBehaviour.Properties.of()));
        }

        /**
         * Registers a block with default properties using the provided factory.
         *
         * @param name the block entry name
         * @param func the block factory
         * @param <B>  the block type
         * @return a {@link BlockHolder} for the registered block
         */
        public <B extends Block> BlockHolder<B> registerSimple(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
            return register(name, func, UnaryOperator.identity());
        }

        /**
         * Registers a standard {@link Block} with the given properties supplier.
         *
         * @param name       the block entry name
         * @param properties the properties supplier
         * @return a {@link BlockHolder} for the registered block
         */
        public BlockHolder<Block> registerSimple(String name, Supplier<BlockBehaviour.Properties> properties) {
            return register(name, Block::new, properties);
        }

        /**
         * Registers a standard {@link Block} with the given properties operator.
         *
         * @param name       the block entry name
         * @param properties the properties operator
         * @return a {@link BlockHolder} for the registered block
         */
        public BlockHolder<Block> registerSimple(String name, UnaryOperator<BlockBehaviour.Properties> properties) {
            return register(name, Block::new, properties);
        }

        /**
         * Registers a standard {@link Block} with default properties.
         *
         * @param name the block entry name
         * @return a {@link BlockHolder} for the registered block
         */
        public BlockHolder<Block> registerSimple(String name) {
            return registerSimple(name, UnaryOperator.identity());
        }

        @Override
        protected <I extends Block> BlockHolder<I> createHolder(ResourceKey<? extends Registry<Block>> registryKey, ResourceLocation key) {
            return BlockHolder.createBlock(ResourceKey.create(registryKey, key));
        }
    }

    /** Specialized Registrar for {@link Item}s that uses the specialized {@link ItemHolder} as the return type. */
    public abstract static class Items extends Registrar<Item> {
        /**
         * Constructs a new item registrar.
         *
         * @param namespace the mod namespace
         */
        protected Items(String namespace) {
            super(Registries.ITEM, namespace);
        }

        @Override
        public abstract <I extends Item> ItemHolder<I> register(String name, Function<ResourceLocation, ? extends I> func);

        @Override
        public <I extends Item> ItemHolder<I> register(String name, Supplier<? extends I> sup) {
            return register(name, key -> sup.get());
        }

        /**
         * Registers an item using a factory accepting {@link Item.Properties} and a properties supplier.
         *
         * @param name       the item entry name
         * @param func       the item factory
         * @param properties the properties supplier
         * @param <I>        the item type
         * @return an {@link ItemHolder} for the registered item
         */
        public <I extends Item> ItemHolder<I> register(String name, Function<Item.Properties, ? extends I> func, Supplier<Item.Properties> properties) {
            return register(name, key -> func.apply(properties.get()));
        }

        /**
         * Registers an item using a factory accepting {@link Item.Properties} and a properties operator.
         *
         * @param name       the item entry name
         * @param func       the item factory
         * @param properties the properties operator
         * @param <I>        the item type
         * @return an {@link ItemHolder} for the registered item
         */
        public <I extends Item> ItemHolder<I> register(String name, Function<Item.Properties, ? extends I> func, UnaryOperator<Item.Properties> properties) {
            return register(name, func, () -> properties.apply(new Item.Properties()));
        }

        /**
         * Registers an item with default properties using the provided factory.
         *
         * @param name the item entry name
         * @param func the item factory
         * @param <I>  the item type
         * @return an {@link ItemHolder} for the registered item
         */
        public <I extends Item> ItemHolder<I> registerSimple(String name, Function<Item.Properties, ? extends I> func) {
            return register(name, func, UnaryOperator.identity());
        }

        /**
         * Registers a standard {@link Item} with the given properties supplier.
         *
         * @param name       the item entry name
         * @param properties the properties supplier
         * @return an {@link ItemHolder} for the registered item
         */
        public ItemHolder<Item> registerSimple(String name, Supplier<Item.Properties> properties) {
            return register(name, Item::new, properties);
        }

        /**
         * Registers a standard {@link Item} with the given properties operator.
         *
         * @param name       the item entry name
         * @param properties the properties operator
         * @return an {@link ItemHolder} for the registered item
         */
        public ItemHolder<Item> registerSimple(String name, UnaryOperator<Item.Properties> properties) {
            return register(name, Item::new, properties);
        }

        /**
         * Registers a standard {@link Item} with default properties.
         *
         * @param name the item entry name
         * @return an {@link ItemHolder} for the registered item
         */
        public ItemHolder<Item> registerSimple(String name) {
            return registerSimple(name, UnaryOperator.identity());
        }

        /**
         * Registers a simple {@link BlockItem} for a given block supplier and properties supplier.
         *
         * @param name       the item entry name
         * @param block      the block supplier
         * @param properties the properties supplier
         * @return an {@link ItemHolder} for the registered block item
         */
        public ItemHolder<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block, Supplier<Item.Properties> properties) {
            return register(name, props -> new BlockItem(block.get(), props), properties);
        }

        /**
         * Registers a simple {@link BlockItem} for a given block supplier and properties operator.
         *
         * @param name       the item entry name
         * @param block      the block supplier
         * @param properties the properties operator
         * @return an {@link ItemHolder} for the registered block item
         */
        public ItemHolder<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block, UnaryOperator<Item.Properties> properties) {
            return registerSimpleBlockItem(name, block, () -> properties.apply(new Item.Properties()));
        }

        /**
         * Registers a simple {@link BlockItem} for a given block supplier with default properties.
         *
         * @param name  the item entry name
         * @param block the block supplier
         * @return an {@link ItemHolder} for the registered block item
         */
        public ItemHolder<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block) {
            return registerSimpleBlockItem(name, block, UnaryOperator.identity());
        }

        /**
         * Registers a simple {@link BlockItem} using the block's path name and the given properties supplier.
         *
         * @param block      the block holder
         * @param properties the properties supplier
         * @return an {@link ItemHolder} for the registered block item
         */
        public ItemHolder<BlockItem> registerSimpleBlockItem(Holder<Block> block, Supplier<Item.Properties> properties) {
            return registerSimpleBlockItem(block.unwrapKey().orElseThrow().location().getPath(), block::value, properties);
        }

        /**
         * Registers a simple {@link BlockItem} using the block's path name and the given properties operator.
         *
         * @param block      the block holder
         * @param properties the properties operator
         * @return an {@link ItemHolder} for the registered block item
         */
        public ItemHolder<BlockItem> registerSimpleBlockItem(Holder<Block> block, UnaryOperator<Item.Properties> properties) {
            return registerSimpleBlockItem(block, () -> properties.apply(new Item.Properties()));
        }

        /**
         * Registers a simple {@link BlockItem} using the block's path name with default properties.
         *
         * @param block the block holder
         * @return an {@link ItemHolder} for the registered block item
         */
        public ItemHolder<BlockItem> registerSimpleBlockItem(Holder<Block> block) {
            return registerSimpleBlockItem(block, UnaryOperator.identity());
        }

        @Override
        protected <I extends Item> ItemHolder<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key) {
            return ItemHolder.createItem(ResourceKey.create(registryKey, key));
        }
    }

    /** Specialized Registrar for {@link DataComponentType}s. */
    public abstract static class DataComponents extends Registrar<DataComponentType<?>> {
        /**
         * Constructs a new data component type registrar.
         *
         * @param registryKey the data component type registry key
         * @param namespace   the mod namespace
         */
        protected DataComponents(ResourceKey<? extends Registry<DataComponentType<?>>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        /**
         * Registers a data component type using a builder operator.
         *
         * @param name    the component entry name
         * @param builder the builder configuration operator
         * @param <D>     the component value type
         * @return an {@link ExtendedHolder} for the registered component type
         */
        public <D> ExtendedHolder<DataComponentType<?>, DataComponentType<D>> registerSimple(String name, UnaryOperator<DataComponentType.Builder<D>> builder) {
            return register(name, () -> builder.apply(DataComponentType.builder()).build());
        }
    }

    /** Specialized Registrar for {@link EntityType}s. */
    public abstract static class Entities extends Registrar<EntityType<?>> {
        /**
         * Constructs a new entity type registrar.
         *
         * @param namespace the mod namespace
         */
        protected Entities(String namespace) {
            super(Registries.ENTITY_TYPE, namespace);
        }

        /**
         * Registers an entity type using a factory and mob category.
         *
         * @param name     the entity type entry name
         * @param factory  the entity factory
         * @param category the mob category
         * @param <E>      the entity type
         * @return an {@link ExtendedHolder} for the registered entity type
         */
        public <E extends Entity> ExtendedHolder<EntityType<?>, EntityType<E>> register(String name, EntityType.EntityFactory<E> factory, MobCategory category) {
            return register(name, factory, category, UnaryOperator.identity());
        }

        /**
         * Registers an entity type using a factory, mob category, and builder operator.
         *
         * @param name     the entity type entry name
         * @param factory  the entity factory
         * @param category the mob category
         * @param builder  the builder configuration operator
         * @param <E>      the entity type
         * @return an {@link ExtendedHolder} for the registered entity type
         */
        public <E extends Entity> ExtendedHolder<EntityType<?>, EntityType<E>> register(String name, EntityType.EntityFactory<E> factory, MobCategory category, UnaryOperator<EntityType.Builder<E>> builder) {
            return register(name, key -> builder.apply(EntityType.Builder.of(factory, category)).build(name));
        }
    }

    private static class RegistryHolder<V> implements Supplier<Registry<V>> {
        private final ResourceKey<? extends Registry<V>> registryKey;
        private Registry<V> registry = null;

        private RegistryHolder(ResourceKey<? extends Registry<V>> registryKey) {
            this.registryKey = registryKey;
        }

        @SuppressWarnings({ "unchecked" })
        @Override
        public @Nullable Registry<V> get() {
            if (registry == null)
                registry = (Registry<V>) BuiltInRegistries.REGISTRY.get(registryKey.location());
            return registry;
        }
    }
}
