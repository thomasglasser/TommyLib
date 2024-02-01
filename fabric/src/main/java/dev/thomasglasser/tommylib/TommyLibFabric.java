package dev.thomasglasser.tommylib;

import net.fabricmc.api.ModInitializer;

public class TommyLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        TommyLib.init();
    }
}