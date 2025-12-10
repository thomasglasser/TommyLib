/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 * SPDX-License-Identifier: MIT
 */

package dev.thomasglasser.tommylib.impl.registration;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
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

public class FabricRegistrationFactory implements DeferredRegister.Factory {
    @Override
    public <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        return new Provider<>(resourceKey, modId);
    }

    @Override
    public DeferredRegister.Items createItems(String namespace) {
        return new ItemsProvider(namespace);
    }

    @Override
    public DeferredRegister.Blocks createBlocks(String namespace) {
        return new BlocksProvider(namespace);
    }

    @Override
    public DeferredRegister.DataComponents createDataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
        return new DataComponentsProvider(registryKey, namespace);
    }

    @Override
    public DeferredRegister.Entities createEntities(String namespace) {
        return new EntitiesProvider(namespace);
    }

    private static class Provider<T> extends DeferredRegister<T> {
        private final Set<DeferredHolder<T, ? extends T>> entries = new HashSet<>();
        private final Set<DeferredHolder<T, ? extends T>> entriesView = Collections.unmodifiableSet(this.entries);

        protected Provider(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        @Override
        public <I extends T> DeferredHolder<T, I> register(String name, Function<Identifier, ? extends I> func) {
            final var id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            DeferredHolder<T, I> ret = DeferredHolder.create(getRegistryKey(), id);
            this.entries.add(ret);
            return ret;
        }

        @Override
        public Collection<DeferredHolder<T, ? extends T>> getEntries() {
            return this.entriesView;
        }
    }

    private static class ItemsProvider extends DeferredRegister.Items {
        private final Set<DeferredHolder<Item, ? extends Item>> entries = new HashSet<>();
        private final Set<DeferredHolder<Item, ? extends Item>> entriesView = Collections.unmodifiableSet(this.entries);

        protected ItemsProvider(String namespace) {
            super(namespace);
        }

        @Override
        public Collection<DeferredHolder<Item, ? extends Item>> getEntries() {
            return this.entriesView;
        }

        @Override
        public <I extends Item> DeferredItem<I> register(String name, Function<Identifier, ? extends I> func) {
            final var id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            DeferredItem<I> ret = DeferredItem.createItem(id);
            this.entries.add(ret);
            return ret;
        }
    }

    private static class BlocksProvider extends DeferredRegister.Blocks {
        private final Set<DeferredHolder<Block, ? extends Block>> entries = new HashSet<>();
        private final Set<DeferredHolder<Block, ? extends Block>> entriesView = Collections.unmodifiableSet(this.entries);

        protected BlocksProvider(String namespace) {
            super(namespace);
        }

        @Override
        public Collection<DeferredHolder<Block, ? extends Block>> getEntries() {
            return this.entriesView;
        }

        @Override
        public <I extends Block> DeferredBlock<I> register(String name, Function<Identifier, ? extends I> func) {
            final var id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            DeferredBlock<I> ret = DeferredBlock.createBlock(id);
            this.entries.add(ret);
            return ret;
        }
    }

    private static class DataComponentsProvider extends DeferredRegister.DataComponents {
        private final Set<DeferredHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entries = new HashSet<>();
        private final Set<DeferredHolder<DataComponentType<?>, ? extends DataComponentType<?>>> entriesView = Collections.unmodifiableSet(this.entries);

        protected DataComponentsProvider(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace) {
            super(registryKey, namespace);
        }

        @Override
        public Set<DeferredHolder<DataComponentType<?>, ? extends DataComponentType<?>>> getEntries() {
            return this.entriesView;
        }

        @Override
        public <I extends DataComponentType<?>> DeferredHolder<DataComponentType<?>, I> register(String name, Function<Identifier, ? extends I> func) {
            final var id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            DeferredHolder<DataComponentType<?>, I> ret = DeferredHolder.create(ResourceKey.create(getRegistryKey(), id));
            this.entries.add(ret);
            return ret;
        }
    }

    private static class EntitiesProvider extends DeferredRegister.Entities {
        private final Set<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entries = new HashSet<>();
        private final Set<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entriesView = Collections.unmodifiableSet(this.entries);

        protected EntitiesProvider(String namespace) {
            super(namespace);
        }

        @Override
        public Set<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> getEntries() {
            return this.entriesView;
        }

        @Override
        public <I extends EntityType<?>> DeferredHolder<EntityType<?>, I> register(String name, Function<Identifier, ? extends I> func) {
            final var id = Identifier.fromNamespaceAndPath(getNamespace(), name);
            Registry.register(getRegistry().get(), id, func.apply(id));
            DeferredHolder<EntityType<?>, I> ret = DeferredHolder.create(ResourceKey.create(getRegistryKey(), id));
            this.entries.add(ret);
            return ret;
        }
    }
}
