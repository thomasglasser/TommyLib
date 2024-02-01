package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public interface Enchantable
{
    boolean canEnchant(Enchantment enchantment, ItemStack stack);
}
