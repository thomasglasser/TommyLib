package dev.thomasglasser.tommylib.api.world.level.block.grower;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import java.util.SortedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public class StructureGrower {
    private static final Map<String, StructureGrower> GROWERS = new Object2ObjectArrayMap<>();
    public static final Codec<StructureGrower> CODEC = Codec.stringResolver(grower -> grower.name, GROWERS::get);

    private final String name;
    private final SortedMap<StructureGenerationContext, ResourceKey<Structure>> structures;

    public StructureGrower(String name, SortedMap<StructureGenerationContext, ResourceKey<Structure>> structures) {
        this.name = name;
        this.structures = structures;
        GROWERS.put(name, this);
    }

    public boolean grow(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random) {
        for (Map.Entry<StructureGenerationContext, ResourceKey<Structure>> entry : structures.entrySet()) {
            if (entry.getKey().canGenerate(random)) {
                RegistryAccess registryAccess = level.registryAccess();
                Structure structure = registryAccess.registryOrThrow(Registries.STRUCTURE).get(entry.getValue());
                StructureStart structureStart = structure.generate(
                        registryAccess, chunkGenerator,
                        chunkGenerator.getBiomeSource(),
                        level.getChunkSource().randomState(),
                        level.getStructureManager(), level.getSeed(),
                        new ChunkPos(pos), 0, level, biome -> true);
                if (structureStart.isValid()) {
                    BoundingBox boundingbox = structureStart.getBoundingBox();
                    ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
                    ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
                    ChunkPos.rangeClosed(chunkpos, chunkpos1)
                            .forEach(
                                    chunkPos -> structureStart.placeInChunk(level,
                                            level.structureManager(), chunkGenerator,
                                            level.getRandom(),
                                            new BoundingBox(
                                                    chunkPos.getMinBlockX(),
                                                    level.getMinBuildHeight(),
                                                    chunkPos.getMinBlockZ(),
                                                    chunkPos.getMaxBlockX(),
                                                    level.getMaxBuildHeight(),
                                                    chunkPos.getMaxBlockZ()),
                                            chunkPos));
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    @FunctionalInterface
    public interface StructureGenerationContext {
        boolean canGenerate(RandomSource random);
    }
}
