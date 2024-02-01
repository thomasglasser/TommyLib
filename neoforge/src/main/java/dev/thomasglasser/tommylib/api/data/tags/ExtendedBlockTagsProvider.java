package dev.thomasglasser.tommylib.api.data.tags;

import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public abstract class ExtendedBlockTagsProvider extends IntrinsicHolderTagsProvider<Block>
{
    public ExtendedBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, Registries.BLOCK, lookupProvider, block -> block.builtInRegistryHolder().key(), modId, existingFileHelper);
    }

    protected void woodSet(WoodSet set)
    {
        tag(set.logsBlockTag().get())
                .add(set.log().get(), set.strippedLog().get(), set.wood().get(), set.strippedWood().get());

        tag(BlockTags.PLANKS)
                .add(set.planks().get());

        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(set.logsBlockTag().get());

        tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                .add(set.log().get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(set.logsBlockTag().get())
                .add(set.planks().get());
    }

    protected void leavesSet(LeavesSet set)
    {
        tag(BlockTags.LEAVES)
                .add(set.leaves().get());

        tag(BlockTags.SAPLINGS)
                .add(set.sapling().get());
    }

    @Override
    public String getName() {
        return modId + " Block Tags";
    }
}
