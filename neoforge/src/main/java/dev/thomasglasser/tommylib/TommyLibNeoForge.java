package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.impl.data.TommyLibDataGenerators;
import dev.thomasglasser.tommylib.impl.network.TommyLibNeoForgePayloads;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(TommyLib.MOD_ID)
public class TommyLibNeoForge {
    public TommyLibNeoForge(IEventBus modBus) {
        TommyLib.init();

        modBus.addListener(TommyLibDataGenerators::onGatherData);

        modBus.addListener(TommyLibNeoForgePayloads::onRegisterPayloadHandlers);
    }
}
