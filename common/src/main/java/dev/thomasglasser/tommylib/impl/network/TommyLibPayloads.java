package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.api.network.BidirectionalSyncDataComponentPayload;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;

public class TommyLibPayloads {
    public static Set<PayloadInfo<?>> PAYLOADS = ReferenceOpenHashSet.of(
            // Common
            new PayloadInfo<>(BidirectionalSyncDataComponentPayload.TYPE, ExtendedPacketPayload.Direction.BIDIRECTIONAL, BidirectionalSyncDataComponentPayload.CODEC));
}
