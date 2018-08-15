package de.esports.aeq.ts3.bot.api.channel;

import java.util.Set;
import java.util.TreeSet;

public final class TypeIterators {

    public static class IntAscending implements TypeIterator<Integer> {

        private Set<Integer> values = new TreeSet<>();
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
        public Set<Integer> elements() {
            return values;
        }

        @Override
        public boolean remove(Integer element) {
            return values.remove(element);
        }

        @Override
        public void reset() {
            values.clear();
        }
    }
}
