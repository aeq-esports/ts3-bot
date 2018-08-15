package de.esports.aeq.ts3.bot.api.channel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamePattern {

    public static final String GROUP = "(\\{.*})";
    public static final String PLACEHOLDER = "${???}";

    private static final int START = 1;

    private StringBuilder patternBuilder = new StringBuilder();
    private StringBuilder templateBuilder = new StringBuilder();

    private Map<Function<String, ?>, Function<?, String>> mappers = new HashMap<>();

    private Type type;
    private boolean dynamic;

    private String template;
    private Pattern pattern;

    private NamePattern() {

    }

    public static void of(String expression) {
        new NamePattern().compile(expression);
    }

    public Pattern getPattern() {
        return pattern;
    }

    private void addText(String text) {
        templateBuilder.append(text);
        patternBuilder.append(Pattern.quote(text));
    }

    private void addNumeric() {
        if (dynamic) {
            throw new IllegalArgumentException("Multiple dynamic parts are not supported");
        }
        type = Type.NUMERIC;
        templateBuilder.append(PLACEHOLDER);
        patternBuilder.append("([0-9]+)");

        addMapper(Integer::valueOf, i -> String.valueOf(i + 1));

        dynamic = true;
    }

    private int getNextInt(List<Integer> arguments, int start) {
        if (arguments.isEmpty()) {
            return start;
        }
        Collections.sort(arguments);

        int next = arguments.get(0);
        if (next > start) {
            return start;
        }

        for (int i = 1; i < arguments.size(); i++) {
            next++;
            if (next != arguments.get(i)) {
                break;
            }
        }
        return next;
    }

    private void compile() {
        if (dynamic) {
            template = templateBuilder.toString();
            pattern = Pattern.compile(patternBuilder.toString());
        }
        throw new IllegalArgumentException("Must contain at least one dynamic group");
    }

    private void compile(String expression) {
        Matcher matcher = Pattern.compile(GROUP).matcher(expression);

        int lastMatchEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > 0) {
                String sub = expression.substring(lastMatchEnd, matcher.start());
                addText(sub);
                lastMatchEnd = matcher.end();
            }
            handleGroup(matcher.group());
        }
        if (matcher.groupCount() > 0) {
            String sub = expression.substring(lastMatchEnd, expression.length());
            addText(sub);
        }
        compile();
    }

    private <T extends Comparable<T>> void addMapper(Function<String, T> mapper,
            Function<T, String> next) {

    }

    public Map<Function<String, ?>, Function<List<?>, ?>> getMappers() {
        return mappers;
    }

    private void handleGroup(String group) {
        switch (group) {
            case "{NUM}":
                addNumeric();
                break;
            default:
                throw new IllegalArgumentException("Illegal expression group: " + group);
        }
    }

    public enum Type {
        NUMERIC
    }

    public static class Builder {

        private NamePattern pattern = new NamePattern();

        public Builder text(String text) {
            pattern.addText(text);
            return this;
        }

        public Builder numeric() {
            pattern.addNumeric();
            return this;
        }

        public NamePattern build() {
            pattern.compile();
            return pattern;
        }
    }
}
