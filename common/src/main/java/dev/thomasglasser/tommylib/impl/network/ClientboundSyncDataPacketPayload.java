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

public record ClientboundSyncDataPacketPayload(CompoundTag compoundTag, int entity) implements ExtendedPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundSyncDataPacketPayload> TYPE = new CustomPacketPayload.Type<>(TommyLib.modLoc("clientbound_sync_data"));
    public static final StreamCodec<ByteBuf, ClientboundSyncDataPacketPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, ClientboundSyncDataPacketPayload::compoundTag,
            ByteBufCodecs.INT, ClientboundSyncDataPacketPayload::entity,
            ClientboundSyncDataPacketPayload::new);

    // ON CLIENT
    @Override
    public void handle(Player player) {
        Entity target = player.level().getEntity(entity);
        if (target != null)
            TommyLibServices.ENTITY.setPersistentData(target, compoundTag, false);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
