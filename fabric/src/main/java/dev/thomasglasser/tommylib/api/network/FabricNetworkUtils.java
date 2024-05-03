package dev.thomasglasser.tommylib.api.network;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.client.InternalFabricClientUtils;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricNetworkUtils
{
	public static <T extends ExtendedPacketPayload> void register(PayloadInfo<T> info)
	{
		registerCommon(info);
		if (TommyLibServices.PLATFORM.isClientSide())
			InternalFabricClientUtils.registerClientReceiver(info);
	}

	protected static <T extends ExtendedPacketPayload> void registerCommon(PayloadInfo<T> info)
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
		}
	}
}
