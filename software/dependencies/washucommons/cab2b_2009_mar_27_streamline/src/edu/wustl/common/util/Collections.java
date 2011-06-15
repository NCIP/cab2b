package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class Collections {
    private Collections() {

    }

    private static class EmptyIterator<E> implements Iterator<E> {

        public boolean hasNext() {
            return false;
        }

        public E next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }

    }

    private static class RemovalForbiddenIterator<E> implements Iterator<E> {
        private Iterator<E> wrappedIter;

        public RemovalForbiddenIterator(Iterator<E> wrappedIter) {
            this.wrappedIter = wrappedIter;
        }

        public boolean hasNext() {
            return wrappedIter.hasNext();
        }

        public E next() {
            return wrappedIter.next();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private static final Iterator EMPTY_ITERATOR = new EmptyIterator();

    @SuppressWarnings("unchecked")
    public static <E> Iterator<E> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    public static <E> Iterator<E> removalForbiddenIterator(Iterable<E> coll) {
        return new RemovalForbiddenIterator<E>(coll.iterator());
    }

    public static <E> List<E> list(Iterable<E> src) {
        List<E> res = new ArrayList<E>();
        fill(src, res);
        return res;
    }

    public static <E> Set<E> set(Iterable<E> src) {
        Set<E> res = new HashSet<E>();
        fill(src, res);
        return res;
    }

    public static <E> void fill(Iterable<E> src, Collection<E> target) {
        for (E e : src) {
            target.add(e);
        }
    }
}
