package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.api.network.CustomPacket;
import dev.thomasglasser.tommylib.impl.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.function.Function;

public class FabricNetworkHelper implements NetworkHelper
{
    @Override
    public <MSG extends CustomPacket> void sendToServer(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf) {
        ClientPlayNetworking.send(id, buf);
    }

    @Override
    public <MSG extends CustomPacket> void sendToClient(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, ServerPlayer player) {
        ServerPlayNetworking.send(player, id, buf);
    }

    @Override
    public <MSG extends CustomPacket> void sendToAllClients(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, MinecraftServer server) {
        for (ServerPlayer player : PlayerLookup.all(server))
        {
            ServerPlayNetworking.send(player, id, buf);
        }
    }

    @Override
    public <MSG extends CustomPacket> void sendToTrackingClients(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, MinecraftServer server, Entity tracked)
    {
        ArrayList<ServerPlayer> tracking = new ArrayList<>(PlayerLookup.tracking(tracked));
        if (tracked instanceof ServerPlayer serverPlayer && !tracking.contains(serverPlayer)) tracking.add(serverPlayer);
        for (ServerPlayer player : tracking)
        {
            ServerPlayNetworking.send(player, id, buf);
        }
    }
}
