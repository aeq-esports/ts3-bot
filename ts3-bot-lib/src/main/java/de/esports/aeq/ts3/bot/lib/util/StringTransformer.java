package de.esports.aeq.ts3.bot.lib.util;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 1.0
 */
public class StringTransformer {

    private final String original;
    private String instance;

    public StringTransformer(String s) {
        this.original = s;
        instance = original;
    }

    public static StringTransformer of(String s) {
        return new StringTransformer(s);
    }

    public StringTransformer replaceByRegex(String regex,
            Function<String, String> matchTransformer) {
        replaceByRegex(regex, matchTransformer, null);
        return this;
    }

    public StringTransformer replaceByRegex(String regex, Function<String, String> matchTransformer,
            Function<String, String> mismatchTransformer) {
        Matcher matcher = Pattern.compile(regex).matcher(instance);
        StringBuilder builder = new StringBuilder();

        int lastGroupEnd = 0;
        while (matcher.find()) {
            if (matcher.start() != 0) {
                String substring = instance.substring(lastGroupEnd, matcher.start());
                builder.append(mismatchTransformer.apply(substring));
            }
            builder.append(matchTransformer.apply(matcher.group()));
            lastGroupEnd = matcher.end();
        }

        if (lastGroupEnd != instance.length()) {
            String substring = instance.substring(lastGroupEnd);
            builder.append(mismatchTransformer.apply(substring));
        }
        instance = builder.toString();
        return this;
    }

    /**
     * Returns the original String passed to this transformer.
     *
     * @return the original string
     */
    public String original() {
        return original;
    }

    @Override
    public String toString() {
        return instance;
    }
}
