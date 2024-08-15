package dev.thomasglasser.tommylib.api.world.level.levelgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

/**
 * Represents a feature configuration that can be used to generate features with NBT data.
 * @param allowInWater Whether this feature can be placed in water.
 * @param canBreakExistingBlocks Whether this feature can break existing blocks.
 * @param heightOffset The height offset of this feature.
 * @param nbts The NBTs that will be generated.
 * @param processor The processor to be used on the feature.
 */
public record NbtFeatureConfig(boolean allowInWater, boolean canBreakExistingBlocks, int heightOffset,
        List<Pair<ResourceLocation, Integer>> nbts,
        ResourceKey<StructureProcessorList> processor) implements FeatureConfiguration {

    public static final Codec<NbtFeatureConfig> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            Codec.BOOL.fieldOf("allow_liquid").orElse(false).forGetter(NbtFeatureConfig::allowInWater),
            Codec.BOOL.fieldOf("can_break_existing_blocks").orElse(false).forGetter(NbtFeatureConfig::canBreakExistingBlocks),
            Codec.INT.fieldOf("height_offset").orElse(0).forGetter(NbtFeatureConfig::heightOffset),
            Codec.mapPair(ResourceLocation.CODEC.fieldOf("id"), Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight")).codec().listOf().fieldOf("nbt_entries").forGetter(NbtFeatureConfig::nbts),
            ResourceKey.codec(Registries.PROCESSOR_LIST).fieldOf("processors").orElse(null).forGetter(NbtFeatureConfig::processor)).apply(configInstance, NbtFeatureConfig::new));
}
