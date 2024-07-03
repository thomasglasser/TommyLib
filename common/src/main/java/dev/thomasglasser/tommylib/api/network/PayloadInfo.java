package dev.thomasglasser.tommylib.api.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Represents a payload that can be sent over the network.
 * 
 * @param type      The ID of the payload
 * @param direction The direction the payload is sent
 * @param codec     The codec used to encode and decode the payload
 * @param <T>       The type of payload
 */
public record PayloadInfo<T extends ExtendedPacketPayload>(CustomPacketPayload.Type<T> type, ExtendedPacketPayload.Direction direction, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {}
