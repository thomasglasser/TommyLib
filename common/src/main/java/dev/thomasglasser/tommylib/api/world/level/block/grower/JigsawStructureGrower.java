package dev.thomasglasser.tommylib.api.world.level.block.grower;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import dev.thomasglasser.tommylib.TommyLib;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class JigsawStructureGrower {
    private static final Map<String, JigsawStructureGrower> GROWERS = new Object2ObjectArrayMap<>();
    public static final Codec<JigsawStructureGrower> CODEC = Codec.stringResolver(grower -> grower.name, GROWERS::get);

    private final String name;
    private final StructureGenerationContext structure;

    public JigsawStructureGrower(String name, StructureGenerationContext structure) {
        this.name = name;
        this.structure = structure;
        GROWERS.put(name, this);
    }

    public boolean grow(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random) {
        ResourceKey<Structure> key = structure.getStructure(state, level, pos, random);
        if (key != null) {
            RegistryAccess registryAccess = level.registryAccess();
            Structure structure = registryAccess.registryOrThrow(Registries.STRUCTURE).get(key);
            if (structure == null) {
                TommyLib.LOGGER.error("Structure {} not found", key.location());
                return false;
            } else if (!(structure instanceof JigsawStructure)) {
                TommyLib.LOGGER.error("Structure {} is not a JigsawStructure", key.location());
                return false;
            }
            StructureStart structureStart = generate(
                    (JigsawStructure) structure,
                    registryAccess, chunkGenerator,
                    chunkGenerator.getBiomeSource(),
                    level.getChunkSource().randomState(),
                    level.getStructureManager(), level.getSeed(),
                    pos, 0, level, biome -> true);
            if (structureStart.isValid()) {
                BoundingBox boundingbox = structureStart.getBoundingBox();
                ChunkPos startChunkPos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.minX()), SectionPos.blockToSectionCoord(boundingbox.minZ()));
                ChunkPos endChunkPos = new ChunkPos(SectionPos.blockToSectionCoord(boundingbox.maxX()), SectionPos.blockToSectionCoord(boundingbox.maxZ()));
                ChunkPos.rangeClosed(startChunkPos, endChunkPos)
                        .forEach(
                                chunkPos -> structureStart.placeInChunk(
                                        level,
                                        level.structureManager(),
                                        chunkGenerator,
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

    private static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos, int xOffset, int yOffset) {
        Block block = state.getBlock();
        return level.getBlockState(pos.offset(xOffset, 0, yOffset)).is(block)
                && level.getBlockState(pos.offset(xOffset + 1, 0, yOffset)).is(block)
                && level.getBlockState(pos.offset(xOffset, 0, yOffset + 1)).is(block)
                && level.getBlockState(pos.offset(xOffset + 1, 0, yOffset + 1)).is(block);
    }

    public static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos) {
        for (int i = 0; i >= -1; i--) {
            for (int j = 0; j >= -1; j--) {
                if (isTwoByTwoSapling(state, level, pos, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isThreeByThreeSapling(BlockState state, BlockGetter level, BlockPos pos, int xOffset, int yOffset) {
        Block block = state.getBlock();
        return level.getBlockState(pos.offset(xOffset, 0, yOffset)).is(block)
                && level.getBlockState(pos.offset(xOffset + 1, 0, yOffset)).is(block)
                && level.getBlockState(pos.offset(xOffset + 2, 0, yOffset)).is(block)
                && level.getBlockState(pos.offset(xOffset, 0, yOffset + 1)).is(block)
                && level.getBlockState(pos.offset(xOffset + 1, 0, yOffset + 1)).is(block)
                && level.getBlockState(pos.offset(xOffset + 2, 0, yOffset + 1)).is(block)
                && level.getBlockState(pos.offset(xOffset, 0, yOffset + 2)).is(block)
                && level.getBlockState(pos.offset(xOffset + 1, 0, yOffset + 2)).is(block)
                && level.getBlockState(pos.offset(xOffset + 2, 0, yOffset + 2)).is(block);
    }

    public static boolean isThreeByThreeSapling(BlockState state, BlockGetter level, BlockPos pos) {
        for (int i = 0; i >= -2; i--) {
            for (int j = 0; j >= -2; j--) {
                if (isThreeByThreeSapling(state, level, pos, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private StructureStart generate(
            JigsawStructure structure,
            RegistryAccess registryAccess,
            ChunkGenerator chunkGenerator,
            BiomeSource biomeSource,
            RandomState randomState,
            StructureTemplateManager structureTemplateManager,
            long seed,
            BlockPos pos,
            int references,
            LevelHeightAccessor heightAccessor,
            Predicate<Holder<Biome>> validBiome) {
        ChunkPos chunkPos = new ChunkPos(pos);
        Structure.GenerationContext structure$generationcontext = new Structure.GenerationContext(
                registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, seed, chunkPos, heightAccessor, validBiome);
        Optional<Structure.GenerationStub> optional = findGenerationPoint(structure, pos, structure$generationcontext);
        if (optional.isPresent()) {
            PiecesContainer piecesContainer = optional.get().getPiecesBuilder().build();
            StructureStart structurestart = new StructureStart(structure, chunkPos, references, piecesContainer);
            if (structurestart.isValid()) {
                return structurestart;
            }
        }

        return StructureStart.INVALID_START;
    }

    private Optional<Structure.GenerationStub> findGenerationPoint(JigsawStructure structure, BlockPos blockPos, Structure.GenerationContext context) {
        int i = structure.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        return addPieces(context, structure.startPool, structure.startJigsawName, structure.maxDepth, blockPos.atY(i), structure.useExpansionHack, structure.projectStartToHeightmap, structure.maxDistanceFromCenter, PoolAliasLookup.create(structure.poolAliases, blockPos, context.seed()), structure.dimensionPadding, structure.liquidSettings);
    }

    private Optional<Structure.GenerationStub> addPieces(
            Structure.GenerationContext context,
            Holder<StructureTemplatePool> startPool,
            Optional<ResourceLocation> startJigsawName,
            int maxDepth,
            BlockPos pos,
            boolean useExpansionHack,
            Optional<Heightmap.Types> projectStartToHeightmap,
            int maxDistanceFromCenter,
            PoolAliasLookup aliasLookup,
            DimensionPadding dimensionPadding,
            LiquidSettings liquidSettings) {
        RegistryAccess registryaccess = context.registryAccess();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        StructureTemplateManager structuretemplatemanager = context.structureTemplateManager();
        LevelHeightAccessor levelheightaccessor = context.heightAccessor();
        WorldgenRandom worldgenrandom = context.random();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        StructureTemplatePool structuretemplatepool = startPool.unwrapKey()
                .flatMap(p_314915_ -> registry.getOptional(aliasLookup.lookup((ResourceKey<StructureTemplatePool>) p_314915_)))
                .orElse(startPool.value());
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            BlockPos blockpos;
            if (startJigsawName.isPresent()) {
                ResourceLocation resourcelocation = startJigsawName.get();
                Optional<BlockPos> optional = JigsawPlacement.getRandomNamedJigsaw(
                        structurepoolelement, resourcelocation, pos, rotation, structuretemplatemanager, worldgenrandom);
                if (optional.isEmpty()) {
                    TommyLib.LOGGER.error(
                            "No starting jigsaw {} found in start pool {}",
                            resourcelocation,
                            startPool.unwrapKey().map(p_248484_ -> p_248484_.location().toString()).orElse("<unregistered>"));
                    return Optional.empty();
                }

                blockpos = optional.get();
            } else {
                blockpos = pos;
            }

            Vec3i vec3i = blockpos.subtract(pos);
            BlockPos blockpos1 = pos.subtract(vec3i);
            PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(
                    structuretemplatemanager,
                    structurepoolelement,
                    blockpos1,
                    structurepoolelement.getGroundLevelDelta(),
                    rotation,
                    structurepoolelement.getBoundingBox(structuretemplatemanager, blockpos1, rotation),
                    liquidSettings);
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int k;
            k = projectStartToHeightmap.map(types -> pos.getY() + chunkgenerator.getFirstFreeHeight(i, j, types, levelheightaccessor, context.randomState())).orElseGet(blockpos1::getY);

            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            int i1 = k + vec3i.getY();
            return Optional.of(
                    new Structure.GenerationStub(
                            new BlockPos(i, i1, j),
                            p_352014_ -> {
                                List<PoolElementStructurePiece> list = Lists.newArrayList();
                                list.add(poolelementstructurepiece);
                                if (maxDepth > 0) {
                                    AABB aabb = new AABB(
                                            i - maxDistanceFromCenter,
                                            Math.max(i1 - maxDistanceFromCenter, levelheightaccessor.getMinBuildHeight() + dimensionPadding.bottom()),
                                            j - maxDistanceFromCenter,
                                            i + maxDistanceFromCenter + 1,
                                            Math.min(i1 + maxDistanceFromCenter + 1, levelheightaccessor.getMaxBuildHeight() - dimensionPadding.top()),
                                            j + maxDistanceFromCenter + 1);
                                    VoxelShape voxelshape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST);
                                    JigsawPlacement.addPieces(
                                            context.randomState(),
                                            maxDepth,
                                            useExpansionHack,
                                            chunkgenerator,
                                            structuretemplatemanager,
                                            levelheightaccessor,
                                            worldgenrandom,
                                            registry,
                                            poolelementstructurepiece,
                                            list,
                                            voxelshape,
                                            aliasLookup,
                                            liquidSettings);
                                    list.forEach(p_352014_::addPiece);
                                }
                            }));
        }
    }

    @FunctionalInterface
    public interface StructureGenerationContext {
        ResourceKey<Structure> getStructure(BlockState state, BlockGetter level, BlockPos pos, RandomSource random);
    }
}
