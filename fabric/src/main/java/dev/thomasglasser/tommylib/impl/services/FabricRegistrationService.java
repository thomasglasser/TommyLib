package dev.thomasglasser.tommylib.impl.services;

import dev.thomasglasser.tommylib.api.registration.BlockHolder;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.ItemHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class FabricRegistrationService implements RegistrationService {
    @Override
    public <T> Registrar<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return new FabricRegistrar<>(key, namespace);
    }

    @Override
    public Registrar.Items createItems(String namespace) {
        return new FabricItemsRegistrar(namespace);
    }

    @Override
    public Registrar.Blocks createBlocks(String namespace) {
        return new FabricBlocksRegistrar(namespace);
    }

    @Override
    public Registrar.DataComponents createDataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
        return new FabricDataComponentsRegistrar(registryKey, namespace);
    }

    @Override
    public Registrar.Entities createEntities(String namespace) {
        return new FabricEntitiesRegistrar(namespace);
    }

    private static class FabricRegistrar<T> extends Registrar<T> {
        private final Set<ExtendedHolder<T, ? extends T>> entries = new HashSet<>();
        private final Set<ExtendedHolder<T, ? extends T>> entriesView = Collections.unmodifiableSet(this.entries);

        protected FabricRegistrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        @Override
        public <I extends T> ExtendedHolder<T, I> register(String name, Function<Identifier, ? extends I> func) {
            Identifier id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            ExtendedHolder<T, I> holder = ExtendedHolder.create(getRegistryKey(), id);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Collection<ExtendedHolder<T, ? extends T>> getEntries() {
            return this.entriesView;
        }
    }

    private static class FabricItemsRegistrar extends Registrar.Items {
        private final Set<ExtendedHolder<Item, ? extends Item>> entries = new HashSet<>();
        private final Set<ExtendedHolder<Item, ? extends Item>> entriesView = Collections.unmodifiableSet(this.entries);

        protected FabricItemsRegistrar(String namespace) {
            super(namespace);
        }

        @Override
        public <I extends Item> ItemHolder<I> register(String name, Function<Identifier, ? extends I> func) {
            Identifier id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            ItemHolder<I> holder = ItemHolder.createItem(id);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Collection<ExtendedHolder<Item, ? extends Item>> getEntries() {
            return this.entriesView;
        }
    }

    private static class FabricBlocksRegistrar extends Registrar.Blocks {
        private final Set<ExtendedHolder<Block, ? extends Block>> entries = new HashSet<>();
        private final Set<ExtendedHolder<Block, ? extends Block>> entriesView = Collections.unmodifiableSet(this.entries);

        protected FabricBlocksRegistrar(String namespace) {
            super(namespace);
        }

        @Override
        public <B extends Block> BlockHolder<B> register(String name, Function<Identifier, ? extends B> func) {
            Identifier id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            BlockHolder<B> holder = BlockHolder.createBlock(id);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<Block, ? extends Block>> getEntries() {
            return this.entriesView;
        }
    }

    private static class FabricDataComponentsRegistrar extends Registrar.DataComponents {
        private final Set<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entries = new HashSet<>();
        private final Set<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entriesView = Collections.unmodifiableSet(this.entries);

        protected FabricDataComponentsRegistrar(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        @Override
        public <D extends DataComponentType<?>> ExtendedHolder<DataComponentType<?>, D> register(String name, Function<Identifier, ? extends D> func) {
            Identifier id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            ExtendedHolder<DataComponentType<?>, D> holder = ExtendedHolder.create(getRegistryKey(), id);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> getEntries() {
            return this.entriesView;
        }
    }

    private static class FabricEntitiesRegistrar extends Registrar.Entities {
        private final Set<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entries = new HashSet<>();
        private final Set<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entriesView = Collections.unmodifiableSet(this.entries);

        protected FabricEntitiesRegistrar(String namespace) {
            super(namespace);
        }

        @Override
        public <I extends EntityType<?>> ExtendedHolder<EntityType<?>, I> register(String name, Function<Identifier, ? extends I> func) {
            Identifier id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            ExtendedHolder<EntityType<?>, I> holder = ExtendedHolder.create(getRegistryKey(), id);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> getEntries() {
            return this.entriesView;
        }
    }
}
