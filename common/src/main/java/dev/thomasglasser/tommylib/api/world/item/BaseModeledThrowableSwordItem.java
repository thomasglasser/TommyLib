package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.Tier;

public abstract class BaseModeledThrowableSwordItem extends ThrowableSwordItem implements ModeledItem
{
	protected BaseModeledThrowableSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties)
	{
		super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
	}
}
