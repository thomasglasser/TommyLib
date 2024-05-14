package dev.thomasglasser.tommylib.api.data.tags;

import dev.thomasglasser.tommylib.api.world.item.armor.ArmorSet;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Extension of {@link ItemTagsProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedItemTagsProvider extends ItemTagsProvider
{
    public ExtendedItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> blockTagsProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(output, future, blockTagsProvider, modId, existingFileHelper);
    }

    /**
     * Creates a {@link ResourceLocation} with the given path and the "c" namespace.
     * @param path The path of the resource location.
     * @return A new {@link ResourceLocation} with the "c" namespace.
     */
    protected static ResourceLocation cLoc(String path)
    {
        return new ResourceLocation("c", path);
    }

    /**
     * Generates relevant tags for a {@link WoodSet}.
     * @param set The {@link WoodSet} to generate tags for.
     */
    protected void woodSet(WoodSet set)
    {
        copy(set.logsBlockTag().get(), set.logsItemTag().get());

        tag(ItemTags.PLANKS)
                .add(set.planks().get().asItem());

        tag(ItemTags.LOGS_THAT_BURN)
                .addTag(set.logsItemTag().get());
    }

    /**
     * Generates relevant tags for a {@link LeavesSet}.
     * @param set The {@link LeavesSet} to generate tags for.
     */
    protected void leavesSet(LeavesSet set)
    {
        tag(ItemTags.LEAVES)
                .add(set.leaves().get().asItem());

        tag(ItemTags.SAPLINGS)
                .add(set.sapling().get().asItem());
    }

    /**
     * Generates relevant tags for an {@link ArmorSet}.
     * @param armorSet The {@link ArmorSet} to generate tags for.
     */
    protected void armorSet(ArmorSet armorSet)
    {
        tag(ItemTags.HEAD_ARMOR).add(armorSet.HEAD.get());
        tag(ItemTags.CHEST_ARMOR).add(armorSet.CHEST.get());
        tag(ItemTags.LEG_ARMOR).add(armorSet.LEGS.get());
        tag(ItemTags.FOOT_ARMOR).add(armorSet.FEET.get());
    }

    @Override
    public String getName()
    {
        return modId + " Item Tags";
    }
}
