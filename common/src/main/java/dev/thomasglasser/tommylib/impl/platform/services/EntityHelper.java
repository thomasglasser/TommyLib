package dev.thomasglasser.tommylib.impl.platform.services;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;

import java.util.Map;

public interface EntityHelper
{
	default void registerDataSerializers(String modId, Map<String, EntityDataSerializer<?>> serializers)
	{
		serializers.forEach((rl, serializer) -> EntityDataSerializers.registerSerializer(serializer));
	}

	CompoundTag getPersistentData(Entity entity);
	void setPersistentData(Entity entity, CompoundTag data, boolean syncToClient);
}
