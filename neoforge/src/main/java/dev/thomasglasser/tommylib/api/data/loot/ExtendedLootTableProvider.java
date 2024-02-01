package dev.thomasglasser.tommylib.api.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ExtendedLootTableProvider extends LootTableProvider
{
	public ExtendedLootTableProvider(PackOutput pOutput, Set<ResourceLocation> pRequiredTables, List<SubProviderEntry> pSubProviders)
	{
		super(pOutput, pRequiredTables, pSubProviders);
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
		map.forEach((location, lootTable) -> lootTable.validate(validationtracker));
	}
}
