package dev.thomasglasser.tommylib.impl.platform.services;

import java.util.function.Supplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

public interface ClientHelper {
    CreativeModeTab.Builder tabBuilder();

    Supplier<KeyMapping> registerKeyMapping(Identifier id, int key, KeyMapping.Category category);
}
