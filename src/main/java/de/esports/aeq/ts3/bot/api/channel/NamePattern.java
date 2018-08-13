package de.esports.aeq.ts3.bot.api.channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NamePattern {

    private static final String GROUP = "(\\{.*})";
    private static final String PLACEHOLDER = "${???}";

    private static final int START = 1;

    private StringBuilder patternBuilder = new StringBuilder();
    private StringBuilder templateBuilder = new StringBuilder();
    private Type type;
    private boolean dynamic;

    private String template;
    private Pattern pattern;

    private String current;

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

    public DynamicMatcher matcher() {
        return new DynamicMatcher(this);
    }

    public String next(List<String> present) {

        if (present.isEmpty()) {
            // TODO next = start number
        }

        Collections.sort(present);

        List<String> arguments = new ArrayList<>();
        for (String s : present) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                arguments.add(matcher.group());
            }
        }

        String next;
        switch (type) {
            case NUMERIC:
                List<Integer> ints = arguments.stream().mapToInt(Integer::valueOf).boxed()
                        .collect(Collectors.toList());
                next = String.valueOf(getNextInt(ints, START));
                break;
            default:
                throw new IllegalStateException(type.toString());
        }

        return template.replace(PLACEHOLDER, next);
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

    private void handleGroup(String group) {
        switch (group) {
            case "{NUM}":
                addNumeric();
                break;
            default:
                throw new IllegalArgumentException("Illegal expression group: " + group);
        }
    }

    private enum Type {
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
