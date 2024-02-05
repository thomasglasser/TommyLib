package dev.thomasglasser.tommylib.impl.platform.services;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

import java.util.Map;

public interface EntityHelper
{
	default void registerDataSerializers(String modId, Map<String, EntityDataSerializer<?>> serializers)
	{
		serializers.forEach((rl, serializer) -> EntityDataSerializers.registerSerializer(serializer));
	}
}
