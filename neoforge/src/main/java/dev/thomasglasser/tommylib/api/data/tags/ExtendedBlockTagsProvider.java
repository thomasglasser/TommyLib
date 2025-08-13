package dev.thomasglasser.tommylib.api.data.tags;

import dev.thomasglasser.tommylib.api.tags.ConventionalBlockTags;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link BlockTagsProvider} that provides helpers
 * and dumps the contents of all generated tags not in the default namespace.
 */
public abstract class ExtendedBlockTagsProvider extends BlockTagsProvider {
    protected final PackOutput output;

    protected ExtendedBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return ExtendedTagsProvider.runAndDump(output, createContentsProvider(), contentsDone, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), registryKey, builders, existingFileHelper, this::getPath, this.output);
    }

    @Override
    protected ExtendedIntrinsicHolderTagsProvider.ExtendedIntrinsicTagAppender<Block> tag(TagKey<Block> tag) {
        return new ExtendedIntrinsicHolderTagsProvider.ExtendedIntrinsicTagAppender<>(this.getOrCreateRawBuilder(tag), this.keyExtractor, this.modId);
    }

    /**
     * Generates tags for a {@link WoodSet}.
     * 
     * @param set The set to generate tags for.
     */
    protected void woodSet(WoodSet set) {
        tag(set.logsBlockTag())
                .add(set.log(), set.strippedLog(), set.wood(), set.strippedWood());

        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(set.logsBlockTag());

        tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                .add(set.log());

        tag(ConventionalBlockTags.STRIPPED_LOGS)
                .add(set.strippedLog());

        tag(ConventionalBlockTags.STRIPPED_WOODS)
                .add(set.strippedWood());

        tag(BlockTags.PLANKS)
                .add(set.planks());

        tag(BlockTags.WOODEN_SLABS)
                .add(set.slab());

        tag(BlockTags.WOODEN_STAIRS)
                .add(set.stairs());

        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(set.pressurePlate());

        tag(BlockTags.WOODEN_BUTTONS)
                .add(set.button());

        tag(BlockTags.WOODEN_FENCES)
                .add(set.fence());

        tag(BlockTags.FENCE_GATES)
                .add(set.fenceGate());

        tag(BlockTags.WOODEN_DOORS)
                .add(set.door());

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(set.trapdoor());

        tag(BlockTags.STANDING_SIGNS)
                .add(set.sign());

        tag(BlockTags.WALL_SIGNS)
                .add(set.wallSign());

        tag(BlockTags.CEILING_HANGING_SIGNS)
                .add(set.hangingSign());

        tag(BlockTags.WALL_HANGING_SIGNS)
                .add(set.wallHangingSign());
    }

    /**
     * Generates tags for a {@link LeavesSet}.
     * 
     * @param set The set to generate tags for.
     */
    protected void leavesSet(LeavesSet set) {
        tag(BlockTags.LEAVES)
                .add(set.leaves());

        tag(BlockTags.SAPLINGS)
                .add(set.sapling());
    }

    @Override
    public String getName() {
        return modId + " Block Tags";
    }
}
