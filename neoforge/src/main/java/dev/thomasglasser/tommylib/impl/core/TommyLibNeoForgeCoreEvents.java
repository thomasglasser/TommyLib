package dev.thomasglasser.tommylib.impl.core;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.NeoForgeNetworkUtils;
import dev.thomasglasser.tommylib.impl.network.TommyLibPayloads;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class TommyLibNeoForgeCoreEvents {
    public static void onRegisterPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(TommyLib.MOD_ID);
        TommyLibPayloads.PAYLOADS.forEach((info) -> NeoForgeNetworkUtils.register(registrar, info));
    }
}
