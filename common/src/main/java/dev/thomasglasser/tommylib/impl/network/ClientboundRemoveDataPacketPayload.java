package dev.thomasglasser.tommylib.impl.network;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.network.ExtendedPacketPayload;
import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record ClientboundRemoveDataPacketPayload(List<String> tags, int entity) implements ExtendedPacketPayload {
    public static final Type<ClientboundRemoveDataPacketPayload> TYPE = new Type<>(TommyLib.modLoc("clientbound_remove_data"));
    public static final StreamCodec<ByteBuf, ClientboundRemoveDataPacketPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), ClientboundRemoveDataPacketPayload::tags,
            ByteBufCodecs.INT, ClientboundRemoveDataPacketPayload::entity,
            ClientboundRemoveDataPacketPayload::new);

    // ON CLIENT
    @Override
    public void handle(Player player) {
        Entity target = player.level().getEntity(entity);
        if (target != null) {
            CompoundTag data = TommyLibServices.ENTITY.getPersistentData(target);
            for (String tag : tags) {
                data.remove(tag);
            }
            TommyLibServices.ENTITY.setPersistentData(target, data, false);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
