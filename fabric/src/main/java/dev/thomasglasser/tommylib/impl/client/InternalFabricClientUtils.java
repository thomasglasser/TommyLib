package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class InternalFabricClientUtils
{
	public static <T extends ExtendedPacketPayload> void registerClientReceiver(PayloadInfo<T> info)
	{
		if (info.direction() == ExtendedPacketPayload.Direction.SERVER_TO_CLIENT)
		{
			ClientPlayNetworking.registerGlobalReceiver(info.type(), (payload, context) ->
					payload.handle(context.player()));
		}
	}
}
