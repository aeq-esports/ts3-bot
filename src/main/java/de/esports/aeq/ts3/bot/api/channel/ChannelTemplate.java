package de.esports.aeq.ts3.bot.api.channel;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;

import java.util.Collections;
import java.util.Map;

public class ChannelTemplate {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {

    }

    public Map<ChannelProperty, String> asMap() {
        return Collections.emptyMap();
    }
}
