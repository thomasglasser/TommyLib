package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;
import java.util.List;

public class TommyLibPayloads {
    public static List<PayloadInfo<?>> PAYLOADS = List.of(
            // Serverbound
            new PayloadInfo<>(ServerboundRequestDataSyncPacketPayload.TYPE, ExtendedPacketPayload.Direction.CLIENT_TO_SERVER, ServerboundRequestDataSyncPacketPayload.CODEC),

            // Clientbound
            new PayloadInfo<>(ClientboundSyncDataPacketPayload.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundSyncDataPacketPayload.CODEC),
            new PayloadInfo<>(ClientboundMergeDataPacketPayload.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundMergeDataPacketPayload.CODEC),
            new PayloadInfo<>(ClientboundRemoveDataPacketPayload.TYPE, ExtendedPacketPayload.Direction.SERVER_TO_CLIENT, ClientboundRemoveDataPacketPayload.CODEC));
}
