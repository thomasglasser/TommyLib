package dev.thomasglasser.tommylib.api.network;

import com.mojang.datafixers.util.Pair;
import dev.thomasglasser.tommylib.api.client.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.function.Function;

public class NeoForgePacketUtils
{
	public static void register(IPayloadRegistrar registrar, Function<FriendlyByteBuf, ? extends CustomPacket> packet, Pair<ResourceLocation, CustomPacket.Direction> pair)
	{

		registrar.play(pair.getFirst(), packet::apply, handler -> handler
				.client((payload, context) ->
				{
					if (payload.direction() == CustomPacket.Direction.SERVER_TO_CLIENT)
						context.workHandler().execute(() -> payload.handle(context.player().orElse(ClientUtils.getMainClientPlayer())));
					else throw new RuntimeException("Serverbound packet sent to client!");
				})
				.server((payload, context) ->
				{
					if (payload.direction() == CustomPacket.Direction.CLIENT_TO_SERVER)
						context.workHandler().execute(() -> payload.handle(context.player().orElse(null)));
					else throw new RuntimeException("Clientbound packet sent to server!");
				}));
	}
}
