package dev.thomasglasser.tommylib.impl.services;

import dev.thomasglasser.tommylib.api.collection.ImmutableCollectionView;
import dev.thomasglasser.tommylib.api.registration.BlockHolder;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.ItemHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
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
        private final Set<ExtendedHolder<T, ? extends T>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<T, ? extends T>> entriesView = ImmutableCollectionView.of(entries);

        protected FabricRegistrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        @Override
        public <I extends T> ExtendedHolder<T, I> register(String name, Function<Identifier, ? extends I> func) {
            Identifier id = Identifier.fromNamespaceAndPath(namespace(), name);
            Registry.register(registry().get(), id, func.apply(id));
            ExtendedHolder<T, I> holder = ExtendedHolder.create(registryKey(), id);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<T, ? extends T>> entries() {
            return entriesView;
        }
    }

    private static class FabricItemsRegistrar extends Registrar.Items {
        private final Set<ExtendedHolder<Item, ? extends Item>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<Item, ? extends Item>> entriesView = ImmutableCollectionView.of(entries);

        protected FabricItemsRegistrar(String namespace) {
            super(namespace);
        }

        @Override
        public <I extends Item> ItemHolder<I> register(String name, Function<Identifier, ? extends I> func) {
            Identifier id = Identifier.fromNamespaceAndPath(namespace(), name);
            Registry.register(registry().get(), id, func.apply(id));
            ItemHolder<I> holder = ItemHolder.createItem(id);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<Item, ? extends Item>> entries() {
            return entriesView;
        }
    }

    private static class FabricBlocksRegistrar extends Registrar.Blocks {
        private final Set<ExtendedHolder<Block, ? extends Block>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<Block, ? extends Block>> entriesView = ImmutableCollectionView.of(entries);

        protected FabricBlocksRegistrar(String namespace) {
            super(namespace);
        }

        @Override
        public <B extends Block> BlockHolder<B> register(String name, Function<Identifier, ? extends B> func) {
            Identifier id = Identifier.fromNamespaceAndPath(namespace(), name);
            Registry.register(registry().get(), id, func.apply(id));
            BlockHolder<B> holder = BlockHolder.createBlock(id);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<Block, ? extends Block>> entries() {
            return entriesView;
        }
    }

    private static class FabricDataComponentsRegistrar extends Registrar.DataComponents {
        private final Set<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entriesView = ImmutableCollectionView.of(entries);

        protected FabricDataComponentsRegistrar(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        @Override
        public <D extends DataComponentType<?>> ExtendedHolder<DataComponentType<?>, D> register(String name, Function<Identifier, ? extends D> func) {
            Identifier id = Identifier.fromNamespaceAndPath(namespace(), name);
            Registry.register(registry().get(), id, func.apply(id));
            ExtendedHolder<DataComponentType<?>, D> holder = ExtendedHolder.create(registryKey(), id);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entries() {
            return entriesView;
        }
    }

    private static class FabricEntitiesRegistrar extends Registrar.Entities {
        private final Set<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entriesView = ImmutableCollectionView.of(entries);

        protected FabricEntitiesRegistrar(String namespace) {
            super(namespace);
        }

        @Override
        public <I extends EntityType<?>> ExtendedHolder<EntityType<?>, I> register(String name, Function<Identifier, ? extends I> func) {
            Identifier id = Identifier.fromNamespaceAndPath(namespace(), name);
            Registry.register(registry().get(), id, func.apply(id));
            ExtendedHolder<EntityType<?>, I> holder = ExtendedHolder.create(registryKey(), id);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entries() {
            return entriesView;
        }
    }
}
