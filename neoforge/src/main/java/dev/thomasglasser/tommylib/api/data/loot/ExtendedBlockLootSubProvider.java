package dev.thomasglasser.tommylib.api.data.loot;

import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;

import java.util.Set;

/**
 * Extension of {@link BlockLootSubProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider
{
	protected ExtendedBlockLootSubProvider(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures)
	{
		super(pExplosionResistant, pEnabledFeatures);
	}

	/**
	 * Adds default loot tables for all blocks in a {@link WoodSet}.
	 * @param set The {@link WoodSet} to add loot tables for.
	 */
	protected void woodSet(WoodSet set)
	{
		dropSelf(set.planks().get());
		dropSelf(set.log().get());
		dropSelf(set.strippedLog().get());
		dropSelf(set.wood().get());
		dropSelf(set.strippedWood().get());

	}

	/**
	 * Adds default loot tables for all blocks in a {@link LeavesSet}.
	 * @param set The {@link LeavesSet} to add loot tables for.
	 */
	protected void leavesSet(LeavesSet set)
	{
		add(set.leaves().get(), createLeavesDrops(set.leaves().get(), set.sapling().get(), NORMAL_LEAVES_SAPLING_CHANCES));

		dropSelf(set.sapling().get());

		dropPottedContents(set.pottedSapling().get());
	}
}
