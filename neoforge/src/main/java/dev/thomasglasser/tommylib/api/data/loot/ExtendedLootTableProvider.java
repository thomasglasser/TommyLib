package dev.thomasglasser.tommylib.api.data.loot;

import dev.thomasglasser.tommylib.api.data.info.ModRegistryDumpReport;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;

/**
 * Extension of {@link LootTableProvider} that provides default validation
 * and dumps a list of all generated loot tables.
 */
public abstract class ExtendedLootTableProvider extends LootTableProvider {
    protected final PackOutput output;
    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;

    protected WritableRegistry<LootTable> registry;

    public ExtendedLootTableProvider(PackOutput pOutput, Set<ResourceKey<LootTable>> pRequiredTables, List<SubProviderEntry> pSubProviders, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pOutput, pRequiredTables, pSubProviders, lookupProvider);
        this.output = pOutput;
        this.lookupProvider = lookupProvider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return CompletableFuture.allOf(super.run(output), dumpRegistry(output));
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        writableregistry.listElements()
                .forEach(
                        p_380823_ -> p_380823_.value()
                                .validate(
                                        validationcontext.setContextKeySet(p_380823_.value().getParamSet())
                                                .enterElement("{" + p_380823_.key().location() + "}", p_380823_.key())));
    }

    protected CompletableFuture<?> dumpRegistry(CachedOutput output) {
        return this.lookupProvider.thenCompose((registries) -> DataProvider.saveStable(output, ModRegistryDumpReport.dumpRegistry(registry, ""), this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("loot_tables.json")));
    }
}
