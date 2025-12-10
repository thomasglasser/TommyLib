package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.impl.platform.services.ClientHelper;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

public class FabricClientHelper implements ClientHelper {
    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return FabricItemGroup.builder();
    }

    @Override
    public Supplier<KeyMapping> registerKeyMapping(Identifier id, int key, KeyMapping.Category category) {
        KeyMapping mapping = KeyBindingHelper.registerKeyBinding(new KeyMapping(id.toLanguageKey("key"), key, category));
        return () -> mapping;
    }
}
