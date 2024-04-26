package dev.thomasglasser.tommylib.api.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class ExtendedLootTableProvider extends LootTableProvider
{
	public ExtendedLootTableProvider(PackOutput pOutput, Set<ResourceKey<LootTable>> pRequiredTables, List<SubProviderEntry> pSubProviders, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(pOutput, pRequiredTables, pSubProviders, lookupProvider);
	}

	@Override
	protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
		writableregistry.holders().forEach(p_335195_ -> {
			p_335195_.value().validate(validationcontext.setParams(p_335195_.value().getParamSet()).enterElement("{" + p_335195_.key().location() + "}", p_335195_.key()));
		});
	}
}
