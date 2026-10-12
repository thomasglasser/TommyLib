package dev.thomasglasser.tommylib.api.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

/** An unmodifiable view of a backing {@link List}. */
public class ImmutableListView<E> implements List<E> {
    private final List<E> delegate;

    public ImmutableListView(List<E> delegate) {
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
                throw new UnsupportedOperationException("ImmutableListView iterator cannot be modified");
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
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public E get(int index) {
        return delegate.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListIterator<>() {
            private final ListIterator<E> iterator = delegate.listIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public boolean hasPrevious() {
                return iterator.hasPrevious();
            }

            @Override
            public E previous() {
                return iterator.previous();
            }

            @Override
            public int nextIndex() {
                return iterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iterator.previousIndex();
            }

            @Deprecated
            @Override
            public void remove() {
                throw new UnsupportedOperationException("ImmutableListView cannot be modified");
            }

            @Deprecated
            @Override
            public void set(E e) {
                throw new UnsupportedOperationException("ImmutableListView cannot be modified");
            }

            @Deprecated
            @Override
            public void add(E e) {
                throw new UnsupportedOperationException("ImmutableListView cannot be modified");
            }
        };
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIterator<>() {
            private ListIterator<E> iterator = delegate.listIterator(index);

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public boolean hasPrevious() {
                return iterator.hasPrevious();
            }

            @Override
            public E previous() {
                return iterator.previous();
            }

            @Override
            public int nextIndex() {
                return iterator.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iterator.previousIndex();
            }

            @Deprecated
            @Override
            public void remove() {
                throw new UnsupportedOperationException("ImmutableListView iterator cannot be modified");
            }

            @Deprecated
            @Override
            public void set(E e) {
                throw new UnsupportedOperationException("ImmutableListView iterator cannot be modified");
            }

            @Deprecated
            @Override
            public void add(E e) {
                throw new UnsupportedOperationException("ImmutableListView iterator cannot be modified");
            }
        };
    }

    @Override
    public ImmutableListView<E> subList(int fromIndex, int toIndex) {
        return new ImmutableListView<>(delegate.subList(fromIndex, toIndex));
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(delegate, Spliterator.ORDERED & Spliterator.IMMUTABLE);
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
        if (o instanceof ImmutableListView<?> other)
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
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }

    @Deprecated
    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException("ImmutableListView cannot be modified");
    }
}
