package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.impl.platform.services.NetworkHelper;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class FabricNetworkHelper implements NetworkHelper {
    @Override
    public void sendToServer(ExtendedPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    public void sendToClient(ExtendedPacketPayload payload, ServerPlayer player) {
        ServerPlayNetworking.send(player, payload);
    }

    @Override
    public void sendToAllClients(ExtendedPacketPayload payload, MinecraftServer server) {
        for (ServerPlayer player : PlayerLookup.all(server)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    @Override
    public void sendToTrackingClients(ExtendedPacketPayload payload, Entity tracked) {
        for (ServerPlayer player : PlayerLookup.tracking(tracked)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    @Override
    public void sendToTrackingClientsAndSelf(ExtendedPacketPayload payload, Entity tracked) {
        ReferenceOpenHashSet<ServerPlayer> tracking = new ReferenceOpenHashSet<>(PlayerLookup.tracking(tracked));
        if (tracked instanceof ServerPlayer serverPlayer) {
            tracking.add(serverPlayer);
        }
        for (ServerPlayer player : tracking) {
            ServerPlayNetworking.send(player, payload);
        }
    }
}
