package dev.thomasglasser.tommylib.api.network;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.client.InternalFabricClientUtils;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricNetworkUtils {
    /**
     * Register a packet payload for common handling and client-side receiving.
     * 
     * @param info The payload info.
     * @param <T>  The payload type.
     */
    public static <T extends ExtendedPacketPayload> void register(PayloadInfo<T> info) {
        registerCommon(info);
        if (TommyLibServices.PLATFORM.isClientSide())
            InternalFabricClientUtils.registerClientReceiver(info);
    }

    /**
     * Register a packet payload for common handling.
     * 
     * @param info The payload info.
     * @param <T>  The payload type.
     */
    protected static <T extends ExtendedPacketPayload> void registerCommon(PayloadInfo<T> info) {
        if (info.direction() == ExtendedPacketPayload.Direction.CLIENT_TO_SERVER) {
            PayloadTypeRegistry.playC2S().register(info.type(), info.codec());
            ServerPlayNetworking.registerGlobalReceiver(info.type(), (payload, context) -> payload.handle(context.player()));
        } else {
            PayloadTypeRegistry.playS2C().register(info.type(), info.codec());
        }
    }
}
