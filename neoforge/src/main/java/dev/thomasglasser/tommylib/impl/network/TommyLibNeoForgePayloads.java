package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.BidirectionalSyncDataAttachmentPayload;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.network.NeoForgeNetworkUtils;
import dev.thomasglasser.tommylib.api.network.PayloadInfo;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class TommyLibNeoForgePayloads {
    public static Set<PayloadInfo<?>> PAYLOADS = ReferenceOpenHashSet.of(
            // Common
            new PayloadInfo<>(BidirectionalSyncDataAttachmentPayload.TYPE, ExtendedPacketPayload.Direction.BIDIRECTIONAL, BidirectionalSyncDataAttachmentPayload.CODEC));

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(TommyLib.MOD_ID);
        TommyLibPayloads.PAYLOADS.forEach((info) -> NeoForgeNetworkUtils.register(registrar, info));
        PAYLOADS.forEach((info) -> NeoForgeNetworkUtils.register(registrar, info));
    }
}
