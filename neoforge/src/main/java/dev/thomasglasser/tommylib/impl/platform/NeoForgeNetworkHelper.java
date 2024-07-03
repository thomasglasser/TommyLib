package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.impl.platform.services.NetworkHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgeNetworkHelper implements NetworkHelper {
    @Override
    public void sendToServer(ExtendedPacketPayload extendedPacketPayload) {
        PacketDistributor.sendToServer(extendedPacketPayload);
    }

    @Override
    public void sendToClient(ExtendedPacketPayload extendedPacketPayload, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, extendedPacketPayload);
    }

    @Override
    public void sendToAllClients(ExtendedPacketPayload extendedPacketPayload, MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(extendedPacketPayload);
    }

    @Override
    public void sendToTrackingClients(ExtendedPacketPayload extendedPacketPayload, MinecraftServer server, Entity tracked) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(tracked, extendedPacketPayload);
    }
}
