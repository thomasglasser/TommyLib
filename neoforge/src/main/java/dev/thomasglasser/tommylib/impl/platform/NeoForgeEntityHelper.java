package dev.thomasglasser.tommylib.impl.platform;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.impl.platform.services.EntityHelper;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NeoForgeEntityHelper implements EntityHelper {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TommyLib.MOD_ID);
    private static final DeferredHolder<AttachmentType<?>, AttachmentType<CompoundTag>> DATA = ATTACHMENT_TYPES.register("data", () -> AttachmentType.builder(CompoundTag::new).serialize(CompoundTag.CODEC).build());

    private final Map<String, DeferredRegister<EntityDataSerializer<?>>> DATA_SERIALIZERS = new HashMap<>();

    @Override
    public void registerDataSerializers(String modId, Map<String, EntityDataSerializer<?>> serializers) {
        DeferredRegister<EntityDataSerializer<?>> register = DATA_SERIALIZERS.computeIfAbsent(modId, id -> {
            DeferredRegister<EntityDataSerializer<?>> reg = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, id);
            reg.register(ModLoadingContext.get().getActiveContainer().getEventBus());
            return reg;
        });
        serializers.forEach((name, serializer) -> register.register(name, () -> serializer));
    }
}
