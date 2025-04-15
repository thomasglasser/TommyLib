package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.client.TommyLibNeoForgeClientEvents;
import dev.thomasglasser.tommylib.impl.data.TommyLibDataGenerators;
import dev.thomasglasser.tommylib.impl.platform.NeoForgeEntityHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(TommyLib.MOD_ID)
public class TommyLibNeoForge {
    public TommyLibNeoForge(IEventBus eventBus) {
        TommyLib.init();
        NeoForgeEntityHelper.ATTACHMENT_TYPES.register(eventBus);

        eventBus.addListener(TommyLibDataGenerators::onGatherData);

        if (TommyLibServices.PLATFORM.isClientSide()) {
            eventBus.addListener(TommyLibNeoForgeClientEvents::onRegisterKeyMappings);
            eventBus.addListener(TommyLibNeoForgeClientEvents::onClientSetup);
        }
    }
}
