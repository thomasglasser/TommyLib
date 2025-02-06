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
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link TagsProvider} that dumps a list of all generated tags without the default namespace.
 *
 * @param <T> The type of tag.
 */
public abstract class ExtendedTagsProvider<T> extends TagsProvider<T> {
    protected final CompletableFuture<TagsProvider.TagLookup<T>> parentProvider;
    protected final PackOutput output;

    protected ExtendedTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        this(output, registryKey, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), modId, existingFileHelper);
    }

    protected ExtendedTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registryKey, lookupProvider, parentProvider, modId, existingFileHelper);
        this.parentProvider = parentProvider;
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return runAndDump(output, createContentsProvider(), contentsDone, parentProvider, registryKey, builders, existingFileHelper, this::getPath, this.output);
    }

    public static <T> CompletableFuture<?> runAndDump(CachedOutput output, CompletableFuture<HolderLookup.Provider> contentsProvider, CompletableFuture<Void> contentsDone, CompletableFuture<TagLookup<T>> parentProvider, ResourceKey<? extends Registry<T>> registryKey, Map<ResourceLocation, TagBuilder> builders, ExistingFileHelper existingFileHelper, Function<ResourceLocation, Path> pathGetter, PackOutput packOutput) {
        record CombinedData<T>(HolderLookup.Provider contents, TagsProvider.TagLookup<T> parent) {}
        ExistingFileHelper.ResourceType resourceType = new ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", net.minecraft.core.registries.Registries.tagsDirPath(registryKey));
        ExistingFileHelper.ResourceType elementResourceType = new ExistingFileHelper.ResourceType(net.minecraft.server.packs.PackType.SERVER_DATA, ".json", net.neoforged.neoforge.common.CommonHooks.prefixNamespace(registryKey.location()));

        return contentsProvider
                .thenApply(p_275895_ -> {
                    contentsDone.complete(null);
                    return p_275895_;
                })
                .thenCombineAsync(
                        parentProvider, CombinedData::new, Util.backgroundExecutor())
                .thenCompose(
                        p_323140_ -> {
                            HolderLookup.RegistryLookup<T> registrylookup = p_323140_.contents.lookupOrThrow(registryKey);
                            Predicate<ResourceLocation> predicate = p_255496_ -> registrylookup.get(ResourceKey.create(registryKey, p_255496_)).isPresent();
                            Predicate<ResourceLocation> predicate1 = p_274776_ -> builders.containsKey(p_274776_)
                                    || p_323140_.parent.contains(TagKey.create(registryKey, p_274776_));
                            JsonArray jsonarray = new JsonArray();
                            builders.keySet().stream().filter(rl -> !rl.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)).sorted().forEach(rl -> jsonarray.add(rl.toString()));
                            CompletableFuture<Void> tags = CompletableFuture.allOf(
                                    builders
                                            .entrySet()
                                            .stream()
                                            .sorted(Map.Entry.comparingByKey())
                                            .map(
                                                    p_323138_ -> {
                                                        ResourceLocation resourcelocation = p_323138_.getKey();
                                                        TagBuilder tagbuilder = p_323138_.getValue();
                                                        List<TagEntry> list = tagbuilder.build();
                                                        List<TagEntry> list1 = Stream.concat(list.stream(), tagbuilder.getRemoveEntries())
                                                                .filter((p_274771_) -> !p_274771_.verifyIfPresent(predicate, predicate1))
                                                                .filter(entry -> missing(entry, existingFileHelper, resourceType, elementResourceType))
                                                                .toList();
                                                        if (!list1.isEmpty()) {
                                                            throw new IllegalArgumentException(
                                                                    String.format(
                                                                            Locale.ROOT,
                                                                            "Couldn't define tag %s as it is missing following references: %s",
                                                                            resourcelocation,
                                                                            list1.stream().map(Objects::toString).collect(Collectors.joining(","))));
                                                        } else {
                                                            Path path = pathGetter.apply(resourcelocation);
                                                            if (path == null)
                                                                return CompletableFuture.completedFuture(null); // Neo: Allow running this data provider without writing it. Recipe provider needs valid tags.
                                                            var removed = tagbuilder.getRemoveEntries().toList();
                                                            return DataProvider.saveStable(output, p_323140_.contents, TagFile.CODEC, new TagFile(list, tagbuilder.isReplace(), removed), path);
                                                        }
                                                    })
                                            .toArray(CompletableFuture[]::new));
                            return jsonarray.isEmpty() ? tags : CompletableFuture.allOf(tags, DataProvider.saveStable(output, jsonarray, packOutput.getOutputFolder(PackOutput.Target.REPORTS).resolve("tags/" + registryKey.location().getNamespace() + "/" + registryKey.location().getPath() + ".json")));
                        });
    }

    private static boolean missing(TagEntry reference, ExistingFileHelper existingFileHelper, ExistingFileHelper.ResourceType resourceType, ExistingFileHelper.ResourceType elementResourceType) {
        // Optional tags should not be validated

        if (reference.isRequired()) {
            return existingFileHelper == null || !existingFileHelper.exists(reference.getId(), reference.isTag() ? resourceType : elementResourceType);
        }
        return false;
    }
}
