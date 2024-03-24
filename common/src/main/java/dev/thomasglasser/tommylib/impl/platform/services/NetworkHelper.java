package dev.thomasglasser.tommylib.impl.platform.services;

import dev.thomasglasser.tommylib.api.network.CustomPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface NetworkHelper
{
    <MSG extends CustomPacket> void sendToServer(Class<MSG> msgClass, FriendlyByteBuf args);
    <MSG extends CustomPacket> void sendToServer(Class<MSG> msgClass);
    <MSG extends CustomPacket> void sendToClient(Class<MSG> msgClass, FriendlyByteBuf args, ServerPlayer player);
    <MSG extends CustomPacket> void sendToClient(Class<MSG> msgClass, ServerPlayer player);
    <MSG extends CustomPacket> void sendToAllClients(Class<MSG> msgClass, FriendlyByteBuf args, MinecraftServer server);
    <MSG extends CustomPacket> void sendToAllClients(Class<MSG> msgClass, MinecraftServer server);
    <MSG extends CustomPacket> void sendToTrackingClients(Class<MSG> msgClass, FriendlyByteBuf args, MinecraftServer server, Entity tracked);
    <MSG extends CustomPacket> void sendToTrackingClients(Class<MSG> msgClass, MinecraftServer server, Entity tracked);
}
