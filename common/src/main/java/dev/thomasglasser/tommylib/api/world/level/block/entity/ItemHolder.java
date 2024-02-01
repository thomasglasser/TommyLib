package dev.thomasglasser.tommylib.api.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ItemHolder
{
    void handleTag(CompoundTag tag);

    int getSlotCount();

    ItemStack getInSlot(int slot);

    ItemStack insert(int slot, ItemStack stack);

    ItemStack extract(int slot, int amount);

    int getSlotMax(int slot);

    boolean isValid(int slot, ItemStack stack);
}
