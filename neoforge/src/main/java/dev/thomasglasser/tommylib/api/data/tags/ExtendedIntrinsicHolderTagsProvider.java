package dev.thomasglasser.tommylib.api.data.tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;

/**
 * An {@link IntrinsicHolderTagsProvider} that dumps a list of all generated tags without the default namespace.
 *
 * @param <T> The type of tag.
 */
public abstract class ExtendedIntrinsicHolderTagsProvider<T> extends IntrinsicHolderTagsProvider<T> {
    protected final CompletableFuture<TagsProvider.TagLookup<T>> parentProvider;
    protected final PackOutput output;

    public ExtendedIntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, Function<T, ResourceKey<T>> keyExtractor, String modId) {
        this(output, registryKey, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), keyExtractor, modId);
    }

    public ExtendedIntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider, Function<T, ResourceKey<T>> keyExtractor, String modId) {
        super(output, registryKey, lookupProvider, parentProvider, keyExtractor, modId);
        this.parentProvider = parentProvider;
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return ExtendedTagsProvider.runAndDump(output, createContentsProvider(), contentsDone, parentProvider, registryKey, builders, this::getPath, this.output, modId);
    }
}
