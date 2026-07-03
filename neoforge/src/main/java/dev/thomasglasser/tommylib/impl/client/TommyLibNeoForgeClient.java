package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.impl.TommyLib;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = TommyLib.MOD_ID, dist = Dist.CLIENT)
public class TommyLibNeoForgeClient {
    public TommyLibNeoForgeClient(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> TommyLibClientEvents.onClientTick());

        modBus.addListener(TommyLibNeoForgeClientEvents::onRegisterKeyMappings);
        modBus.addListener(TommyLibNeoForgeClientEvents::onRegisterClientExtensions);
    }
}
