package de.esports.aeq.ts3.bot.api.channel;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import de.esports.aeq.ts3.bot.util.PasswordGenerator;

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

    public ChannelTemplate getNext() {

    }

    public ChannelTemplate getNext(Collection<? extends ChannelTemplate> current) {

    }

    public class Builder {

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
