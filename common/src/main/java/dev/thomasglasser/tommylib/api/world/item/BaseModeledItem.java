package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.Item;

public abstract class BaseModeledItem extends Item implements ModeledItem
{
	protected BaseModeledItem(Properties properties)
	{
		super(properties);
	}
}
