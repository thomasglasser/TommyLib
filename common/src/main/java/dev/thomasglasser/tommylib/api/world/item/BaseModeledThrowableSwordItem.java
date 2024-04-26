package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.Tier;

public abstract class BaseModeledThrowableSwordItem extends ThrowableSwordItem implements ModeledItem
{
	protected BaseModeledThrowableSwordItem(Tier pTier, Properties pProperties)
	{
		super(pTier, pProperties);
	}
}
