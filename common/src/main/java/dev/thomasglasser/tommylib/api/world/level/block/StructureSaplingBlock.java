package dev.thomasglasser.tommylib.api.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thomasglasser.tommylib.api.world.level.block.grower.StructureGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class StructureSaplingBlock extends SaplingBlock
{
	public static final MapCodec<StructureSaplingBlock> CODEC = RecordCodecBuilder.mapCodec(
			p_308834_ -> p_308834_.group(StructureGrower.CODEC.fieldOf("structure").forGetter(p_304391_ -> p_304391_.structureGrower), propertiesCodec())
					.apply(p_308834_, StructureSaplingBlock::new)
	);

	protected final StructureGrower structureGrower;

	public StructureSaplingBlock(StructureGrower structureGrower, Properties p_55979_)
	{
		super(null, p_55979_);
		this.structureGrower = structureGrower;
	}

	@Override
	public MapCodec<? extends SaplingBlock> codec()
	{
		return CODEC;
	}

	@Override
	public void advanceTree(ServerLevel pLevel, BlockPos pPos, BlockState pState, RandomSource pRandom)
	{
		if (pState.getValue(STAGE) == 0) {
			pLevel.setBlock(pPos, pState.cycle(STAGE), 4);
		} else {
			this.structureGrower.grow(pLevel, pLevel.getChunkSource().getGenerator(), pPos, pState, pRandom);
		}
	}
}
