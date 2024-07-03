package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.network.ServerboundRequestDataSyncPacketPayload;
import net.minecraft.world.entity.Entity;

public class TommyLibClientEvents {
    public static void onEntityJoinLevel(Entity entity) {
        TommyLibServices.NETWORK.sendToServer(new ServerboundRequestDataSyncPacketPayload(entity.getId()));
    }
}
