package dev.thomasglasser.tommylib.api.data.tags;

import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link BlockTagsProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedBlockTagsProvider extends BlockTagsProvider {
    public ExtendedBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    /**
     * Generates tags for a {@link WoodSet}.
     * 
     * @param set The set to generate tags for.
     */
    protected void woodSet(WoodSet set) {
        tag(set.logsBlockTag())
                .add(set.log().get(), set.strippedLog().get(), set.wood().get(), set.strippedWood().get());

        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(set.logsBlockTag());

        tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                .add(set.log().get());

        tag(BlockTags.PLANKS)
                .add(set.planks().get());

        tag(BlockTags.WOODEN_SLABS)
                .add(set.slab().get());

        tag(BlockTags.WOODEN_STAIRS)
                .add(set.stairs().get());

        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(set.pressurePlate().get());

        tag(BlockTags.WOODEN_BUTTONS)
                .add(set.button().get());

        tag(BlockTags.WOODEN_FENCES)
                .add(set.fence().get());

        tag(BlockTags.FENCE_GATES)
                .add(set.fenceGate().get());

        tag(BlockTags.WOODEN_DOORS)
                .add(set.door().get());

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(set.trapdoor().get());

        tag(BlockTags.STANDING_SIGNS)
                .add(set.sign().get());

        tag(BlockTags.WALL_SIGNS)
                .add(set.wallSign().get());

        tag(BlockTags.CEILING_HANGING_SIGNS)
                .add(set.hangingSign().get());

        tag(BlockTags.WALL_HANGING_SIGNS)
                .add(set.wallHangingSign().get());
    }

    /**
     * Generates tags for a {@link LeavesSet}.
     * 
     * @param set The set to generate tags for.
     */
    protected void leavesSet(LeavesSet set) {
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
