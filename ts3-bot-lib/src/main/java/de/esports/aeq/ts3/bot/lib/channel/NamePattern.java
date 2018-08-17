package de.esports.aeq.ts3.bot.lib.channel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamePattern {

    public static final String GROUP = "(\\{.*})";
    public static final String PLACEHOLDER = "${???}";

    private static final int START = 1;

    private StringBuilder patternBuilder = new StringBuilder();
    private StringBuilder templateBuilder = new StringBuilder();

    private Type type;
    private boolean dynamic;

    private String template;
    private Pattern pattern;

    private NamePattern() {

    }

    public static NamePattern of(String expression) {
        NamePattern pattern = new NamePattern();
        pattern.compile(expression);
        return pattern;
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

        dynamic = true;
    }

    public String getTemplate() {
        return template;
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

    public TypeIterator<String> matcher() {
        switch (type) {
            case NUMERIC:
                return new DynamicMatcher<>(this, TypeIterators.intAscending(), Integer::valueOf);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
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

    public Type getType() {
        return type;
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
