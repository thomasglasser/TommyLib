package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.impl.platform.services.ClientHelper;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class FabricClientHelper implements ClientHelper {
    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return FabricItemGroup.builder();
    }

    @Override
    public Supplier<KeyMapping> registerKeyMapping(ResourceLocation name, int key, String category) {
        KeyMapping mapping = KeyBindingHelper.registerKeyBinding(new KeyMapping(name.toLanguageKey("key"), key, category));
        return () -> mapping;
    }
}
