package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.impl.platform.services.EntityHelper;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeEntityHelper implements EntityHelper
{
	private final Map<String, DeferredRegister<EntityDataSerializer<?>>> DATA_SERIALIZERS = new HashMap<>();

	@Override
	public void registerDataSerializers(String modId, Map<String, EntityDataSerializer<?>> serializers)
	{
		DeferredRegister<EntityDataSerializer<?>> register = DATA_SERIALIZERS.computeIfAbsent(modId, id ->
		{
			DeferredRegister<EntityDataSerializer<?>> reg = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, id);
			reg.register(FMLJavaModLoadingContext.get().getModEventBus());
			return reg;
		});
		serializers.forEach((name, serializer) -> register.register(name, () -> serializer));
	}
}
