package dev.thomasglasser.tommylib.impl.platform.services;

import java.util.function.Supplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public interface ClientHelper {
    CreativeModeTab.Builder tabBuilder();

    Supplier<KeyMapping> registerKeyMapping(ResourceLocation name, int key, String category);
}
