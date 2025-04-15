package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.client.animation.AnimationUtils;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.platform.NeoForgeClientHelper;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class TommyLibNeoForgeClientEvents {
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (TommyLib.Dependencies.PLAYERANIMATOR.isLoaded())
            AnimationUtils.registerPlayerForAnimation();
    }

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ((NeoForgeClientHelper) TommyLibServices.CLIENT).getKeyMappings().forEach(mapping -> event.register(mapping.get()));
    }
}
