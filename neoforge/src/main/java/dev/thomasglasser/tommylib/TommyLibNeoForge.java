package dev.thomasglasser.tommylib;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.client.TommyLibNeoForgeClientEvents;
import dev.thomasglasser.tommylib.impl.core.TommyLibNeoForgeCoreEvents;
import dev.thomasglasser.tommylib.impl.data.TommyLibDataGenerators;
import dev.thomasglasser.tommylib.impl.platform.NeoForgeEntityHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(TommyLib.MOD_ID)
public class TommyLibNeoForge {
    public TommyLibNeoForge(IEventBus eventBus) {
        TommyLib.init();
        NeoForgeEntityHelper.ATTACHMENT_TYPES.register(eventBus);

        eventBus.addListener(TommyLibDataGenerators::onGatherData);
        eventBus.addListener(TommyLibNeoForgeCoreEvents::onRegisterPackets);

        if (TommyLibServices.PLATFORM.isClientSide()) {
            NeoForge.EVENT_BUS.addListener(TommyLibNeoForgeClientEvents::onEntityJoinLevel);

            eventBus.addListener(TommyLibNeoForgeClientEvents::onRegisterClientExtensions);
            eventBus.addListener(TommyLibNeoForgeClientEvents::onRegisterKeyMappings);
            eventBus.addListener(TommyLibNeoForgeClientEvents::onClientSetup);
        }
    }
}
