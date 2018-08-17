package de.esports.aeq.ts3.bot.lib.channel;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelBase;
import de.esports.aeq.ts3.bot.lib.util.PasswordGenerator;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class ChannelFactory {

    private Map<ChannelProperty, String> properties;

    private TypeIterator<String> typeIterator;
    private PasswordGenerator generator;

    private ChannelFactory(Builder builder) {
        this.typeIterator = builder.pattern.matcher();
        this.properties = builder.properties;
        this.generator = builder.generator;
    }

    public static Builder builder(NamePattern pattern) {
        return new Builder(pattern);
    }

    public ChannelTemplate getNext() {
        ChannelTemplate template = new ChannelTemplate();
        template.setName(typeIterator.next());
        return template;
    }

    public ChannelTemplate getNext(Collection<? extends ChannelBase> channels) {
        return new ChannelTemplate();
    }

    public TypeIterator<String> getTypeIterator() {
        return typeIterator;
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
