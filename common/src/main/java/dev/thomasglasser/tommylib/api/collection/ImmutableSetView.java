package dev.thomasglasser.tommylib.api.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/** An unmodifiable view of a backing {@link Set}. */
public class ImmutableSetView<E> implements Set<E> {
    private final Set<E> delegate;

    public ImmutableSetView(Set<E> delegate) {
        this.delegate = delegate;
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

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                iterator.forEachRemaining(action);
            }

            @Deprecated
            @Override
            public void remove() {
                throw new UnsupportedOperationException("ImmutableSetView iterator cannot be modified");
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
        return Spliterators.spliterator(delegate, Spliterator.DISTINCT & Spliterator.IMMUTABLE);
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof ImmutableSetView<?> other)
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
    public boolean add(E e) {
        throw new UnsupportedOperationException("ImmutableSetView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("ImmutableSetView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("ImmutableSetView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableSetView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableSetView cannot be modified");
    }

    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException("ImmutableSetView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException("ImmutableSetView cannot be modified");
    }
}
