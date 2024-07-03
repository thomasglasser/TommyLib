/*
 * SPDX-FileCopyrightText: 2023 klikli-dev
 * SPDX-License-Identifier: MIT
 */

package dev.thomasglasser.tommylib.impl.registration;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;

public class NeoForgeRegistrationFactory implements DeferredRegister.Factory {
    @Override
    public <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        final var containerOpt = ModList.get().getModContainerById(modId);
        if (containerOpt.isEmpty())
            throw new NullPointerException("Cannot find mod container for id " + modId);
        final var cont = containerOpt.get();
        if (cont instanceof FMLModContainer fmlModContainer) {
            final var register = net.neoforged.neoforge.registries.DeferredRegister.create(resourceKey, modId);
            register.register(fmlModContainer.getEventBus());
            return new Provider<>(modId, register);
        } else {
            throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
        }
    }

    @Override
    public DeferredRegister.Items createItems(String modId) {
        final var containerOpt = ModList.get().getModContainerById(modId);
        if (containerOpt.isEmpty())
            throw new NullPointerException("Cannot find mod container for id " + modId);
        final var cont = containerOpt.get();
        if (cont instanceof FMLModContainer fmlModContainer) {
            final var register = net.neoforged.neoforge.registries.DeferredRegister.create(Registries.ITEM, modId);
            register.register(fmlModContainer.getEventBus());
            return new ItemsProvider(modId, register);
        } else {
            throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
        }
    }

    @Override
    public DeferredRegister.Blocks createBlocks(String modId) {
        final var containerOpt = ModList.get().getModContainerById(modId);
        if (containerOpt.isEmpty())
            throw new NullPointerException("Cannot find mod container for id " + modId);
        final var cont = containerOpt.get();
        if (cont instanceof FMLModContainer fmlModContainer) {
            final var register = net.neoforged.neoforge.registries.DeferredRegister.create(Registries.BLOCK, modId);
            register.register(fmlModContainer.getEventBus());
            return new BlocksProvider(modId, register);
        } else {
            throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
        }
    }

    private static class Provider<T> extends DeferredRegister<T> {
        private final net.neoforged.neoforge.registries.DeferredRegister<T> registry;

        private final Set<DeferredHolder<T, ? extends T>> entries = new HashSet<>();
        private final Set<DeferredHolder<T, ? extends T>> entriesView = Collections.unmodifiableSet(this.entries);

        private Provider(String modId, net.neoforged.neoforge.registries.DeferredRegister<T> registry) {
            super(registry.getRegistryKey(), modId);
            this.registry = registry;
        }

        @Override
        public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
            final var obj = this.registry.<I>register(name, supplier);
            DeferredHolder<T, I> h = DeferredHolder.create(obj.getKey());
            this.entries.add(h);
            return h;
        }

        @Override
        public Set<DeferredHolder<T, ? extends T>> getEntries() {
            return this.entriesView;
        }
    }

    private static class ItemsProvider extends DeferredRegister.Items {
        private final net.neoforged.neoforge.registries.DeferredRegister<Item> registry;

        private final Set<DeferredHolder<Item, ? extends Item>> entries = new HashSet<>();
        private final Set<DeferredHolder<Item, ? extends Item>> entriesView = Collections.unmodifiableSet(this.entries);

        private ItemsProvider(String modId, net.neoforged.neoforge.registries.DeferredRegister<Item> registry) {
            super(modId);
            this.registry = registry;
        }

        @Override
        public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> supplier) {
            final var obj = this.registry.<I>register(name, supplier);
            DeferredItem<I> h = DeferredItem.createItem(obj.getKey());
            this.entries.add(h);
            return h;
        }

        @Override
        public Set<DeferredHolder<Item, ? extends Item>> getEntries() {
            return this.entriesView;
        }
    }

    private static class BlocksProvider extends DeferredRegister.Blocks {
        private final net.neoforged.neoforge.registries.DeferredRegister<Block> registry;

        private final Set<DeferredHolder<Block, ? extends Block>> entries = new HashSet<>();
        private final Set<DeferredHolder<Block, ? extends Block>> entriesView = Collections.unmodifiableSet(this.entries);

        private BlocksProvider(String modId, net.neoforged.neoforge.registries.DeferredRegister<Block> registry) {
            super(modId);
            this.registry = registry;
        }

        @Override
        public <I extends Block> DeferredBlock<I> register(String name, Supplier<? extends I> supplier) {
            final var obj = this.registry.<I>register(name, supplier);
            DeferredBlock<I> h = DeferredBlock.createBlock(obj.getKey());
            this.entries.add(h);
            return h;
        }

        @Override
        public Set<DeferredHolder<Block, ? extends Block>> getEntries() {
            return this.entriesView;
        }
    }
}
