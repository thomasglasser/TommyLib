package dev.thomasglasser.tommylib.api.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public interface ExtendedPacketPayload extends CustomPacketPayload
{
	void handle(Player player);

	enum Direction
	{
		SERVER_TO_CLIENT,
		CLIENT_TO_SERVER
	}
}
