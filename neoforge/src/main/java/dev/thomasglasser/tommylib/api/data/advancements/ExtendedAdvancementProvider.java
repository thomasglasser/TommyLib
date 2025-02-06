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
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * An {@link AdvancementProvider} that dumps a list of all generated advancements.
 */
public class ExtendedAdvancementProvider extends AdvancementProvider {
    protected final CompletableFuture<HolderLookup.Provider> registries;
    protected final PackOutput.PathProvider pathProvider;
    protected final List<AdvancementSubProvider> subProviders;
    protected final PackOutput output;

    public ExtendedAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
        super(output, registries, existingFileHelper, subProviders);
        this.registries = registries;
        this.pathProvider = output.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
        this.subProviders = subProviders.stream().map((generator) -> generator.toSubProvider(existingFileHelper)).toList();
        this.output = output;
    }

    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose((provider) -> {
            Set<ResourceLocation> set = new HashSet();
            List<CompletableFuture<?>> list = new ArrayList();
            Consumer<AdvancementHolder> consumer = (advancementHolder) -> {
                if (!set.add(advancementHolder.id())) {
                    throw new IllegalStateException("Duplicate advancement " + advancementHolder.id());
                } else {
                    Path path = this.pathProvider.json(advancementHolder.id());
                    list.add(DataProvider.saveStable(output, provider, Advancement.CODEC, advancementHolder.value(), path));
                }
            };

            for (AdvancementSubProvider advancementSubProvider : this.subProviders) {
                advancementSubProvider.generate(provider, consumer);
            }
            JsonArray jsonarray = new JsonArray();
            set.stream().sorted().forEach(rl -> jsonarray.add(rl.toString()));
            list.add(DataProvider.saveStable(output, jsonarray, this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("advancements.json")));
            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        });
    }
}
