package dev.thomasglasser.tommylib.api.registration;

import com.mojang.datafixers.util.Either;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

/**
 * An ExtendedHolder is a {@link Holder} constructed with a {@link ResourceKey} that lazily binds to the underlying registry holder.
 *
 * @param <R> The base registry type.
 * @param <T> The specific object type held by this holder.
 */
public class ExtendedHolder<R, T extends R> implements Holder<R>, Supplier<T> {
    /** The resource key of the target object. */
    protected final ResourceKey<R> key;

    /** The currently cached value holder from the registry. */
    private @Nullable Holder<R> holder = null;

    /**
     * Constructs a new {@link ExtendedHolder} pointing to the specified resource key.
     *
     * @param key the resource key of the target object
     */
    protected ExtendedHolder(ResourceKey<R> key) {
        this.key = key;
        bind(false);
    }

    /**
     * Creates a new ExtendedHolder targeting the value with the specified name in the specified registry.
     *
     * @param registryKey The key of the registry the target value is a member of.
     * @param valueName   The name of the target value.
     * @param <R>         The base registry type.
     * @param <T>         The specific value type.
     * @return a new {@link ExtendedHolder} instance.
     */
    public static <R, T extends R> ExtendedHolder<R, T> create(ResourceKey<? extends Registry<R>> registryKey, ResourceLocation valueName) {
        return create(ResourceKey.create(registryKey, valueName));
    }

    /**
     * Creates a new ExtendedHolder targeting the value with the specified name in the specified registry.
     *
     * @param registryName The name of the registry the target value is a member of.
     * @param valueName    The name of the target value.
     * @param <R>          The base registry type.
     * @param <T>          The specific value type.
     * @return a new {@link ExtendedHolder} instance.
     */
    public static <R, T extends R> ExtendedHolder<R, T> create(ResourceLocation registryName, ResourceLocation valueName) {
        return create(ResourceKey.createRegistryKey(registryName), valueName);
    }

    /**
     * Creates a new ExtendedHolder targeting the specified value key.
     *
     * @param key The resource key of the target value.
     * @param <R> The base registry type.
     * @param <T> The specific value type.
     * @return a new {@link ExtendedHolder} instance.
     */
    public static <R, T extends R> ExtendedHolder<R, T> create(ResourceKey<R> key) {
        return new ExtendedHolder<>(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T value() {
        bind(true);
        if (holder == null)
            throw new NullPointerException("Trying to access unbound value: " + key);
        return (T) holder.value();
    }

    @Override
    public T get() {
        return value();
    }

    /**
     * Returns an optional containing the target object, if bound; otherwise an empty optional.
     *
     * @return an optional containing the target object if bound.
     */
    public Optional<T> asOptional() {
        return isBound() ? Optional.of(value()) : Optional.empty();
    }

    /**
     * Returns the registry that this ExtendedHolder is pointing at, or {@code null} if it doesn't exist.
     *
     * @return the backing registry or {@code null}.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    protected Registry<R> registry() {
        return (Registry<R>) BuiltInRegistries.REGISTRY.get(key.registry());
    }

    /**
     * Binds this ExtendedHolder to the underlying registry and target object.
     *
     * @param throwOnMissingRegistry If true, an exception will be thrown if the registry is absent.
     */
    protected final void bind(boolean throwOnMissingRegistry) {
        if (holder != null)
            return;
        Registry<R> registry = registry();
        if (registry != null)
            holder = registry.getHolder(key).orElse(null);
        else if (throwOnMissingRegistry)
            throw new IllegalStateException("Registry not present for " + this + ": " + key.registry());
    }

    /**
     * Returns the ID of the object pointed to by this ExtendedHolder.
     *
     * @return the {@link ResourceLocation} ID.
     */
    public ResourceLocation id() {
        return key.location();
    }

    /**
     * Returns the ResourceKey of the object pointed to by this ExtendedHolder.
     *
     * @return the {@link ResourceKey}.
     */
    public ResourceKey<R> key() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof Holder<?> h && h.kind() == Kind.REFERENCE && h.unwrapKey().orElse(null) == key;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "ExtendedHolder{%s}", key);
    }

    @Override
    public boolean isBound() {
        bind(false);
        return holder != null && holder.isBound();
    }

    @Override
    public boolean is(ResourceLocation id) {
        return id.equals(key.location());
    }

    @Override
    public boolean is(ResourceKey<R> key) {
        return key == this.key;
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> filter) {
        return filter.test(key);
    }

    @Override
    public boolean is(TagKey<R> tag) {
        bind(false);
        return holder != null && holder.is(tag);
    }

    @Override
    @Deprecated
    public boolean is(Holder<R> holder) {
        bind(false);
        return this.holder != null && this.holder.is(holder);
    }

    @Override
    public Stream<TagKey<R>> tags() {
        bind(false);
        return holder != null ? holder.tags() : Stream.empty();
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return Either.left(key);
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return Optional.of(key);
    }

    @Override
    public Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        bind(false);
        return holder != null && holder.canSerializeIn(owner);
    }

    /**
     * Resolves this holder to a {@link Reference} using the provided {@link HolderGetter}.
     *
     * @param getter the holder getter used to resolve this reference
     * @return the resolved {@link Reference}
     */
    public Reference<R> asReference(HolderGetter<R> getter) {
        return getter.getOrThrow(key);
    }

    /**
     * Resolves this holder to a {@link Reference} using the provided {@link HolderLookup.Provider}.
     *
     * @param registries the lookup provider used to resolve this reference
     * @return the resolved {@link Reference}, or {@code null} if the registry cannot be found
     */
    public Reference<R> asReference(HolderLookup.Provider registries) {
        return registries.lookupOrThrow(key.registryKey()).getOrThrow(key);
    }
}
