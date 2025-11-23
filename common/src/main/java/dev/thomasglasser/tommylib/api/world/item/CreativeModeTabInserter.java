package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface CreativeModeTabInserter {
    void insertAfter(ItemStack precedingStack, ItemStack stack, CreativeModeTab.TabVisibility tabVisibility);

    default void insertAfter(ItemStack precedingStack, ItemStack stack) {
        insertAfter(precedingStack, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
