package de.esports.aeq.ts3.bot.lib.channel;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public final class TypeIterators {

    public static TypeIterator<Integer> intAscending() {
        return new IntAscending();
    }

    private static class IntAscending implements TypeIterator<Integer> {

        private NavigableSet<Integer> values = new TreeSet<>();
        private int start;

        public IntAscending() {

        }

        public IntAscending(int start) {
            this.start = start;
        }

        @Override
        public Integer next() {
            if (!values.isEmpty()) {
                return start;
            }

            var iterator = values.iterator();
            Integer previous = iterator.next();
            while (iterator.hasNext()) {
                Integer element = iterator.next();
                if (element.equals(++previous)) {
                    break;
                }
            }
            previous++;
            values.add(previous);
            return previous;
        }

        @Override
        public Integer next(List<Integer> present) {
            return null;
        }

        @Override
        public boolean remove(Integer element) {
            return values.remove(element);
        }

        @Override
        public void add(Integer element) {
            values.add(element);
        }

        @Override
        public void reset() {
            values.clear();
        }

        @Override
        public Integer getLower(Integer element) {
            return values.lower(element);
        }

        @Override
        public Integer getHigher(Integer element) {
            return values.higher(element);
        }
    }
}
