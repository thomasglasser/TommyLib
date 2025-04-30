package dev.thomasglasser.tommylib.api.network;

import dev.thomasglasser.tommylib.TommyLib;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record BidirectionalSyncDataComponentPayload<T>(InteractionHand hand, Holder<DataComponentType<?>> componentType, T value) implements ExtendedPacketPayload {

    public static final Type<BidirectionalSyncDataComponentPayload<?>> TYPE = new Type<>(TommyLib.modLoc("bidirectional_sync_data_component"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BidirectionalSyncDataComponentPayload<?>> CODEC = StreamCodec.of(BidirectionalSyncDataComponentPayload::encode, BidirectionalSyncDataComponentPayload::decode);

    private static final StreamCodec<RegistryFriendlyByteBuf, Holder<DataComponentType<?>>> DATA_COMPONENT_CODEC = ByteBufCodecs.holderRegistry(Registries.DATA_COMPONENT_TYPE);
    @Override
    public void handle(Player player) {
        ItemStack stack = player.getItemInHand(hand);
        DataComponentType<T> type = (DataComponentType<T>) componentType.value();
        stack.set(type, value);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static <T> void encode(RegistryFriendlyByteBuf buf, BidirectionalSyncDataComponentPayload<T> payload) {
        buf.writeEnum(payload.hand);
        DATA_COMPONENT_CODEC.encode(buf, payload.componentType);
        StreamCodec<? super RegistryFriendlyByteBuf, T> codec = (StreamCodec<? super RegistryFriendlyByteBuf, T>) payload.componentType.value().streamCodec();
        codec.encode(buf, payload.value);
    }

    private static <T> BidirectionalSyncDataComponentPayload<T> decode(RegistryFriendlyByteBuf buf) {
        InteractionHand hand = buf.readEnum(InteractionHand.class);
        Holder<DataComponentType<?>> holder = DATA_COMPONENT_CODEC.decode(buf);
        StreamCodec<? super RegistryFriendlyByteBuf, T> codec = (StreamCodec<? super RegistryFriendlyByteBuf, T>) holder.value().streamCodec();
        T value = codec.decode(buf);
        return new BidirectionalSyncDataComponentPayload<>(hand, holder, value);
    }
}
