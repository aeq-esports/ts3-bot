package de.esports.aeq.ts3.bot.api.channel;

import java.util.Set;

/**
 * @author Lukas Kannenberg
 */
public interface TypeIterator {

    String next();

    boolean remove(String element);

    void reset();

    void add(String element);
}