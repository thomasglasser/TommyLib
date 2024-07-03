package dev.thomasglasser.tommylib.api.data.loot;

import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import dev.thomasglasser.tommylib.api.registration.DeferredRegister;
import dev.thomasglasser.tommylib.api.world.level.block.LeavesSet;
import dev.thomasglasser.tommylib.api.world.level.block.WoodSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyBlockState;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

/**
 * Extension of {@link BlockLootSubProvider} that provides functionality for mod holders.
 */
public abstract class ExtendedBlockLootSubProvider extends BlockLootSubProvider {
    /**
     * The {@link Set} of blocks that this provider must generate loot tables for.
     */
    protected final Set<Block> knownBlocks;

    /**
     * Creates a new {@link ExtendedBlockLootSubProvider} knowing all blocks in a {@link Set}.
     * 
     * @param explosionResistant The set of items that are explosion resistant.
     * @param enabledFeatures    The {@link FeatureFlagSet} of enabled features.
     * @param knownBlocks        The {@link Set} of blocks to generate loot tables for.
     */
    protected ExtendedBlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider provider, Set<Block> knownBlocks) {
        super(explosionResistant, enabledFeatures, provider);
        this.knownBlocks = knownBlocks;
    }

    /**
     * Creates a new {@link ExtendedBlockLootSubProvider} knowing all blocks in a {@link Set}.
     * 
     * @param explosionResistant The set of items that are explosion resistant.
     * @param enabledFeatures    The {@link FeatureFlagSet} of enabled features.
     * @param existingTables     The existing loot tables map to add to.
     * @param provider           The {@link HolderLookup.Provider} to use for registry lookups.
     * @param knownBlocks        The {@link Set} of blocks to generate loot tables for.
     */
    protected ExtendedBlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, Map<ResourceKey<LootTable>, LootTable.Builder> existingTables, HolderLookup.Provider provider, Set<Block> knownBlocks) {
        super(explosionResistant, enabledFeatures, existingTables, provider);
        this.knownBlocks = knownBlocks;
    }

    /**
     * Creates a new {@link ExtendedBlockLootSubProvider} knowing all blocks in a {@link DeferredRegister}.
     * 
     * @param explosionResistant The set of items that are explosion resistant.
     * @param enabledFeatures    The {@link FeatureFlagSet} of enabled features.
     * @param knownBlocks        The {@link DeferredRegister} of blocks to generate loot tables for.
     */
    protected ExtendedBlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, HolderLookup.Provider provider, DeferredRegister<Block> knownBlocks) {
        this(explosionResistant, enabledFeatures, provider, knownBlocks.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet()));
    }

    /**
     * Creates a new {@link ExtendedBlockLootSubProvider} knowing all blocks in a {@link DeferredRegister}.
     * 
     * @param explosionResistant The set of items that are explosion resistant.
     * @param enabledFeatures    The {@link FeatureFlagSet} of enabled features.
     * @param existingTables     The existing loot tables map to add to.
     * @param provider           The {@link HolderLookup.Provider} to use for registry lookups.
     * @param knownBlocks        The {@link DeferredRegister} of blocks to generate loot tables for.
     */
    protected ExtendedBlockLootSubProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures, Map<ResourceKey<LootTable>, LootTable.Builder> existingTables, HolderLookup.Provider provider, DeferredRegister<Block> knownBlocks) {
        this(explosionResistant, enabledFeatures, existingTables, provider, knownBlocks.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet()));
    }

    /**
     * Makes the provider aware of only the provided blocks.
     */
    @Override
    public Set<Block> getKnownBlocks() {
        return knownBlocks;
    }

    /**
     * Adds default loot tables for all blocks in a {@link WoodSet}.
     * 
     * @param set The {@link WoodSet} to add loot tables for.
     */
    protected void woodSet(WoodSet set) {
        dropSelf(set.planks().get());
        dropSelf(set.log().get());
        dropSelf(set.strippedLog().get());
        dropSelf(set.wood().get());
        dropSelf(set.strippedWood().get());
    }

    /**
     * Adds default loot tables for all blocks in a {@link LeavesSet}.
     * 
     * @param set The {@link LeavesSet} to add loot tables for.
     */
    protected void leavesSet(LeavesSet set) {
        add(set.leaves().get(), createLeavesDrops(set.leaves().get(), set.sapling().get(), NORMAL_LEAVES_SAPLING_CHANCES));

        dropSelf(set.sapling().get());

        dropPottedContents(set.pottedSapling().get());
    }

    /**
     * Adds a loot table for a block
     * that drops itself with the specified {@link Property properties} in the item's {@code BlockStateData}
     * tag that is copied upon place.
     * 
     * @param block      The block to add a loot table for.
     * @param properties The properties to copy to the item's {@code BlockStateData} tag.
     */
    protected void dropWithProperties(Block block, Property<?>... properties) {
        CopyBlockState.Builder builder = CopyBlockState.copyState(block);
        for (Property<?> property : properties) {
            builder.copy(property);
        }
        add(block, createSingleItemTable(block.asItem()).apply(builder));
    }

    /**
     * Used for all leaves, drops self with silk touch,
     * otherwise drops the second Block param with the passed chances for fortune levels,
     * adding in sticks and the passed item.
     */
    protected LootTable.Builder createFruitfulLeavesDrops(Block pOakLeavesBlock, Block pSaplingBlock, Item fruit, float... pChances) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createLeavesDrops(pOakLeavesBlock, pSaplingBlock, pChances)
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .when(this.doesNotHaveShearsOrSilkTouch())
                                .add(
                                        (this.applyExplosionCondition(pOakLeavesBlock, LootItem.lootTableItem(fruit)))
                                                .when(
                                                        BonusLevelTableCondition.bonusLevelFlatChance(
                                                                registrylookup.getOrThrow(Enchantments.FORTUNE), 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
    }
}
