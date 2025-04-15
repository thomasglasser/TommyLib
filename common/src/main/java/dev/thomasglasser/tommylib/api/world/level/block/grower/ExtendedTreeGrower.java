package dev.thomasglasser.tommylib.api.world.level.block.grower;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

/**
 * {@link TreeGrower} that supports 3x3 trees
 */
public class ExtendedTreeGrower {
    private static final Map<String, ExtendedTreeGrower> GROWERS = new Object2ObjectArrayMap<>();
    public static final Codec<ExtendedTreeGrower> CODEC = Codec.stringResolver(p_304625_ -> p_304625_.name, GROWERS::get);

    private final String name;
    private final float secondaryChance;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> superTree;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondarySuperTree;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers;
    private final Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers;

    public ExtendedTreeGrower(String name, float secondaryChance, Optional<ResourceKey<ConfiguredFeature<?, ?>>> superTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondarySuperTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> megaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryMegaTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> tree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryTree, Optional<ResourceKey<ConfiguredFeature<?, ?>>> flowers, Optional<ResourceKey<ConfiguredFeature<?, ?>>> secondaryFlowers) {
        this.name = name;
        this.secondaryChance = secondaryChance;
        this.superTree = superTree;
        this.secondarySuperTree = secondarySuperTree;
        this.megaTree = megaTree;
        this.secondaryMegaTree = secondaryMegaTree;
        this.tree = tree;
        this.secondaryTree = secondaryTree;
        this.flowers = flowers;
        this.secondaryFlowers = secondaryFlowers;
        GROWERS.put(name, this);
    }

    @Nullable
    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean flowers) {
        if (random.nextFloat() < this.secondaryChance) {
            if (flowers && this.secondaryFlowers.isPresent()) {
                return this.secondaryFlowers.get();
            }

            if (this.secondaryTree.isPresent()) {
                return this.secondaryTree.get();
            }
        }

        return flowers && this.flowers.isPresent() ? this.flowers.get() : this.tree.orElse(null);
    }

    @Nullable
    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
        return this.secondaryMegaTree.isPresent() && random.nextFloat() < this.secondaryChance ? this.secondaryMegaTree.get() : this.megaTree.orElse(null);
    }

    @Nullable
    private ResourceKey<ConfiguredFeature<?, ?>> getConfiguredSuperFeature(RandomSource random) {
        return this.secondarySuperTree.isPresent() && random.nextFloat() < this.secondaryChance ? this.secondarySuperTree.get() : this.superTree.orElse(null);
    }

    public boolean growTree(ServerLevel level, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, RandomSource random) {
        ResourceKey<ConfiguredFeature<?, ?>> superFeature = this.getConfiguredSuperFeature(random);
        if (superFeature != null) {
            Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess()
                    .get(superFeature)
                    .orElse(null);
            if (holder != null) {
                for (int i = 0; i >= -1; i--) {
                    for (int j = 0; j >= -1; j--) {
                        if (isThreeByThreeSapling(state, level, pos)) {
                            ConfiguredFeature<?, ?> configuredfeature = holder.value();
                            BlockState blockstate = Blocks.AIR.defaultBlockState();
                            level.setBlock(pos.offset(i, 0, j), blockstate, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j), blockstate, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i, 0, j + 1), blockstate, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j + 1), blockstate, Block.UPDATE_ALL);
                            if (configuredfeature.place(level, chunkGenerator, random, pos.offset(i, 0, j))) {
                                return true;
                            }

                            level.setBlock(pos.offset(i, 0, j), state, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j), state, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i, 0, j + 1), state, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j + 1), state, Block.UPDATE_ALL);
                            return false;
                        }
                    }
                }
            }
        }

        ResourceKey<ConfiguredFeature<?, ?>> megaFeature = this.getConfiguredMegaFeature(random);
        if (megaFeature != null) {
            Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess()
                    .get(megaFeature)
                    .orElse(null);
            if (holder != null) {
                for (int i = 0; i >= -1; i--) {
                    for (int j = 0; j >= -1; j--) {
                        if (isTwoByTwoSapling(state, level, pos, i, j)) {
                            ConfiguredFeature<?, ?> configuredfeature = holder.value();
                            BlockState blockstate = Blocks.AIR.defaultBlockState();
                            level.setBlock(pos.offset(i, 0, j), blockstate, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j), blockstate, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i, 0, j + 1), blockstate, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j + 1), blockstate, Block.UPDATE_ALL);
                            if (configuredfeature.place(level, chunkGenerator, random, pos.offset(i, 0, j))) {
                                return true;
                            }

                            level.setBlock(pos.offset(i, 0, j), state, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j), state, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i, 0, j + 1), state, Block.UPDATE_ALL);
                            level.setBlock(pos.offset(i + 1, 0, j + 1), state, Block.UPDATE_ALL);
                            return false;
                        }
                    }
                }
            }
        }

        ResourceKey<ConfiguredFeature<?, ?>> feature = this.getConfiguredFeature(random, this.hasFlowers(level, pos));
        if (feature == null) {
            return false;
        } else {
            Holder<ConfiguredFeature<?, ?>> holder1 = level.registryAccess()
                    .get(feature)
                    .orElse(null);
            if (holder1 == null) {
                return false;
            } else {
                ConfiguredFeature<?, ?> configuredfeature1 = holder1.value();
                BlockState blockstate1 = level.getFluidState(pos).createLegacyBlock();
                level.setBlock(pos, blockstate1, Block.UPDATE_ALL);
                if (configuredfeature1.place(level, chunkGenerator, random, pos)) {
                    if (level.getBlockState(pos) == blockstate1) {
                        level.sendBlockUpdated(pos, state, blockstate1, Block.UPDATE_ALL);
                    }

                    return true;
                } else {
                    level.setBlock(pos, state, Block.UPDATE_ALL);
                    return false;
                }
            }
        }
    }

    private static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos, int xOffset, int yOffset) {
        Block block = state.getBlock();
        return level.getBlockState(pos.offset(xOffset, 0, yOffset)).is(block)
                && level.getBlockState(pos.offset(xOffset + 1, 0, yOffset)).is(block)
                && level.getBlockState(pos.offset(xOffset, 0, yOffset + 1)).is(block)
                && level.getBlockState(pos.offset(xOffset + 1, 0, yOffset + 1)).is(block);
    }

    private static boolean isThreeByThreeSapling(BlockState state, BlockGetter level, BlockPos pos) {
        Block block = state.getBlock();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!level.getBlockState(pos.offset(i, 0, j)).is(block)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean hasFlowers(LevelAccessor level, BlockPos pos) {
        for (BlockPos blockpos : BlockPos.MutableBlockPos.betweenClosed(pos.below().north(2).west(2), pos.above().south(2).east(2))) {
            if (level.getBlockState(blockpos).is(BlockTags.FLOWERS)) {
                return true;
            }
        }

        return false;
    }
}
