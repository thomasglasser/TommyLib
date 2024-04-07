package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public abstract class BaseModeledSwordItem extends SwordItem implements ModeledItem
{
	protected BaseModeledSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties)
	{
		super(tier, attackDamageModifier, attackSpeedModifier, properties);
	}
}
