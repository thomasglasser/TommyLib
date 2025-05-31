package dev.thomasglasser.tommylib.api.data.tags;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.registration.DeferredItem;
import dev.thomasglasser.tommylib.api.tags.ConventionalItemTags;
import dev.thomasglasser.tommylib.api.world.item.armor.ArmorSet;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Extension of {@link ItemTagsProvider} that provides helpers
 * and dumps the contents of all generated tags not in the default namespace.
 */
public abstract class ExtendedItemTagsProvider extends ItemTagsProvider {
    protected final PackOutput output;

    protected ExtendedItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> blockTagsProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(output, future, blockTagsProvider, modId, existingFileHelper);
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return ExtendedTagsProvider.runAndDump(output, createContentsProvider(), contentsDone, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), registryKey, builders, existingFileHelper, this::getPath, this.output);
    }

    @Override
    protected ItemLikeTagAppender tag(TagKey<Item> tag) {
        return new ItemLikeTagAppender(this.getOrCreateRawBuilder(tag), this.keyExtractor, this.modId);
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
                .add(set.strippedLog());

        tag(ConventionalItemTags.STRIPPED_WOODS)
                .add(set.strippedWood());

        tag(ItemTags.PLANKS)
                .add(set.planks());

        tag(ItemTags.WOODEN_SLABS)
                .add(set.slab());

        tag(ItemTags.WOODEN_STAIRS)
                .add(set.stairs());

        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(set.pressurePlate());

        tag(ItemTags.WOODEN_BUTTONS)
                .add(set.button());

        tag(ItemTags.WOODEN_FENCES)
                .add(set.fence());

        tag(ItemTags.FENCE_GATES)
                .add(set.fenceGate());

        tag(ItemTags.WOODEN_DOORS)
                .add(set.door());

        tag(ItemTags.WOODEN_TRAPDOORS)
                .add(set.trapdoor());

        tag(ItemTags.SIGNS)
                .add(set.sign());

        tag(ItemTags.HANGING_SIGNS)
                .add(set.hangingSign());

        tag(ItemTags.BOATS)
                .add(set.boatItem());

        tag(ItemTags.CHEST_BOATS)
                .add(set.chestBoatItem());
    }

    /**
     * Generates relevant tags for a {@link LeavesSet}.
     * 
     * @param set The {@link LeavesSet} to generate tags for.
     */
    protected void leavesSet(LeavesSet set) {
        tag(ItemTags.LEAVES)
                .add(set.leaves());

        tag(ItemTags.SAPLINGS)
                .add(set.sapling());
    }

    /**
     * Generates relevant tags for an {@link ArmorSet}.
     * 
     * @param armorSet The {@link ArmorSet} to generate tags for.
     */
    protected void armorSet(ArmorSet armorSet) {
        tag(ItemTags.HEAD_ARMOR).add(armorSet.HEAD);
        tag(ItemTags.CHEST_ARMOR).add(armorSet.CHEST);
        tag(ItemTags.LEG_ARMOR).add(armorSet.LEGS);
        tag(ItemTags.FOOT_ARMOR).add(armorSet.FEET);
    }

    @Override
    public String getName() {
        return modId + " Item Tags";
    }

    /**
     * Allows passing any {@link ItemLike} for {@link Block}, {@link DeferredBlock}, and {@link DeferredItem} support
     */
    public static class ItemLikeTagAppender extends IntrinsicTagAppender<Item> {
        protected ItemLikeTagAppender(TagBuilder builder, Function<Item, ResourceKey<Item>> keyExtractor, String modId) {
            super(builder, keyExtractor, modId);
        }

        public ItemLikeTagAppender add(ItemLike value) {
            this.add(value.asItem());
            return this;
        }

        public ItemLikeTagAppender add(ItemLike... values) {
            for (ItemLike value : values) {
                add(value.asItem());
            }
            return this;
        }
    }
}
