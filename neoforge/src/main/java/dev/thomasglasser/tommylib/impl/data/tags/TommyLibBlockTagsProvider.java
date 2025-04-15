package dev.thomasglasser.tommylib.impl.data.tags;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.data.tags.ExtendedBlockTagsProvider;
import dev.thomasglasser.tommylib.api.tags.ConventionalBlockTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

public class TommyLibBlockTagsProvider extends ExtendedBlockTagsProvider {
    public TommyLibBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, TommyLib.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        tag(ConventionalBlockTags.UNBREAKABLE_BLOCKS)
                .add(Blocks.BEDROCK)
                .add(Blocks.BARRIER)
                .add(Blocks.CHAIN_COMMAND_BLOCK)
                .add(Blocks.COMMAND_BLOCK)
                .add(Blocks.REPEATING_COMMAND_BLOCK)
                .add(Blocks.END_PORTAL_FRAME)
                .add(Blocks.JIGSAW)
                .add(Blocks.LIGHT)
                .add(Blocks.AIR)
                .add(Blocks.STRUCTURE_VOID)
                .add(Blocks.STRUCTURE_BLOCK)
                .add(Blocks.CAVE_AIR)
                .add(Blocks.VOID_AIR);
    }
}
