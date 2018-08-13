package de.esports.aeq.ts3.bot.api.channel;

import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelBase;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;


public class DynamicMatcher {

    private NamePattern pattern;

    private NavigableMap<String, ChannelBase> channelNames = new TreeMap<>();

    public DynamicMatcher(NamePattern pattern) {
        this.pattern = pattern;
    }

    public boolean matches(String string) {

    }

    public String next() {

    }

    public String next(Collection<String> existing) {

    }

    public void add(String string) {

    }

    public void remove(String string) {

    }

    public int index() {

    }

    public String get(int index) {

    }

    public String current() {

    }
}
