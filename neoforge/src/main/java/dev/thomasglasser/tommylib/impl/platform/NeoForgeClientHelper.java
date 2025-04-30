package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.impl.platform.services.ClientHelper;
import net.minecraft.world.item.CreativeModeTab;

public class NeoForgeClientHelper implements ClientHelper {
    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return CreativeModeTab.builder();
    }
}
