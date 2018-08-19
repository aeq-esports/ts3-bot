package de.esports.aeq.ts3.bot.lib.util;

import java.util.*;
import java.util.function.Function;

public final class TypeIterators {

    public static TypeIterator<Integer> intAscending() {
        return new DefaultTypeIterator<>(1, i -> i + 1);
    }

    private static class DefaultTypeIterator<T> implements TypeIterator<T> {

        private T start;
        private Function<T, T> mapper;
        private Comparator<T> comparator;
        private NavigableSet<T> values;


        public DefaultTypeIterator(T start, Function<T, T> mapper) {
            this.start = Objects.requireNonNull(start);
            this.mapper = Objects.requireNonNull(mapper);
            this.values = new TreeSet<>();
        }

        public DefaultTypeIterator(T start, Function<T, T> mapper, Comparator<T> comparator) {
            this.start = Objects.requireNonNull(start);
            this.mapper = Objects.requireNonNull(mapper);
            this.comparator = comparator;
            this.values = new TreeSet<>(comparator);
        }

        @Override
        public T next() {
            if (values.isEmpty()) {
                addAndReturn(start);
            }

            var iterator = values.iterator();

            T next = mapper.apply(start);
            while (iterator.hasNext()) {
                T element = iterator.next();
                if (comparator != null) {
                    if (comparator.compare(element, next) == 0) {
                        return addAndReturn(next);
                    }
                } else {
                    /*
                     * The backing tree set will throw an exception if the comparator is null and
                     * the type does not implement Comparable, so this cast is safe.
                     */
                    @SuppressWarnings("unchecked")
                    Comparable<T> comparable = (Comparable<T>) element;
                    if (comparable.compareTo(next) == 0) {
                        return addAndReturn(next);
                    }
                }
                next = mapper.apply(next);
            }
            throw new NoSuchElementException("Stream is exhausted");
        }

        @Override
        public T next(List<T> present) {
            return null;
        }

        @Override
        public boolean remove(T element) {
            return values.remove(element);
        }

        @Override
        public void reset() {
            values.clear();
        }

        @Override
        public void add(T element) {
            values.add(element);
        }

        private T addAndReturn(T value) {
            add(value);
            return value;
        }

        @Override
        public T getLower(T element) {
            return values.lower(element);
        }

        @Override
        public T getHigher(T element) {
            return values.higher(element);
        }
    }
}
