package dev.thomasglasser.tommylib.api.collection;

import java.util.Map;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.Nullable;

/** An unmodifiable view of a backing {@link Map}. */
public class ImmutableMapView<K, V> implements Map<K, V> {
    private final Map<K, V> delegate;
    private final ImmutableSetView<K> keySet;
    private final ImmutableCollectionView<V> values;
    private final ImmutableSetView<Map.Entry<K, V>> entrySet;

    public ImmutableMapView(Map<K, V> delegate) {
        this.delegate = delegate;
        this.keySet = new ImmutableSetView<>(delegate.keySet());
        this.values = new ImmutableCollectionView<>(delegate.values());
        this.entrySet = new ImmutableSetView<>(delegate.entrySet());
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return delegate.get(key);
    }

    @Override
    public ImmutableSetView<K> keySet() {
        return keySet;
    }

    @Override
    public ImmutableCollectionView<V> values() {
        return values;
    }

    @Override
    public ImmutableSetView<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        delegate.forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof ImmutableMapView<?, ?> other)
            return delegate.equals(other.delegate);
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    // Modifiers

    @Deprecated
    @Override
    public @Nullable V put(K key, V value) {
        throw new UnsupportedOperationException("ImmutableMapView cannot be modified");
    }

    @Deprecated
    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("ImmutableMapView cannot be modified");
    }

    @Deprecated
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("ImmutableMapView cannot be modified");
    }

    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException("ImmutableMapView cannot be modified");
    }
}
