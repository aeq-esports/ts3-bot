package de.esports.aeq.ts3.bot.api.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DynamicMatcher {

    private Pattern pattern;
    private String template;
    private TypeIterator typeIterator;

    public DynamicMatcher(NamePattern pattern) {
        this.pattern = pattern.getPattern();
        this.template = "";
    }

    public String next() {
        String token = String.valueOf(typeIterator.next());
        return template.replace(NamePattern.PLACEHOLDER, token);
    }

    public String next(List<String> present) {
        typeIterator.reset();

        for (String s : present) {
            List<String> groups = extractGroup(s);
            if (groups.size() > 1) {
                throw new IllegalStateException();
            }
            typeIterator.add(groups.get(0));
        }

        return next();
    }

    private List<String> extractGroup(String string) {
        List<String> arguments = new ArrayList<>();
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            arguments.add(matcher.group());
        }
        return arguments;
    }

    public void add(String string) {
        List<String> group = extractGroup(string);
        if (group.size() > 1) {
            throw new IllegalStateException();
        }
        typeIterator.add(group.get(0));
    }

    public void remove(String string) {
        List<String> group = extractGroup(string);
        if (group.size() > 1) {
            throw new IllegalStateException();
        }
        typeIterator.remove(group.get(0));
    }
}
