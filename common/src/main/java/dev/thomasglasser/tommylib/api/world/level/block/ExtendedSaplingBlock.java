package dev.thomasglasser.tommylib.api.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.tommylib.api.world.level.block.grower.ExtendedTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Sapling block that supports {@link ExtendedTreeGrower}s
 */
public class ExtendedSaplingBlock extends SaplingBlock {
    public static final MapCodec<ExtendedSaplingBlock> CODEC = RecordCodecBuilder.mapCodec(
            p_308834_ -> p_308834_.group(ExtendedTreeGrower.CODEC.fieldOf("tree").forGetter(p_304391_ -> p_304391_.treeGrower), propertiesCodec())
                    .apply(p_308834_, ExtendedSaplingBlock::new));

    private final ExtendedTreeGrower treeGrower;

    public ExtendedSaplingBlock(ExtendedTreeGrower treeGrower, Properties properties) {
        super(null, properties);
        this.treeGrower = treeGrower;
    }

    @Override
    public MapCodec<? extends ExtendedSaplingBlock> codec() {
        return CODEC;
    }

    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), Block.UPDATE_ALL);
        } else {
            this.treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }
}
