package dev.thomasglasser.tommylib.impl.data.tags;

import dev.thomasglasser.tommylib.TommyLib;
import dev.thomasglasser.tommylib.api.data.tags.ExtendedItemTagsProvider;
import dev.thomasglasser.tommylib.api.tags.ConventionalItemTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class TommyLibItemTagsProvider extends ExtendedItemTagsProvider {
    public TommyLibItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, CompletableFuture<TagLookup<Block>> blockTagsProvider) {
        super(output, future, blockTagsProvider, TommyLib.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(ConventionalItemTags.UNBREAKABLE_BLOCKS)
                .add(Items.BEDROCK)
                .add(Items.BARRIER)
                .add(Items.CHAIN_COMMAND_BLOCK)
                .add(Items.COMMAND_BLOCK)
                .add(Items.REPEATING_COMMAND_BLOCK)
                .add(Items.END_PORTAL_FRAME)
                .add(Items.JIGSAW)
                .add(Items.LIGHT)
                .add(Items.AIR)
                .add(Items.STRUCTURE_VOID)
                .add(Items.STRUCTURE_BLOCK);
    }
}
