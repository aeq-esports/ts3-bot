package de.esports.aeq.ts3.bot.lib.channel;

import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class ChannelNameParser {

    public static final String NUMERIC_REGEX = "(.* )?(?<number>[0-9]+)( .*)?";

    public static final String NAME_REGEX = ".*\\{(NUM|ALPHA)}.*";
    public static final String NUMBER_REGEX = "\\{NUM}";
    public static final String NAME_NUMBER_REGEX = ".*\\{(NUM)}.*";

    private final String regex;

    private ChannelNameParser(String regex) {
        this.regex = regex;
    }

    public static NumericChannelNameParser numeric() {
        return new NumericChannelNameParser(NUMERIC_REGEX);
    }


    public static class NumericChannelNameParser extends ChannelNameParser {

        NumericChannelNameParser(String regex) {
            super(regex);
        }


        public Stream<Integer> stream(List<? extends Channel> channels) {
            Stream.Builder<Integer> builder = Stream.builder();
            for (Channel channel : channels) {
                try {
                    int number = getChannelNumber(channel.getName());
                    builder.add(number);
                } catch (MalformedChannelNameException e) {
                    // ignore
                }
            }
            return builder.build();
        }

        public int getChannelNumber(String name) throws MalformedChannelNameException {
            Pattern pattern = Pattern.compile(NAME_NUMBER_REGEX);
            Matcher matcher = pattern.matcher(name);
            if (matcher.matches() && matcher.groupCount() > 0) {
                try {
                    return Integer.valueOf(matcher.group());
                } catch (NumberFormatException e) {
                    throw new MalformedChannelNameException(name);
                }
            } else {
                throw new MalformedChannelNameException();
            }
        }
    }
}
