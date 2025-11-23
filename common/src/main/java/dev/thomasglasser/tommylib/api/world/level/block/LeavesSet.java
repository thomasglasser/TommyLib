package dev.thomasglasser.tommylib.api.world.level.block;

import dev.thomasglasser.tommylib.api.registration.DeferredBlock;
import dev.thomasglasser.tommylib.api.world.item.CreativeModeTabInserter;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * A set of blocks related to a leaf block.
 * 
 * @param id            The ID of the leaves set
 * @param leaves        The leaf block
 * @param sapling       The sapling block
 * @param pottedSapling The potted sapling block
 */
public record LeavesSet(ResourceLocation id,
        DeferredBlock<?> leaves,
        DeferredBlock<?> sapling,
        DeferredBlock<?> pottedSapling) {
    public List<Block> getAllBlocks() {
        return ObjectArrayList.of(leaves.get(), sapling.get(), pottedSapling.get());
    }

    public List<Item> getAllItems() {
        return ObjectArrayList.of(leaves.asItem(), sapling.asItem());
    }

    public void addToCreativeModeTab(ResourceKey<CreativeModeTab> tab, CreativeModeTabInserter inserter, ItemStack precedingLeaves, ItemStack precedingSapling) {
        if (tab == CreativeModeTabs.NATURAL_BLOCKS) {
            inserter.insertAfter(precedingLeaves, leaves.toStack());
            inserter.insertAfter(precedingSapling, sapling.toStack());
        }
    }
}
