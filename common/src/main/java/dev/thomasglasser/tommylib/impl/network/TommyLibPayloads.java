package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;

import java.util.ArrayList;
import java.util.List;

public class TommyLibPayloads
{
	public static List<PayloadInfo<?>> PAYLOADS = new ArrayList<>();

	public static void init()
	{
		// Serverbound
		PAYLOADS.add(new PayloadInfo<>(ServerboundRequestDataSyncPacketPayload.TYPE, ExtendedPacketPayload.Direction.CLIENT_TO_SERVER, ServerboundRequestDataSyncPacketPayload.CODEC));

		// Clientbound
		PAYLOADS.add(new PayloadInfo<>(ClientboundSyncDataPacketPayload.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundSyncDataPacketPayload.CODEC));
	}
}
