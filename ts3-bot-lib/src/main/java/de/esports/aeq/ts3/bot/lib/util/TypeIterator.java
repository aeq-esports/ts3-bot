package de.esports.aeq.ts3.bot.lib.util;

import java.util.List;


public interface TypeIterator<T> {

    T next();

    T next(List<T> present);

    boolean remove(T element);

    void reset();

    void add(T element);

    T getLower(T element);

    T getHigher(T element);
}