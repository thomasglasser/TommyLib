package dev.thomasglasser.tommylib.api.network;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class FabricPacketUtils
{
	public static void register(Function<FriendlyByteBuf, ? extends CustomPacket> packet, Pair<ResourceLocation, CustomPacket.Direction> pair)
	{
		if (pair.getSecond() == CustomPacket.Direction.CLIENT_TO_SERVER)
		{
			ServerPlayNetworking.registerGlobalReceiver(pair.getFirst(), (server, player, handler, buf, responseSender) ->
					handlePacket(server, packet.apply(buf), buf, player));
		}
		else
		{
			ClientPlayNetworking.registerGlobalReceiver(pair.getFirst(), (client, handler, buf, responseSender) ->
					handlePacket(client, packet.apply(buf), buf, ClientUtils.getMainClientPlayer()));
		}
	}

	private static void handlePacket(BlockableEventLoop<?> handler, CustomPacket packet, FriendlyByteBuf buf, Player player)
	{
		buf.retain();
		handler.execute(() ->
		{
			packet.handle(player);
			buf.release();
		});
	}
}
