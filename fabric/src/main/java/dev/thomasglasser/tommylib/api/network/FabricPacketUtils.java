package dev.thomasglasser.tommylib.api.network;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.player.Player;

public class FabricPacketUtils
{
	public static void register(Class<? extends CustomPacket> packet, Pair<ResourceLocation, CustomPacket.Direction> pair)
	{
		if (pair.getSecond() == CustomPacket.Direction.CLIENT_TO_SERVER)
		{
			ServerPlayNetworking.registerGlobalReceiver(pair.getFirst(), (server, player, handler, buf, responseSender) ->
					handlePacket(server, packet, buf, player));
		}
		else
		{
			ClientPlayNetworking.registerGlobalReceiver(pair.getFirst(), (client, handler, buf, responseSender) ->
					handlePacket(client, packet, buf, ClientUtils.getMainClientPlayer()));
		}
	}

	private static void handlePacket(BlockableEventLoop<?> handler, Class<? extends CustomPacket> packet, FriendlyByteBuf buf, Player player)
	{
		buf.retain();
		handler.execute(() ->
		{
			try
			{
				packet.getConstructor(FriendlyByteBuf.class).newInstance(buf).handle(player);
			}
			catch (NoSuchMethodException e)
			{
				try
				{
					packet.getConstructor().newInstance().handle(player);
				}
				catch (NoSuchMethodException nsm)
				{
					throw new RuntimeException("Custom packets must have FriendlyByteBuf or empty constructor!");
				}
				catch (Exception ex)
				{
					throw new RuntimeException(e);
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			finally
			{
				buf.release();
			}
		});
	}
}
