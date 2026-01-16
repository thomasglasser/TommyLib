package dev.thomasglasser.tommylib.api.data.advancements;

import com.google.gson.JsonArray;
import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.nio.file.Path;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * An {@link AdvancementProvider} that dumps a list of all generated advancements.
 */
public class ExtendedAdvancementProvider implements DataProvider {
    protected final CompletableFuture<HolderLookup.Provider> registries;
    protected final PackOutput.PathProvider pathProvider;
    protected final Set<AdvancementSubProvider> subProviders;
    protected final PackOutput output;

    public ExtendedAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, Set<AdvancementProvider.AdvancementGenerator> subProviders) {
        this.registries = registries;
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
        this.subProviders = subProviders.stream().map(generator -> generator.toSubProvider(existingFileHelper)).collect(Collectors.toSet());
        this.output = output;
    }

    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose((provider) -> {
            SortedSet<ResourceLocation> ids = new ObjectRBTreeSet<>();
            Set<CompletableFuture<?>> futures = new ReferenceOpenHashSet<>();
            Consumer<AdvancementHolder> consumer = (advancementHolder) -> {
                if (!ids.add(advancementHolder.id())) {
                    throw new IllegalStateException("Duplicate advancement " + advancementHolder.id());
                } else {
                    Path path = this.pathProvider.json(advancementHolder.id());
                    futures.add(DataProvider.saveStable(output, provider, Advancement.CODEC, advancementHolder.value(), path));
                }
            };

            for (AdvancementSubProvider advancementSubProvider : this.subProviders) {
                advancementSubProvider.generate(provider, consumer);
            }
            JsonArray jsonarray = new JsonArray();
            ids.forEach(rl -> jsonarray.add(rl.toString()));
            futures.add(DataProvider.saveStable(output, jsonarray, this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("advancements.json")));
            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
