package dev.thomasglasser.tommylib.api.network;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgeNetworkUtils {
    private static final Map<ResourceKey<AttachmentType<?>>, StreamCodec<? super RegistryFriendlyByteBuf, ?>> SYNCED_TYPE_CODECS = new Reference2ObjectOpenHashMap<>();

    /**
     * Registers a packet payload with the given registrar.
     *
     * @param registrar The registrar to register the payload with.
     * @param info      The payload info to register.
     * @param <T>       The type of the payload.
     */
    public static <T extends ExtendedPacketPayload> void register(PayloadRegistrar registrar, PayloadInfo<T> info) {
        switch (info.direction()) {
            case BIDIRECTIONAL -> registrar.playBidirectional(info.type(), info.codec(), ((payload, context) -> payload.handle(context.player())));
            case SERVER_TO_CLIENT -> registrar.playToClient(info.type(), info.codec(), ((payload, context) -> payload.handle(context.player())));
            case CLIENT_TO_SERVER -> registrar.playToServer(info.type(), info.codec(), ((payload, context) -> payload.handle(context.player())));
        }
    }

    public static <T> void registerSyncedAttachment(ResourceKey<AttachmentType<?>> key, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        if (SYNCED_TYPE_CODECS.containsKey(key)) {
            throw new IllegalArgumentException("Synced attachment type " + key + " already registered!");
        }

        SYNCED_TYPE_CODECS.put(key, codec);
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, T> getSyncedAttachmentCodec(ResourceKey<AttachmentType<?>> key) {
        StreamCodec<RegistryFriendlyByteBuf, T> codec = (StreamCodec<RegistryFriendlyByteBuf, T>) SYNCED_TYPE_CODECS.get(key);
        if (codec == null) {
            throw new IllegalArgumentException("Synced attachment type " + key.location() + " is not registered on the " + (TommyLibServices.PLATFORM.isClientSide() ? "client" : "server") + " side! Did you register it on both sides with NeoForgeNetworkUtils.registerSyncedAttachment()?");
        }
        return codec;
    }
}
