package de.esports.aeq.ts3.bot.lib.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;


public class DynamicMatcher<T extends Comparable<T>> implements TypeIterator<String> {

    private NamePattern pattern;
    private TypeIterator<T> typeIterator;
    private Function<String, T> converter;

    public DynamicMatcher(NamePattern pattern, TypeIterator<T> typeIterator,
            Function<String, T> converter) {
        this.typeIterator = typeIterator;
        this.pattern = pattern;
        this.converter = converter;
    }

    @Override
    public String next() {
        String token = String.valueOf(typeIterator.next());
        return pattern.getTemplate().replace(NamePattern.PLACEHOLDER, token);
    }

    @Override
    public String next(List<String> present) {
        typeIterator.reset();

        for (String s : present) {
            String group = extractGroup(s);
            typeIterator.add(converter.apply(group));
        }

        return next();
    }

    private String extractGroup(String string) {
        List<String> arguments = new ArrayList<>();
        Matcher matcher = pattern.getPattern().matcher(string);
        while (matcher.find()) {
            arguments.add(matcher.group());
        }
        if (arguments.size() != 1) {
            throw new IllegalStateException();
        }
        return arguments.get(0);
    }

    @Override
    public void add(String string) {
        String group = extractGroup(string);
        typeIterator.add(converter.apply(group));
    }

    @Override
    public boolean remove(String string) {
        String group = extractGroup(string);
        return typeIterator.remove(converter.apply(group));
    }

    @Override
    public String getLower(String element) {
        String group = extractGroup(element);
        String token = String.valueOf(typeIterator.getLower(converter.apply(group)));
        return pattern.getTemplate().replace(NamePattern.PLACEHOLDER, token);
    }

    @Override
    public String getHigher(String element) {
        String group = extractGroup(element);
        String token = String.valueOf(typeIterator.getHigher(converter.apply(group)));
        return pattern.getTemplate().replace(NamePattern.PLACEHOLDER, token);
    }

    @Override
    public void reset() {
        typeIterator.reset();
    }
}
