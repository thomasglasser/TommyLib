package dev.thomasglasser.tommylib.impl.platform.services;

import dev.thomasglasser.tommylib.api.platform.TommyLibServices;
import dev.thomasglasser.tommylib.impl.network.ClientboundMergeDataPacketPayload;
import dev.thomasglasser.tommylib.impl.network.ClientboundRemoveDataPacketPayload;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;

public interface EntityHelper {
    default void registerDataSerializers(String modId, Map<String, EntityDataSerializer<?>> serializers) {
        serializers.forEach((rl, serializer) -> EntityDataSerializers.registerSerializer(serializer));
    }

    CompoundTag getPersistentData(Entity entity);

    void setPersistentData(Entity entity, CompoundTag data, boolean syncToClient);

    default void mergePersistentData(Entity entity, CompoundTag data, boolean syncToClient) {
        if (syncToClient) TommyLibServices.NETWORK.sendToAllClients(new ClientboundMergeDataPacketPayload(data, entity.getId()), entity.level().getServer());
        setPersistentData(entity, getPersistentData(entity).merge(data), false);
    }

    default void removePersistentData(Entity entity, boolean syncToClient, String... tags) {
        CompoundTag data = getPersistentData(entity);
        for (String tag : tags) {
            data.remove(tag);
        }
        setPersistentData(entity, data, false);
        if (syncToClient) TommyLibServices.NETWORK.sendToAllClients(new ClientboundRemoveDataPacketPayload(List.of(tags), entity.getId()), entity.level().getServer());
    }
}
