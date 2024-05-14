package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.Item;

/**
 * Base class of {@link ModeledItem} for {@link Item}s that performs NeoForge display setup automatically.
 */
public abstract class BaseModeledItem extends Item implements ModeledItem
{
	protected BaseModeledItem(Properties properties)
	{
		super(properties);
	}
}
