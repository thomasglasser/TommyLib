package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.client.TommyLibNeoForgeClientEvents;
import dev.thomasglasser.tommylib.impl.data.TommyLibDataGenerators;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(TommyLib.MOD_ID)
public class TommyLibNeoForge
{
    public TommyLibNeoForge(IEventBus eventBus) {
        TommyLib.init();

        eventBus.addListener(TommyLibDataGenerators::onGatherData);

        if (TommyLibServices.PLATFORM.isClientSide())
        {
            eventBus.addListener(TommyLibNeoForgeClientEvents::onBuildCreativeTabContent);
        }
    }
}