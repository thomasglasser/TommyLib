package dev.thomasglasser.tommylib.impl.client;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class InternalFabricClientUtils {
    public static <T extends ExtendedPacketPayload> void registerClientReceiver(PayloadInfo<T> info) {
        if (info.direction() != ExtendedPacketPayload.Direction.CLIENT_TO_SERVER) {
            ClientPlayNetworking.registerGlobalReceiver(info.type(), (payload, context) -> payload.handle(context.player()));
        } else {
            throw new IllegalArgumentException("Can only register server to client receivers via FabricClientUtils.registerClientReceiver");
        }
    }
}
