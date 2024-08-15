package dev.thomasglasser.tommylib.api.world.level.levelgen.feature;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

public class TommyLibFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, TommyLib.MOD_ID);

    /**
     * A feature that allows for placing NBT structures.
     */
    public static final DeferredHolder<Feature<?>, NbtFeature> NBT_FEATURE = FEATURES.register("nbt_feature", NbtFeature::new);

    public static void init() {}
}
