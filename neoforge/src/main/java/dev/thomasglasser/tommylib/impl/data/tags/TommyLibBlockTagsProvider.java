package dev.thomasglasser.tommylib.impl.data.tags;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.data.tags.ExtendedBlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static dev.thomasglasser.tommylib.api.tags.TommyLibBlockTags.UNBREAKABLE;

public class TommyLibBlockTagsProvider extends ExtendedBlockTagsProvider
{
	public TommyLibBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, TommyLib.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider p_256380_) {
		tag(UNBREAKABLE)
				.add(Blocks.BEDROCK)
				.add(Blocks.BARRIER)
				.add(Blocks.CHAIN_COMMAND_BLOCK)
				.add(Blocks.COMMAND_BLOCK)
				.add(Blocks.REPEATING_COMMAND_BLOCK)
				.addTag(BlockTags.PORTALS)
				.add(Blocks.END_PORTAL_FRAME)
				.add(Blocks.JIGSAW)
				.add(Blocks.LIGHT);
	}
}
