package dev.thomasglasser.tommylib.api.data.tags;

import com.google.gson.JsonArray;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;

/**
 * A {@link TagsProvider} that dumps a list of all generated tags without the default namespace.
 *
 * @param <T> The type of tag.
 */
public abstract class ExtendedTagsProvider<T> extends TagsProvider<T> {
    protected final CompletableFuture<TagsProvider.TagLookup<T>> parentProvider;
    protected final PackOutput output;

    protected ExtendedTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        this(output, registryKey, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), modId);
    }

    protected ExtendedTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider, String modId) {
        super(output, registryKey, lookupProvider, parentProvider, modId);
        this.parentProvider = parentProvider;
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return runAndDump(output, createContentsProvider(), contentsDone, parentProvider, registryKey, builders, this::getPath, this.output, modId);
    }

    public static <T> CompletableFuture<?> runAndDump(CachedOutput output, CompletableFuture<HolderLookup.Provider> contentsProvider, CompletableFuture<Void> contentsDone, CompletableFuture<TagLookup<T>> parentProvider, ResourceKey<? extends Registry<T>> registryKey, Map<Identifier, TagBuilder> builders, Function<Identifier, Path> pathGetter, PackOutput packOutput, String modId) {
        record CombinedData<T>(HolderLookup.Provider contents, TagsProvider.TagLookup<T> parent) {}
        return contentsProvider
                .thenApply(provider -> {
                    contentsDone.complete(null);
                    return provider;
                })
                .thenCombineAsync(
                        parentProvider, CombinedData::new, Util.backgroundExecutor())
                .thenCompose(
                        combinedData -> {
                            HolderLookup.RegistryLookup<T> registrylookup = combinedData.contents.lookupOrThrow(registryKey);
                            Predicate<Identifier> lookupContains = location -> registrylookup.get(ResourceKey.create(registryKey, location)).isPresent();
                            Predicate<Identifier> buildersOrParentContains = location -> builders.containsKey(location)
                                    || combinedData.parent.contains(TagKey.create(registryKey, location));
                            JsonArray jsonarray = new JsonArray();
                            builders.keySet().stream().filter(id -> !id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)).sorted().forEach(id -> jsonarray.add(id.toString()));
                            CompletableFuture<Void> tags = CompletableFuture.allOf(
                                    builders
                                            .entrySet()
                                            .stream()
                                            .sorted(Map.Entry.comparingByKey())
                                            .map(
                                                    entry -> {
                                                        Identifier id = entry.getKey();
                                                        TagBuilder tagbuilder = entry.getValue();
                                                        List<TagEntry> list = tagbuilder.build();
                                                        List<TagEntry> list1 = Stream.concat(list.stream(), tagbuilder.getRemoveEntries())
                                                                // Neo: Assume tags from other namespaces always exists
                                                                .filter((tagEntry) -> tagEntry.getId().getNamespace().equals(modId) && !tagEntry.verifyIfPresent(lookupContains, buildersOrParentContains))
                                                                .toList();
                                                        if (!list1.isEmpty()) {
                                                            throw new IllegalArgumentException(
                                                                    String.format(
                                                                            Locale.ROOT,
                                                                            "Couldn't define tag %s as it is missing following references: %s",
                                                                            id,
                                                                            list1.stream().map(Objects::toString).collect(Collectors.joining(","))));
                                                        } else {
                                                            Path path = pathGetter.apply(id);
                                                            if (path == null) return CompletableFuture.completedFuture(null); // Neo: Allow running this data provider without writing it. Recipe provider needs valid tags.
                                                            var removed = tagbuilder.getRemoveEntries().toList();
                                                            return DataProvider.saveStable(output, combinedData.contents, TagFile.CODEC, new TagFile(list, tagbuilder.isReplace(), removed), path);
                                                        }
                                                    })
                                            .toArray(CompletableFuture[]::new));
                            return jsonarray.isEmpty() ? tags : CompletableFuture.allOf(tags, DataProvider.saveStable(output, jsonarray, packOutput.getOutputFolder(PackOutput.Target.REPORTS).resolve("tags/" + registryKey.identifier().getNamespace() + "/" + registryKey.identifier().getPath() + ".json")));
                        });
    }
}
