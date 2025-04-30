package dev.thomasglasser.tommylib.api.network;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * An extended version of {@link CustomPacketPayload} that adds a handler method.
 */
public interface ExtendedPacketPayload extends CustomPacketPayload {
    /**
     * Handles the packet on the side it is sent.
     * 
     * @param player The {@link ServerPlayer} who sent the payload on the server or the {@link LocalPlayer} who is receiving the packet on the client
     */
    void handle(Player player);

    /**
     * The direction the packet should be sent in.
     */
    enum Direction {
        /**
         * Can be sent from and to either side.
         */
        BIDIRECTIONAL,
        /**
         * Sent from the server to the client.
         */
        SERVER_TO_CLIENT,
        /**
         * Sent from the client to the server.
         */
        CLIENT_TO_SERVER
    }
}
