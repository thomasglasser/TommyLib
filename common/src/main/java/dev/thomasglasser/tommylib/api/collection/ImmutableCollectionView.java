package dev.thomasglasser.tommylib.api.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/// An unmodifiable view of a backing [Collection].
///
/// @param delegate The backing collection
/// @param <E> The element type
public record ImmutableCollectionView<E>(Collection<E> delegate) implements Collection<E> {
    /// Creates an unmodifiable view wrapping the given collection.
    ///
    /// @param delegate The backing collection to wrap
    /// @param <E>      The element type
    /// @return An [ImmutableCollectionView] wrapping the delegate
    public static <E> ImmutableCollectionView<E> of(Collection<E> delegate) {
        return new ImmutableCollectionView<>(delegate);
    }

    /// Returns an empty [ImmutableCollectionView].
    ///
    /// @param <E> The element type
    /// @return An empty unmodifiable collection view
    public static <E> ImmutableCollectionView<E> empty() {
        return of(List.of());
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
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private final Iterator<E> iterator = delegate.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Deprecated
            @Override
            public void remove() {
                throw new UnsupportedOperationException("ImmutableCollectionView iterator cannot be modified");
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                iterator.forEachRemaining(action);
            }
        };
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return delegate.toArray(generator);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public Spliterator<E> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return delegate.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return delegate.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        delegate.forEach(action);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Deprecated
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("ImmutableCollectionView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("ImmutableCollectionView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("ImmutableCollectionView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableCollectionView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableCollectionView cannot be modified");
    }

    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException("ImmutableCollectionView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException("ImmutableCollectionView cannot be modified");
    }
}
