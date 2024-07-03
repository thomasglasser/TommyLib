package dev.thomasglasser.tommylib.api.network;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgeNetworkUtils {
    /**
     * Registers a packet payload with the given registrar.
     *
     * @param registrar The registrar to register the payload with.
     * @param info      The payload info to register.
     * @param <T>       The type of the payload.
     */
    public static <T extends ExtendedPacketPayload> void register(PayloadRegistrar registrar, PayloadInfo<T> info) {
        if (info.direction() == ExtendedPacketPayload.Direction.CLIENT_TO_SERVER) {
            registrar.playToServer(info.type(), info.codec(), ((payload, context) -> payload.handle(context.player())));
        } else {
            registrar.playToClient(info.type(), info.codec(), ((payload, context) -> payload.handle(context.player())));
        }
    }
}
