package de.esports.aeq.ts3.bot.lib.cache;

import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class BotCache {

    public Client getClient(int clientId) {
        return null;
    }

    public int getTotalClients(int channelId) {
        return 0;
    }

    public Optional<Channel> getChannel(String name) {
        return Optional.empty();
    }

    public Optional<Channel> getChannel(int channelId) {
        return Optional.empty();
    }

    public Collection<Channel> getChannels(Collection<Integer> channelIds) {
        return Collections.emptyList();
    }

    public Optional<Channel> getChannelByClientId(int clientId) {
        return Optional.empty();
    }

}
