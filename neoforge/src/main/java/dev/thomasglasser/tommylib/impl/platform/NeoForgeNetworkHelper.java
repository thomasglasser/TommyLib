package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.impl.platform.services.NetworkHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgeNetworkHelper implements NetworkHelper {
    @Override
    public void sendToServer(ExtendedPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendToClient(ExtendedPacketPayload payload, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void sendToAllClients(ExtendedPacketPayload payload, MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(payload);
    }

    @Override
    public void sendToTrackingClients(ExtendedPacketPayload payload, Entity tracked) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(tracked, payload);
    }

    @Override
    public void sendToTrackingClientsAndSelf(ExtendedPacketPayload payload, Entity tracked) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(tracked, payload);
    }
}
