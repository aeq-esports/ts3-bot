package de.esports.aeq.ts3.bot.lib.channel;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelBase;
import de.esports.aeq.ts3.bot.lib.util.PasswordGenerator;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class ChannelFactory {

    private Map<ChannelProperty, String> properties;

    private NamePattern pattern;
    private PasswordGenerator generator;

    private ChannelFactory(Builder builder) {
        this.properties = builder.properties;
        this.pattern = builder.pattern;
        this.generator = builder.generator;
    }

    public static Builder builder(NamePattern pattern) {
        return new Builder(pattern);
    }

    public ChannelTemplate getNext() {
        return new ChannelTemplate();
    }

    public ChannelTemplate getNext(Collection<? extends ChannelBase> channels) {
        return new ChannelTemplate();
    }

    public static class Builder {

        private Map<ChannelProperty, String> properties = new EnumMap<>(ChannelProperty.class);

        private NamePattern pattern;
        private PasswordGenerator generator;

        private Builder(NamePattern pattern) {
            this.pattern = pattern;
        }

        public Builder properties(Map<ChannelProperty, String> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder passwords(PasswordGenerator generator) {
            this.generator = generator;
            return this;
        }

        public ChannelFactory build() {
            return new ChannelFactory(this);
        }
    }
}
