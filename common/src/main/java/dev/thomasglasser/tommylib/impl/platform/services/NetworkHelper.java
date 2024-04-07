package dev.thomasglasser.tommylib.impl.platform.services;

import dev.thomasglasser.tommylib.api.network.CustomPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Supplier;

public interface NetworkHelper
{
    <MSG extends CustomPacket> void sendToServer(ResourceLocation id, Supplier<MSG> packetFunction);
    <MSG extends CustomPacket> void sendToServer(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf);
    <MSG extends CustomPacket> void sendToClient(ResourceLocation id, Supplier<MSG> packetFunction, ServerPlayer player);
    <MSG extends CustomPacket> void sendToClient(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, ServerPlayer player);
    <MSG extends CustomPacket> void sendToAllClients(ResourceLocation id, Supplier<MSG> packetFunction, MinecraftServer server);
    <MSG extends CustomPacket> void sendToAllClients(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, MinecraftServer server);
    <MSG extends CustomPacket> void sendToTrackingClients(ResourceLocation id, Supplier< MSG> packetFunction, MinecraftServer server, Entity tracked);
    <MSG extends CustomPacket> void sendToTrackingClients(ResourceLocation id, Function<FriendlyByteBuf, MSG> packetFunction, FriendlyByteBuf buf, MinecraftServer server, Entity tracked);
}
