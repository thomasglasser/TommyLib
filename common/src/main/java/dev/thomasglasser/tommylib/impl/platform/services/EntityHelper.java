package dev.thomasglasser.tommylib.impl.platform.services;

import java.util.Map;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public interface EntityHelper {
    default void registerDataSerializers(String modId, Map<String, EntityDataSerializer<?>> serializers) {
        serializers.forEach((rl, serializer) -> EntityDataSerializers.registerSerializer(serializer));
    }
}
