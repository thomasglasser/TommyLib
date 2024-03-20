package dev.thomasglasser.tommylib.api.world.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

public abstract class BaseModeledDiggerItem extends DiggerItem implements ModeledItem
{
	protected BaseModeledDiggerItem(float attackDamageModifier, float attackSpeedModifier, Tier tier, TagKey<Block> blocks, Properties properties)
	{
		super(attackDamageModifier, attackSpeedModifier, tier, blocks, properties);
	}
}
