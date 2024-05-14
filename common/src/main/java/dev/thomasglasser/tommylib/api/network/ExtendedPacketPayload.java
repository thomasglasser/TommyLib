package dev.thomasglasser.tommylib.api.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

/**
 * An extended version of {@link CustomPacketPayload} that adds a handler method.
 */
public interface ExtendedPacketPayload extends CustomPacketPayload
{
	void handle(Player player);

	/**
	 * The direction the packet should be sent in.
	 */
	enum Direction
	{
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
