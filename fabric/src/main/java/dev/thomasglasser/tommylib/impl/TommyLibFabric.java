package dev.thomasglasser.tommylib.impl;

import net.fabricmc.api.ModInitializer;

public class TommyLibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TommyLib.init();
    }
}
