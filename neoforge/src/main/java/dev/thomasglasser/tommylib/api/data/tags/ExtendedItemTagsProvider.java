package dev.thomasglasser.tommylib.api.data.tags;

import dev.thomasglasser.tommylib.api.tags.ConventionalItemTags;
import dev.thomasglasser.tommylib.api.world.item.armor.ArmorSet;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

/**
 * Extension of {@link ItemTagsProvider} that provides functionality for mod holders
 * and dumps the contents of all generated tags without the default namespace.
 */
public abstract class ExtendedItemTagsProvider extends ItemTagsProvider {
    protected final PackOutput output;

    public ExtendedItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> blockTagsProvider, String modId) {
        super(output, future, blockTagsProvider, modId);
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return ExtendedTagsProvider.runAndDump(output, createContentsProvider(), contentsDone, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), registryKey, builders, this::getPath, this.output, modId);
    }

    /**
     * Generates relevant tags for a {@link WoodSet}.
     * 
     * @param set The {@link WoodSet} to generate tags for.
     */
    protected void woodSet(WoodSet set) {
        copy(set.logsBlockTag(), set.logsItemTag());

        tag(ItemTags.LOGS_THAT_BURN)
                .addTag(set.logsItemTag());

        tag(ConventionalItemTags.STRIPPED_LOGS)
                .add(set.strippedLog().asItem());

        tag(ConventionalItemTags.STRIPPED_WOODS)
                .add(set.strippedWood().asItem());

        tag(ItemTags.PLANKS)
                .add(set.planks().asItem());

        tag(ItemTags.WOODEN_SLABS)
                .add(set.slab().asItem());

        tag(ItemTags.WOODEN_STAIRS)
                .add(set.stairs().asItem());

        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(set.pressurePlate().asItem());

        tag(ItemTags.WOODEN_BUTTONS)
                .add(set.button().asItem());

        tag(ItemTags.WOODEN_FENCES)
                .add(set.fence().asItem());

        tag(ItemTags.FENCE_GATES)
                .add(set.fenceGate().asItem());

        tag(ItemTags.WOODEN_DOORS)
                .add(set.door().asItem());

        tag(ItemTags.WOODEN_TRAPDOORS)
                .add(set.trapdoor().asItem());

        tag(ItemTags.SIGNS)
                .add(set.sign().asItem());

        tag(ItemTags.HANGING_SIGNS)
                .add(set.hangingSign().asItem());

        tag(ItemTags.BOATS)
                .add(set.boatItem().asItem());

        tag(ItemTags.CHEST_BOATS)
                .add(set.chestBoatItem().asItem());
    }

    /**
     * Generates relevant tags for a {@link LeavesSet}.
     * 
     * @param set The {@link LeavesSet} to generate tags for.
     */
    protected void leavesSet(LeavesSet set) {
        tag(ItemTags.LEAVES)
                .add(set.leaves().get().asItem());

        tag(ItemTags.SAPLINGS)
                .add(set.sapling().get().asItem());
    }

    /**
     * Generates relevant tags for an {@link ArmorSet}.
     * 
     * @param armorSet The {@link ArmorSet} to generate tags for.
     */
    protected void armorSet(ArmorSet armorSet) {
        tag(ItemTags.HEAD_ARMOR).add(armorSet.HEAD.get());
        tag(ItemTags.CHEST_ARMOR).add(armorSet.CHEST.get());
        tag(ItemTags.LEG_ARMOR).add(armorSet.LEGS.get());
        tag(ItemTags.FOOT_ARMOR).add(armorSet.FEET.get());
    }

    @Override
    public String getName() {
        return modId + " Item Tags";
    }
}
