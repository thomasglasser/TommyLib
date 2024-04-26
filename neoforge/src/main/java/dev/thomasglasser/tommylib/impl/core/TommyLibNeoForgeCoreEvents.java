package dev.thomasglasser.tommylib.impl.core;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.NeoForgePacketUtils;
import dev.thomasglasser.tommylib.impl.network.TommyLibPackets;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class TommyLibNeoForgeCoreEvents
{
	public static void onRegisterPackets(RegisterPayloadHandlersEvent event)
	{
		PayloadRegistrar registrar = event.registrar(TommyLib.MOD_ID);
		TommyLibPackets.PACKETS.forEach((info) -> NeoForgePacketUtils.register(registrar, info));
	}
}
