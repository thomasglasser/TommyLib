package dev.thomasglasser.tommylib.api.data.loot;

import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;

import java.util.Set;

public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider
{
	protected ExtendedBlockLootSubProvider(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures)
	{
		super(pExplosionResistant, pEnabledFeatures);
	}

	protected void woodSet(WoodSet set)
	{
		dropSelf(set.planks().get());
		dropSelf(set.log().get());
		dropSelf(set.strippedLog().get());
		dropSelf(set.wood().get());
		dropSelf(set.strippedWood().get());

	}

	protected void leavesSet(LeavesSet set)
	{
		add(set.leaves().get(), createLeavesDrops(set.leaves().get(), set.sapling().get(), NORMAL_LEAVES_SAPLING_CHANCES));

		dropSelf(set.sapling().get());

		dropPottedContents(set.pottedSapling().get());
	}
}
