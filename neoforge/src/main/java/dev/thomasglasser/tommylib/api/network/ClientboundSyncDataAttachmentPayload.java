package dev.thomasglasser.tommylib.api.network;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

public record ClientboundSyncDataAttachmentPayload<T>(int entityId, ResourceKey<AttachmentType<?>> attachmentKey, AttachmentType<T> attachmentType, Optional<T> value) implements ExtendedPacketPayload {

    public static final Type<ClientboundSyncDataAttachmentPayload<?>> TYPE = new Type<>(TommyLib.modLoc("clientbound_sync_data_attachment"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSyncDataAttachmentPayload<?>> CODEC = StreamCodec.of(ClientboundSyncDataAttachmentPayload::encode, ClientboundSyncDataAttachmentPayload::decode);

    private static final StreamCodec<ByteBuf, ResourceKey<AttachmentType<?>>> DATA_ATTACHMENT_CODEC = ResourceKey.streamCodec(NeoForgeRegistries.Keys.ATTACHMENT_TYPES);
    public ClientboundSyncDataAttachmentPayload(int entityId, ResourceKey<AttachmentType<?>> attachmentKey, AttachmentType<T> attachmentType, @Nullable T value) {
        this(entityId, attachmentKey, attachmentType, Optional.ofNullable(value));
    }

    public ClientboundSyncDataAttachmentPayload(int entityId, DeferredHolder<AttachmentType<?>, AttachmentType<T>> holder, Optional<T> value) {
        this(entityId, holder.getKey(), holder.get(), value);
    }

    public ClientboundSyncDataAttachmentPayload(int entityId, DeferredHolder<AttachmentType<?>, AttachmentType<T>> holder, @Nullable T value) {
        this(entityId, holder.getKey(), holder.get(), value);
    }

    @Override
    public void handle(Player player) {
        Entity entity = player.level().getEntity(entityId);
        if (entity != null) {
            value.ifPresentOrElse(v -> entity.setData(attachmentType, v), () -> entity.removeData(attachmentType));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static <T> void encode(RegistryFriendlyByteBuf buf, ClientboundSyncDataAttachmentPayload<T> payload) {
        buf.writeInt(payload.entityId);
        DATA_ATTACHMENT_CODEC.encode(buf, payload.attachmentKey);
        buf.writeOptional(payload.value, (friendlyByteBuf, value) -> NeoForgeNetworkUtils.getSyncedAttachmentCodec(payload.attachmentType).encode(buf, value));
    }

    private static <T> ClientboundSyncDataAttachmentPayload<T> decode(RegistryFriendlyByteBuf buf) {
        int entityId = buf.readInt();
        try {
            ResourceKey<AttachmentType<?>> attachmentKey = DATA_ATTACHMENT_CODEC.decode(buf);
            AttachmentType<T> attachmentType = (AttachmentType<T>) NeoForgeRegistries.ATTACHMENT_TYPES.get(attachmentKey);
            Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> codec = () -> NeoForgeNetworkUtils.getSyncedAttachmentCodec(attachmentType);
            Optional<T> value = buf.readOptional(friendlyByteBuf -> codec.get().decode(buf));
            return new ClientboundSyncDataAttachmentPayload<>(entityId, attachmentKey, attachmentType, value);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Provided data attachment type is not for the provided value", e);
        }
    }
}
