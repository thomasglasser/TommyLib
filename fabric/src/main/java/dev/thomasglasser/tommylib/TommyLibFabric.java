package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.api.network.FabricNetworkUtils;
import dev.thomasglasser.tommylib.impl.network.TommyLibPayloads;
import net.fabricmc.api.ModInitializer;

public class TommyLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        TommyLib.init();

        TommyLibPayloads.PAYLOADS.forEach(FabricNetworkUtils::register);
    }
}