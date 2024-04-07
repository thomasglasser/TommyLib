package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.network.ServerboundRequestDataSyncPacket;
import net.minecraft.world.entity.Entity;

public class TommyLibClientEvents
{
	public static void onEntityJoinLevel(Entity entity)
	{
		TommyLibServices.NETWORK.sendToServer(ServerboundRequestDataSyncPacket.ID, ServerboundRequestDataSyncPacket::new, ServerboundRequestDataSyncPacket.write(entity.getId()));
	}
}
