package de.esports.aeq.ts3.bot.lib.channel;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ChannelTemplate {

    private Map<ChannelProperty, String> properties = new EnumMap<>(ChannelProperty.class);

    private String name;

    public String getName() {
        return properties.get(ChannelProperty.CHANNEL_NAME);
    }

    public void setName(String name) {
        properties.put(ChannelProperty.CHANNEL_NAME, name);
    }

    public void setOrder(int order) {
        properties.put(ChannelProperty.CHANNEL_ORDER, String.valueOf(order));
    }

    public Map<ChannelProperty, String> asMap() {
        return Collections.emptyMap();
    }
}
