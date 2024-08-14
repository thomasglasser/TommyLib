package dev.thomasglasser.tommylib.api.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.tommylib.api.world.level.block.grower.JigsawStructureGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class StructureSaplingBlock extends SaplingBlock {
    public static final MapCodec<StructureSaplingBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(JigsawStructureGrower.CODEC.fieldOf("structure").forGetter(block -> block.structureGrower), propertiesCodec())
                    .apply(instance, StructureSaplingBlock::new));

    protected final JigsawStructureGrower structureGrower;

    public StructureSaplingBlock(JigsawStructureGrower structureGrower, Properties properties) {
        super(null, properties);
        this.structureGrower = structureGrower;
    }

    @Override
    public MapCodec<? extends SaplingBlock> codec() {
        return CODEC;
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            this.structureGrower.grow(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }
}
