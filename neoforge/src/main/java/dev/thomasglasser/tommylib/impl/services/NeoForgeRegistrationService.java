package dev.thomasglasser.tommylib.impl.services;

import dev.thomasglasser.tommylib.api.registration.BlockHolder;
import dev.thomasglasser.tommylib.api.registration.ExtendedHolder;
import dev.thomasglasser.tommylib.api.registration.ItemHolder;
import dev.thomasglasser.tommylib.api.registration.Registrar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
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
        final var containerOpt = ModList.get().getModContainerById(namespace);
        if (containerOpt.isEmpty())
            throw new NullPointerException("Cannot find mod container for id " + namespace);
        final var cont = containerOpt.get();
        if (cont instanceof FMLModContainer fmlModContainer) {
            final var register = DeferredRegister.create(registryKey, namespace);
            IEventBus modBus = fmlModContainer.getEventBus();
            if (modBus == null)
                throw new NullPointerException("Cannot find event bus for mod " + namespace);
            register.register(modBus);
            return register;
        } else {
            throw new ClassCastException("The container of the mod " + namespace + " is not a FML one!");
        }
    }

    private static class NeoForgeRegistrar<T> extends Registrar<T> {
        private final DeferredRegister<T> registry;

        private final Set<ExtendedHolder<T, ? extends T>> entries = new HashSet<>();
        private final Set<ExtendedHolder<T, ? extends T>> entriesView = Collections.unmodifiableSet(this.entries);

        private NeoForgeRegistrar(String namespace, DeferredRegister<T> registry) {
            super(registry.getRegistryKey(), namespace);
            this.registry = registry;
        }

        @Override
        public <I extends T> ExtendedHolder<T, I> register(String name, Function<Identifier, ? extends I> func) {
            ResourceKey<T> key = this.registry.register(name, func).getKey();
            ExtendedHolder<T, I> holder = ExtendedHolder.create(key);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<T, ? extends T>> getEntries() {
            return this.entriesView;
        }
    }

    private static class NeoForgeItemsRegistrar extends Registrar.Items {
        private final DeferredRegister<Item> registry;

        private final Set<ExtendedHolder<Item, ? extends Item>> entries = new HashSet<>();
        private final Set<ExtendedHolder<Item, ? extends Item>> entriesView = Collections.unmodifiableSet(this.entries);

        private NeoForgeItemsRegistrar(String namespace, DeferredRegister<Item> registry) {
            super(namespace);
            this.registry = registry;
        }

        @Override
        public <I extends Item> ItemHolder<I> register(String name, Function<Identifier, ? extends I> func) {
            ResourceKey<Item> key = this.registry.register(name, func).getKey();
            ItemHolder<I> holder = ItemHolder.createItem(key);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<Item, ? extends Item>> getEntries() {
            return this.entriesView;
        }
    }

    private static class NeoForgeBlocksRegistrar extends Registrar.Blocks {
        private final DeferredRegister<Block> registry;

        private final Set<ExtendedHolder<Block, ? extends Block>> entries = new HashSet<>();
        private final Set<ExtendedHolder<Block, ? extends Block>> entriesView = Collections.unmodifiableSet(this.entries);

        private NeoForgeBlocksRegistrar(String namespace, DeferredRegister<Block> registry) {
            super(namespace);
            this.registry = registry;
        }

        @Override
        public <B extends Block> BlockHolder<B> register(String name, Function<Identifier, ? extends B> func) {
            ResourceKey<Block> key = this.registry.register(name, func).getKey();
            BlockHolder<B> holder = BlockHolder.createBlock(key);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<Block, ? extends Block>> getEntries() {
            return this.entriesView;
        }
    }

    private static class NeoForgeDataComponentsRegistrar extends Registrar.DataComponents {
        private final DeferredRegister<DataComponentType<?>> registry;

        private final Set<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entries = new HashSet<>();
        private final Set<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entriesView = Collections.unmodifiableSet(this.entries);

        private NeoForgeDataComponentsRegistrar(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace, DeferredRegister<DataComponentType<?>> registry) {
            super(registryKey, namespace);
            this.registry = registry;
        }

        @Override
        public <D extends DataComponentType<?>> ExtendedHolder<DataComponentType<?>, D> register(String name, Function<Identifier, ? extends D> func) {
            ResourceKey<DataComponentType<?>> key = this.registry.register(name, func).getKey();
            ExtendedHolder<DataComponentType<?>, D> holder = ExtendedHolder.create(key);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<DataComponentType<?>, ? extends DataComponentType<?>>> getEntries() {
            return this.entriesView;
        }
    }

    private static class NeoForgeEntitiesRegistrar extends Registrar.Entities {
        private final DeferredRegister<EntityType<?>> registry;

        private final Set<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entries = new HashSet<>();
        private final Set<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> entriesView = Collections.unmodifiableSet(this.entries);

        private NeoForgeEntitiesRegistrar(String namespace, DeferredRegister<EntityType<?>> registry) {
            super(namespace);
            this.registry = registry;
        }

        @Override
        public <E extends EntityType<?>> ExtendedHolder<EntityType<?>, E> register(String name, Function<Identifier, ? extends E> func) {
            ResourceKey<EntityType<?>> key = this.registry.register(name, func).getKey();
            ExtendedHolder<EntityType<?>, E> holder = ExtendedHolder.create(key);
            this.entries.add(holder);
            return holder;
        }

        @Override
        public Set<ExtendedHolder<EntityType<?>, ? extends EntityType<?>>> getEntries() {
            return this.entriesView;
        }
    }
}
