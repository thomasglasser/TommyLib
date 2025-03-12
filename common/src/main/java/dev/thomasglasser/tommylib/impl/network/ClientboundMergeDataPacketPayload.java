package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record ClientboundMergeDataPacketPayload(CompoundTag compoundTag, int entity) implements ExtendedPacketPayload {
    public static final Type<ClientboundMergeDataPacketPayload> TYPE = new Type<>(TommyLib.modLoc("clientbound_merge_data"));
    public static final StreamCodec<ByteBuf, ClientboundMergeDataPacketPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, ClientboundMergeDataPacketPayload::compoundTag,
            ByteBufCodecs.INT, ClientboundMergeDataPacketPayload::entity,
            ClientboundMergeDataPacketPayload::new);

    // ON CLIENT
    @Override
    public void handle(Player player) {
        Entity target = player.level().getEntity(entity);
        if (target != null)
            TommyLibServices.ENTITY.mergePersistentData(target, compoundTag, false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
