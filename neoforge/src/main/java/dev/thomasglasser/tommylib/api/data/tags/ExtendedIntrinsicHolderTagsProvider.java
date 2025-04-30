package dev.thomasglasser.tommylib.api.data.tags;

import dev.thomasglasser.tommylib.api.registration.DeferredHolder;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link IntrinsicHolderTagsProvider} that dumps a list of all generated tags not in the default namespace.
 *
 * @param <T> The type of tag.
 */
public abstract class ExtendedIntrinsicHolderTagsProvider<T> extends IntrinsicHolderTagsProvider<T> {
    protected final CompletableFuture<TagsProvider.TagLookup<T>> parentProvider;
    protected final PackOutput output;

    protected ExtendedIntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, Function<T, ResourceKey<T>> keyExtractor, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        this(output, registryKey, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty()), keyExtractor, modId, existingFileHelper);
    }

    protected ExtendedIntrinsicHolderTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider, Function<T, ResourceKey<T>> keyExtractor, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registryKey, lookupProvider, parentProvider, keyExtractor, modId, existingFileHelper);
        this.parentProvider = parentProvider;
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return ExtendedTagsProvider.runAndDump(output, createContentsProvider(), contentsDone, parentProvider, registryKey, builders, existingFileHelper, this::getPath, this.output);
    }

    @Override
    protected ExtendedIntrinsicTagAppender<T> tag(TagKey<T> tag) {
        return new ExtendedIntrinsicTagAppender<>(this.getOrCreateRawBuilder(tag), this.keyExtractor, this.modId);
    }

    /**
     * Allows passing in holders for {@link DeferredHolder} support.
     * Direct holders are allowed in this case as the object has an intrinsic supported holder
     * 
     * @param <T> the type the tag is for
     */
    public static class ExtendedIntrinsicTagAppender<T> extends IntrinsicTagAppender<T> {
        public ExtendedIntrinsicTagAppender(TagBuilder builder, Function<T, ResourceKey<T>> keyExtractor, String modId) {
            super(builder, keyExtractor, modId);
        }

        public final IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> add(Holder<T> value) {
            value.unwrap().ifLeft(this::add).ifRight(this::add);
            return this;
        }

        @SafeVarargs
        public final IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> add(Holder<T>... values) {
            for (Holder<T> value : values) {
                value.unwrap().ifLeft(this::add).ifRight(this::add);
            }
            return this;
        }
    }
}
