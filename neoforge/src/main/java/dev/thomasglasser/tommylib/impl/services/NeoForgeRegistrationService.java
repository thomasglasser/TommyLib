package dev.thomasglasser.tommylib.impl.services;

import dev.thomasglasser.tommylib.api.collection.ImmutableCollectionView;
import dev.thomasglasser.tommylib.api.registration.BlockHolder;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.ItemHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NeoForgeRegistrationService implements RegistrationService {
    @Override
    public <T> Registrar<T> create(ResourceKey<? extends Registry<T>> key, String namespace) {
        return new NeoForgeRegistrar<>(namespace, createDeferredRegister(key, namespace));
    }

    @Override
    public Registrar.Items createItems(String namespace) {
        return new NeoForgeItemsRegistrar(namespace, createDeferredRegister(Registries.ITEM, namespace));
    }

    @Override
    public Registrar.Blocks createBlocks(String namespace) {
        return new NeoForgeBlocksRegistrar(namespace, createDeferredRegister(Registries.BLOCK, namespace));
    }

    @Override
    public Registrar.DataComponents createDataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
        return new NeoForgeDataComponentsRegistrar(registryKey, namespace, createDeferredRegister(registryKey, namespace));
    }

    @Override
    public Registrar.Entities createEntities(String namespace) {
        return new NeoForgeEntitiesRegistrar(namespace, createDeferredRegister(Registries.ENTITY_TYPE, namespace));
    }

    private static <T> DeferredRegister<T> createDeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        final Optional<? extends ModContainer> containerOpt = ModList.get().getModContainerById(namespace);
        if (containerOpt.isEmpty())
            throw new NullPointerException("Cannot find mod container for id " + namespace);
        final ModContainer cont = containerOpt.get();
        if (cont instanceof FMLModContainer fmlModContainer) {
            final DeferredRegister<T> register = DeferredRegister.create(registryKey, namespace);
            IEventBus modBus = fmlModContainer.getEventBus();
            if (modBus == null)
                throw new NullPointerException("Cannot find event bus for mod " + namespace);
            register.register(modBus);
            return register;
        }
        throw new ClassCastException("The container of the mod " + namespace + " is not a FML one!");
    }

    private static class NeoForgeRegistrar<T> extends Registrar<T> {
        private final DeferredRegister<T> registry;

        private final ObjectOpenHashSet<ExtendedHolder<T, ? extends T>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<T, ? extends T>> entriesView = ImmutableCollectionView.of(entries);

        private NeoForgeRegistrar(String namespace, DeferredRegister<T> registry) {
            super(registry.getRegistryKey(), namespace);
            this.registry = registry;
        }

        @Override
        public <I extends T> ExtendedHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
            ResourceKey<T> key = registry.register(name, func).getKey();
            ExtendedHolder<T, I> holder = ExtendedHolder.create(key);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<T, ? extends T>> entries() {
            return entriesView;
        }
    }

    private static class NeoForgeItemsRegistrar extends Registrar.Items {
        private final DeferredRegister<Item> registry;

        private final ObjectOpenHashSet<ExtendedHolder<Item, ? extends Item>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<Item, ? extends Item>> entriesView = ImmutableCollectionView.of(entries);

        private NeoForgeItemsRegistrar(String namespace, DeferredRegister<Item> registry) {
            super(namespace);
            this.registry = registry;
        }

        @Override
        public <I extends Item> ItemHolder<I> register(String name, Function<ResourceLocation, ? extends I> func) {
            ResourceKey<Item> key = registry.register(name, func).getKey();
            ItemHolder<I> holder = ItemHolder.createItem(key);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<Item, ? extends Item>> entries() {
            return entriesView;
        }
    }

    private static class NeoForgeBlocksRegistrar extends Registrar.Blocks {
        private final DeferredRegister<Block> registry;

        private final ObjectOpenHashSet<ExtendedHolder<Block, ? extends Block>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<Block, ? extends Block>> entriesView = ImmutableCollectionView.of(entries);

        private NeoForgeBlocksRegistrar(String namespace, DeferredRegister<Block> registry) {
            super(namespace);
            this.registry = registry;
        }

        @Override
        public <B extends Block> BlockHolder<B> register(String name, Function<ResourceLocation, ? extends B> func) {
            ResourceKey<Block> key = registry.register(name, func).getKey();
            BlockHolder<B> holder = BlockHolder.createBlock(key);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<Block, ? extends Block>> entries() {
            return entriesView;
        }
    }

    private static class NeoForgeDataComponentsRegistrar extends Registrar.DataComponents {
        private final DeferredRegister<DataComponentType<?>> registry;

        private final ObjectOpenHashSet<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entriesView = ImmutableCollectionView.of(entries);

        private NeoForgeDataComponentsRegistrar(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace, DeferredRegister<DataComponentType<?>> registry) {
            super(registryKey, namespace);
            this.registry = registry;
        }

        @Override
        public <D extends DataComponentType<?>> ExtendedHolder<DataComponentType<?>, D> register(String name, Function<ResourceLocation, ? extends D> func) {
            ResourceKey<DataComponentType<?>> key = registry.register(name, func).getKey();
            ExtendedHolder<DataComponentType<?>, D> holder = ExtendedHolder.create(key);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entries() {
            return entriesView;
        }
    }

    private static class NeoForgeEntitiesRegistrar extends Registrar.Entities {
        private final DeferredRegister<EntityType<?>> registry;

        private final ObjectOpenHashSet<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entries = new ObjectOpenHashSet<>();
        private final ImmutableCollectionView<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entriesView = ImmutableCollectionView.of(entries);

        private NeoForgeEntitiesRegistrar(String namespace, DeferredRegister<EntityType<?>> registry) {
            super(namespace);
            this.registry = registry;
        }

        @Override
        public <E extends EntityType<?>> ExtendedHolder<EntityType<?>, E> register(String name, Function<ResourceLocation, ? extends E> func) {
            ResourceKey<EntityType<?>> key = registry.register(name, func).getKey();
            ExtendedHolder<EntityType<?>, E> holder = ExtendedHolder.create(key);
            entries.add(holder);
            return holder;
        }

        @Override
        public ImmutableCollectionView<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entries() {
            return entriesView;
        }
    }
}
