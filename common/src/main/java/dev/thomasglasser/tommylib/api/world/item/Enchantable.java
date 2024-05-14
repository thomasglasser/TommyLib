package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Represents an item that can be enchanted and needs extra logic not included in the default implementation.
 */
public interface Enchantable
{
    /**
     * Checks if the item can be enchanted with the given enchantment.
     * If the enchantment already applies to the item, this method has no effect.
     */
    boolean canEnchant(Enchantment enchantment, ItemStack stack);
}
