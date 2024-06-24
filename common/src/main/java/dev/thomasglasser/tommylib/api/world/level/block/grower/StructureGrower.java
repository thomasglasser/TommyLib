package dev.thomasglasser.tommylib.api.world.level.block.grower;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
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

import java.util.Map;
import java.util.SortedMap;

public class StructureGrower
{
	private static final Map<String, StructureGrower> GROWERS = new Object2ObjectArrayMap<>();
	public static final Codec<StructureGrower> CODEC = Codec.stringResolver(p_304625_ -> p_304625_.name, GROWERS::get);

	private final String name;
	private final SortedMap<StructureGenerationContext, ResourceKey<Structure>> structures;

	public StructureGrower(String name, SortedMap<StructureGenerationContext, ResourceKey<Structure>> structures)
	{
		this.name = name;
		this.structures = structures;
		GROWERS.put(name, this);
	}

	public boolean grow(ServerLevel pLevel, ChunkGenerator pChunkGenerator, BlockPos pPos, BlockState pState, RandomSource pRandom)
	{
		for (Map.Entry<StructureGenerationContext, ResourceKey<Structure>> entry : structures.entrySet())
		{
			if (entry.getKey().canGenerate(pRandom))
			{
				RegistryAccess registryAccess = pLevel.registryAccess();
				Structure structure = registryAccess.registryOrThrow(Registries.STRUCTURE).get(entry.getValue());
				StructureStart structureStart = structure.generate(
						registryAccess, pChunkGenerator, 
						pChunkGenerator.getBiomeSource(), 
						pLevel.getChunkSource().randomState(), 
						pLevel.getStructureManager(), pLevel.getSeed(), 
						new ChunkPos(pPos), 0, 
						pLevel, biome -> true);
				if (structureStart.isValid())
				{
					BoundingBox boundingbox = structureStart.getBoundingBox();
					ChunkPos chunkpos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
					ChunkPos chunkpos1 = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
					ChunkPos.rangeClosed(chunkpos, chunkpos1)
							.forEach(
									p_340665_ -> structureStart.placeInChunk(
											pLevel,
											pLevel.structureManager(),
											pChunkGenerator,
											pLevel.getRandom(),
											new BoundingBox(
													p_340665_.getMinBlockX(),
													pLevel.getMinBuildHeight(),
													p_340665_.getMinBlockZ(),
													p_340665_.getMaxBlockX(),
													pLevel.getMaxBuildHeight(),
													p_340665_.getMaxBlockZ()
											),
											p_340665_
									)
							);
					return true;
				}
				else
				{
					return false;
				}
			}
			return false;
		}
		return false;
	}

	@FunctionalInterface
	public interface StructureGenerationContext
	{
		boolean canGenerate(RandomSource random);
	}
}
