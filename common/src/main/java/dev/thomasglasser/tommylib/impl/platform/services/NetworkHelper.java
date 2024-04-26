package dev.thomasglasser.tommylib.impl.platform.services;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface NetworkHelper
{
    void sendToServer(ExtendedPacketPayload extendedPacketPayload);
    void sendToClient(ExtendedPacketPayload extendedPacketPayload, ServerPlayer player);
    void sendToAllClients(ExtendedPacketPayload extendedPacketPayload, MinecraftServer server);
    void sendToTrackingClients(ExtendedPacketPayload extendedPacketPayload, MinecraftServer server, Entity tracked);
}
