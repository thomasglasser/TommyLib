package dev.thomasglasser.tommylib.api.data.advancements;

import com.google.gson.JsonArray;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * An {@link AdvancementProvider} that dumps a list of all generated advancements.
 */
public class ExtendedAdvancementProvider extends AdvancementProvider {
    protected final CompletableFuture<HolderLookup.Provider> registries;
    protected final PackOutput.PathProvider pathProvider;
    protected final List<AdvancementSubProvider> subProviders;
    protected final PackOutput output;

    public ExtendedAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, List<AdvancementSubProvider> subProviders) {
        super(output, registries, subProviders);
        this.registries = registries;
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
        this.subProviders = subProviders;
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(provider -> {
            Set<ResourceLocation> set = new HashSet<>();
            List<CompletableFuture<?>> list = new ArrayList<>();
            Consumer<AdvancementHolder> consumer = holder -> {
                if (!set.add(holder.id())) {
                    throw new IllegalStateException("Duplicate advancement " + holder.id());
                } else {
                    Path path = this.pathProvider.json(holder.id());
                    list.add(DataProvider.saveStable(output, provider, Advancement.CODEC, holder.value(), path));
                }
            };

            for (AdvancementSubProvider advancementsubprovider : this.subProviders) {
                advancementsubprovider.generate(provider, consumer);
            }

            JsonArray jsonarray = new JsonArray();
            set.stream().sorted().forEach(rl -> jsonarray.add(rl.toString()));
            list.add(DataProvider.saveStable(output, jsonarray, this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("advancements.json")));

            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }
}
