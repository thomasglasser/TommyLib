package dev.thomasglasser.tommylib.impl;

import dev.thomasglasser.tommylib.impl.data.TommyLibDataGenerators;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(dev.thomasglasser.tommylib.impl.TommyLib.MOD_ID)
public class TommyLibNeoForge {
    public TommyLibNeoForge(IEventBus modBus) {
        TommyLib.init();

        modBus.addListener(TommyLibDataGenerators::onGatherData);
    }
}
