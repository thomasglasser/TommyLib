package dev.thomasglasser.tommylib.api.network;

import dev.thomasglasser.tommylib.TommyLib;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

// TODO: Check
public record BidirectionalSyncDataAttachmentPayload<T>(int entityId, Holder<AttachmentType<?>> attachmentType, Optional<T> value) implements ExtendedPacketPayload {

    public static final Type<BidirectionalSyncDataAttachmentPayload<?>> TYPE = new Type<>(TommyLib.modLoc("bidirectional_sync_data_attachment"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BidirectionalSyncDataAttachmentPayload<?>> CODEC = StreamCodec.of(BidirectionalSyncDataAttachmentPayload::encode, BidirectionalSyncDataAttachmentPayload::decode);

    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<AttachmentType<?>>> DATA_ATTACHMENT_CODEC = ByteBufCodecs.holderRegistry(NeoForgeRegistries.Keys.ATTACHMENT_TYPES);
    @Override
    public void handle(Player player) {
        Entity entity = player.level().getEntity(entityId);
        if (entity != null) {
            AttachmentType<T> type = (AttachmentType<T>) attachmentType.value();
            value.ifPresentOrElse(v -> entity.setData(type, v), () -> entity.removeData(type));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static <T> void encode(RegistryFriendlyByteBuf buf, BidirectionalSyncDataAttachmentPayload<T> payload) {
        buf.writeInt(payload.entityId);
        DATA_ATTACHMENT_CODEC.encode(buf, payload.attachmentType);
        buf.writeOptional(payload.value, (friendlyByteBuf, value) -> NeoForgeNetworkUtils.getSyncedAttachmentCodec(payload.attachmentType.getKey()).encode(buf, value));
    }

    private static <T> BidirectionalSyncDataAttachmentPayload<T> decode(RegistryFriendlyByteBuf buf) {
        int entityId = buf.readInt();
        Holder<AttachmentType<?>> attachmentType = DATA_ATTACHMENT_CODEC.decode(buf);
        Supplier<StreamCodec<? super RegistryFriendlyByteBuf, T>> codec = () -> NeoForgeNetworkUtils.getSyncedAttachmentCodec(attachmentType.getKey());
        Optional<T> value = buf.readOptional(friendlyByteBuf -> codec.get().decode(buf));
        return new BidirectionalSyncDataAttachmentPayload<>(entityId, attachmentType, value);
    }
}
