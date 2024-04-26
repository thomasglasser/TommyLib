package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.impl.platform.services.NetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;

public class FabricNetworkHelper implements NetworkHelper
{
    @Override
    public void sendToServer(ExtendedPacketPayload extendedPacketPayload) {
        ClientPlayNetworking.send(extendedPacketPayload);
    }

    @Override
    public void sendToClient(ExtendedPacketPayload extendedPacketPayload, ServerPlayer player) {
        ServerPlayNetworking.send(player, extendedPacketPayload);
    }

    @Override
    public void sendToAllClients(ExtendedPacketPayload extendedPacketPayload, MinecraftServer server) {
        for (ServerPlayer player : PlayerLookup.all(server))
        {
            ServerPlayNetworking.send(player, extendedPacketPayload);
        }
    }

    @Override
    public void sendToTrackingClients(ExtendedPacketPayload extendedPacketPayload, MinecraftServer server, Entity tracked)
    {
        ArrayList<ServerPlayer> tracking = new ArrayList<>(PlayerLookup.tracking(tracked));
        if (tracked instanceof ServerPlayer serverPlayer && !tracking.contains(serverPlayer)) tracking.add(serverPlayer);
        for (ServerPlayer player : tracking)
        {
            ServerPlayNetworking.send(player, extendedPacketPayload);
        }
    }
}
