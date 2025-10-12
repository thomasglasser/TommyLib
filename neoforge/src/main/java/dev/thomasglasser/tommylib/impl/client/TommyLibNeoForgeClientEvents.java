package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.platform.NeoForgeClientHelper;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class TommyLibNeoForgeClientEvents {
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ((NeoForgeClientHelper) TommyLibServices.CLIENT).getKeyMappings().forEach(mapping -> event.register(mapping.get()));
    }
}
