package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.api.network.FabricPacketUtils;
import dev.thomasglasser.tommylib.impl.network.TommyLibPackets;
import net.fabricmc.api.ModInitializer;

public class TommyLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        TommyLib.init();

        TommyLibPackets.PACKETS.forEach(FabricPacketUtils::register);
    }
}