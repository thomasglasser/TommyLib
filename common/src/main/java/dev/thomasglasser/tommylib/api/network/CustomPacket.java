package dev.thomasglasser.tommylib.api.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

// Don't forget the constructor with the FBB!
public interface CustomPacket extends CustomPacketPayload
{
	void handle(Player player);

	Direction direction();

	enum Direction
	{
		SERVER_TO_CLIENT,
		CLIENT_TO_SERVER
	}
}
