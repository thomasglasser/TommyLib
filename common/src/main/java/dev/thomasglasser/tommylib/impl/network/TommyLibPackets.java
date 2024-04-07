package dev.thomasglasser.tommylib.impl.network;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.api.network.CustomPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TommyLibPackets
{
	public static Map<Function<FriendlyByteBuf, ? extends CustomPacket>, Pair<ResourceLocation, CustomPacket.Direction>> PACKETS = new HashMap<>();

	public static void init()
	{
		// Serverbound
		PACKETS.put(ServerboundRequestDataSyncPacket::new, Pair.of(ServerboundRequestDataSyncPacket.ID, CustomPacket.Direction.CLIENT_TO_SERVER));

		// Clientbound
		PACKETS.put(ClientboundSyncDataPacket::new, Pair.of(ClientboundSyncDataPacket.ID, CustomPacket.Direction.SERVER_TO_CLIENT));
	}
}
