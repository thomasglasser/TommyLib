package dev.thomasglasser.tommylib.impl.core;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.NeoForgePacketUtils;
import dev.thomasglasser.tommylib.impl.network.TommyLibPackets;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class TommyLibNeoForgeCoreEvents
{
	public static void onRegisterPackets(RegisterPayloadHandlerEvent event)
	{
		IPayloadRegistrar registrar = event.registrar(TommyLib.MOD_ID);
		TommyLibPackets.PACKETS.forEach((packet, pair) -> NeoForgePacketUtils.register(registrar, packet, pair));
	}
}
