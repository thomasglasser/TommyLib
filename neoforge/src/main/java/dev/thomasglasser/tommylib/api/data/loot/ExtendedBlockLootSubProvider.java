package dev.thomasglasser.tommylib.api.data.loot;

import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extension of {@link BlockLootSubProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider
{
	/**
	 * The {@link Set} of blocks that this provider must generate loot tables for.
	 */
	protected final Set<Block> knownBlocks;

	/**
	 * Creates a new {@link ExtendedBlockLootSubProvider} knowing all blocks in a {@link Set}.
	 * @param pExplosionResistant The set of items that are explosion resistant.
	 * @param pEnabledFeatures The {@link FeatureFlagSet} of enabled features.
	 * @param knownBlocks The {@link Set} of blocks to generate loot tables for.
	 */
	protected ExtendedBlockLootSubProvider(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures, Set<Block> knownBlocks)
	{
		super(pExplosionResistant, pEnabledFeatures);
		this.knownBlocks = knownBlocks;
	}

	/**
	 * Creates a new {@link ExtendedBlockLootSubProvider} knowing all blocks in a {@link DeferredRegister}.
	 * @param pExplosionResistant The set of items that are explosion resistant.
	 * @param pEnabledFeatures The {@link FeatureFlagSet} of enabled features.
	 * @param knownBlocks The {@link DeferredRegister} of blocks to generate loot tables for.
	 */
	protected ExtendedBlockLootSubProvider(Set<Item> pExplosionResistant, FeatureFlagSet pEnabledFeatures, DeferredRegister<Block> knownBlocks)
	{
		super(pExplosionResistant, pEnabledFeatures);
		this.knownBlocks = knownBlocks.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
	}

	/**
	 * Makes the provider aware of only the provided blocks.
	 */
	@Override
	public Set<Block> getKnownBlocks()
	{
		return knownBlocks;
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

	/**
	 * Adds a loot table for a block
	 * that drops itself with the specified {@link Property properties} in the item's {@code BlockStateData}
	 * tag that is copied upon place.
	 * @param block The block to add a loot table for.
	 * @param properties The properties to copy to the item's {@code BlockStateData} tag.
	 */
	protected void dropWithProperties(Block block, Property<?>... properties)
	{
		CopyBlockState.Builder builder = CopyBlockState.copyState(block);
		for (Property<?> property : properties)
		{
			builder.copy(property);
		}
		add(block, createSingleItemTable(block.asItem()).apply(builder));
	}
}
