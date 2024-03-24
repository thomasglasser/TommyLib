package dev.thomasglasser.tommylib.api.network;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class NeoForgePacketUtils
{
	public static void register(IPayloadRegistrar registrar, Class<? extends CustomPacket> packet, Pair<ResourceLocation, CustomPacket.Direction> pair)
	{
		registrar.play(pair.getFirst(), (FriendlyByteBuf buf) ->
		{
			try
			{
				return packet.getConstructor(FriendlyByteBuf.class).newInstance(buf);
			}
			catch (NoSuchMethodException e)
			{
				try
				{
					return packet.getConstructor().newInstance();
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
		}, handler -> handler
				.client((payload, context) ->
				{
					if (payload.direction() == CustomPacket.Direction.SERVER_TO_CLIENT) payload.handle(context.player().orElse(ClientUtils.getMainClientPlayer()));
					else throw new RuntimeException("Serverbound packet sent to client!");
				})
				.server((payload, context) ->
				{
					if (payload.direction() == CustomPacket.Direction.CLIENT_TO_SERVER) payload.handle(context.player().orElse(null));
					else throw new RuntimeException("Clientbound packet sent to server!");
				}));
	}
}
