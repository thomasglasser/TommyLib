package dev.thomasglasser.tommylib.api.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricPacketUtils
{
	public static <T extends ExtendedPacketPayload> void register(PacketInfo<T> info)
	{
		if (info.direction() == ExtendedPacketPayload.Direction.CLIENT_TO_SERVER)
		{
			PayloadTypeRegistry.playC2S().register(info.type(), info.codec());
			ServerPlayNetworking.registerGlobalReceiver(info.type(), (payload, context) ->
					payload.handle(context.player()));
		}
		else
		{
			PayloadTypeRegistry.playS2C().register(info.type(), info.codec());
			ClientPlayNetworking.registerGlobalReceiver(info.type(), (payload, context) ->
					payload.handle(context.player()));
		}
	}
}
