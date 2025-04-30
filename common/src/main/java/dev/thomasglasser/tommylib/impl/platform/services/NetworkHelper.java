package dev.thomasglasser.tommylib.impl.platform.services;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * Helpers for sending payloads.
 */
public interface NetworkHelper {
    /**
     * Sends a payload from the client to the server.
     *
     * @param payload the payload to send
     */
    void sendToServer(ExtendedPacketPayload payload);

    /**
     * Sends a payload to a specific client.
     *
     * @param payload the payload to send
     * @param player  the client to send the payload to
     */
    void sendToClient(ExtendedPacketPayload payload, ServerPlayer player);

    /**
     * Sends a payload to all clients on the server.
     *
     * @param payload the payload to send
     * @param server  the server to send the payload from
     */
    void sendToAllClients(ExtendedPacketPayload payload, MinecraftServer server);

    /**
     * Sends a payload to all clients tracking the provided entity.
     *
     * @param payload the payload to send
     * @param tracked the entity being tracked
     */
    void sendToTrackingClients(ExtendedPacketPayload payload, Entity tracked);

    /**
     * Sends a payload to all clients tracking the provided entity, including the entity itself.
     *
     * @param payload the payload to send
     * @param tracked the entity being tracked
     */
    void sendToTrackingClientsAndSelf(ExtendedPacketPayload payload, Entity tracked);
}
