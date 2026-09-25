package dev.thomasglasser.tommylib.impl.services;

import dev.thomasglasser.tommylib.api.registration.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;

public interface RegistrationService {
    <T> Registrar<T> create(ResourceKey<? extends Registry<T>> key, String namespace);

    Registrar.Items createItems(String namespace);

    Registrar.Blocks createBlocks(String namespace);

    Registrar.DataComponents createDataComponents(ResourceKey<Registry<DataComponentType<?>>> registryKey, String namespace);

    Registrar.Entities createEntities(String namespace);
}
