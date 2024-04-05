package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.api.network.CustomPacket;
import dev.thomasglasser.tommylib.impl.platform.services.NetworkHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Function;

public class NeoForgeNetworkHelper implements NetworkHelper
{
	@Override
	public <MSG extends CustomPacket> void sendToServer(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf) {
		PacketDistributor.SERVER.noArg().send(packetFunction.apply(buf));
	}

	@Override
	public <MSG extends CustomPacket> void sendToClient(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, ServerPlayer player) {
		PacketDistributor.PLAYER.with(player).send(packetFunction.apply(buf));
	}

	@Override
	public <MSG extends CustomPacket> void sendToAllClients(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, MinecraftServer server) {
		PacketDistributor.ALL.noArg().send(packetFunction.apply(buf));
	}

	@Override
	public <MSG extends CustomPacket> void sendToTrackingClients(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, MinecraftServer server, Entity tracked)
	{
		PacketDistributor.TRACKING_ENTITY_AND_SELF.with(tracked).send(packetFunction.apply(buf));
	}
}
