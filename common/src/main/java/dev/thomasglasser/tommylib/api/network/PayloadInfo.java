package dev.thomasglasser.tommylib.api.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PayloadInfo<T extends ExtendedPacketPayload>(CustomPacketPayload.Type<T> type, ExtendedPacketPayload.Direction direction, StreamCodec<? super RegistryFriendlyByteBuf, T> codec)
{}