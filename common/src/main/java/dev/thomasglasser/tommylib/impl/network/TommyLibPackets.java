package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.PacketInfo;

import java.util.ArrayList;
import java.util.List;

public class TommyLibPackets
{
	public static List<PacketInfo<?>> PACKETS = new ArrayList<>();

	public static void init()
	{
		// Serverbound
		PACKETS.add(new PacketInfo<>(ServerboundRequestDataSyncPacketPayload.TYPE, ExtendedPacketPayload.Direction.CLIENT_TO_SERVER, ServerboundRequestDataSyncPacketPayload.CODEC));

		// Clientbound
		PACKETS.add(new PacketInfo<>(ClientboundSyncDataPacketPayload.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundSyncDataPacketPayload.CODEC));
	}
}
