package dev.thomasglasser.tommylib.impl.platform;

import com.google.common.base.Suppliers;
import dev.thomasglasser.tommylib.impl.platform.services.ClientHelper;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class NeoForgeClientHelper implements ClientHelper {
    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return CreativeModeTab.builder();
    }

    private final ArrayList<Supplier<KeyMapping>> keyMappings = new ArrayList<>();

    public ArrayList<Supplier<KeyMapping>> getKeyMappings() {
        return keyMappings;
    }

    @Override
    public Supplier<KeyMapping> registerKeyMapping(ResourceLocation name, int key, String category) {
        Supplier<KeyMapping> mapping = Suppliers.memoize(() -> new KeyMapping(name.toLanguageKey("key"), key, category));
        keyMappings.add(mapping);
        return mapping;
    }
}
