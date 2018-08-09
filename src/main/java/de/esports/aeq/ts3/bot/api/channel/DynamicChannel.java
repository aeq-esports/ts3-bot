package de.esports.aeq.ts3.bot.api.channel;

import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelBase;
import de.esports.aeq.ts3.bot.api.BotListenerAdapter;
import de.esports.aeq.ts3.bot.api.TS3Bot;
import de.esports.aeq.ts3.bot.api.cache.ClientMoveEvent;
import de.esports.aeq.ts3.bot.util.StringTransformer;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Represents a channel that belongs to a group of dynamically generated channels.
 * <p>
 * Each group will be altered, depending on its configuration, resulting in channels being added or
 * removed from the group on the fly to reduce the total amount of channels needed and reduce
 * administrative effort.
 * <p>
 * Consider the following example:
 * <ul>
 * <li>ChannelTemplate 1</li>
 * <ul>
 * <li>ChannelTemplate A (3/5)</li>
 * <li>ChannelTemplate B (0/5)</li>
 * <li>ChannelTemplate C (0/5)</li>
 * </ul>
 * </ul>
 * ChannelTemplate A is half full while channel B and C are empty. Therefore, channel C can be deleted,
 * since clients could also join the empty channel B. If channel A and B are occupied, channel C can
 * be created again.
 *
 * @since 1.0
 */
public class DynamicChannel extends BotListenerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicChannel.class);

    @Language("RegExp")
    private static final String COMMAND_REGEX = "\\{.*}";

    private TS3Bot bot;
    private DynamicChannelConfig config;

    /**
     * Stores the current channel ids mapped to this dynamic channel.
     */
    private Set<Integer> channelIds = new HashSet<>();

    /**
     * Precompiled pattern to improve performance.
     */
    private Pattern channelNamePattern;

    public DynamicChannel(TS3Bot bot, DynamicChannelConfig config) {
        this.bot = bot;
        this.config = config;
        channelNamePattern = buildChannelNamePattern(config.getNamePattern());
    }

    private Pattern buildChannelNamePattern(String pattern) {
        String result = StringTransformer.of(pattern).replaceByRegex(COMMAND_REGEX,
                getRegexForGroup(), Pattern::quote).toString();
        return Pattern.compile(result);
    }

    private Function<String, String> getRegexForGroup() {
        return s -> {
            switch (s) {
                case "{NUM}":
                    return "[0-9]+";
                case "{ALPHA}":
                    return "[A-Z]+";
                default:
                    throw new IllegalArgumentException("Unknown group pattern: " + s);
            }
        };
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {
        update(e.getChannelId());
    }

    @Override
    public void onChannelDeleted(ChannelDeletedEvent e) {
        remove(e.getChannelId());
    }

    @Override
    public void onClientMoveEvent(ClientMoveEvent event) {
        if (!event.isLeaveEvent()) {
            update(event.getChannelId());
        }
        bot.getCache().getPreviousMoveEvent(event).ifPresent(e -> update(e.getChannelId()));
    }

    public void update(Channel channel) {
        Objects.requireNonNull(channel);
        if (matches(channel)) {
            channelIds.add(channel.getId());
            handleChanges();
        }
    }

    private void update(int channelId) {
        if (matches(channelId)) {
            channelIds.add(channelId);
            handleChanges();
        }
    }

    private void update(Collection<Integer> channelIds) {
        this.channelIds.stream().filter(this::matches).forEach(channelIds::add);
        handleChanges();
    }

    private void remove(int channelId) {
        channelIds.remove(channelId);
        bot.getApiAsync().deleteChannel(channelId);
    }

    private boolean matches(int channelId) {
        return bot.getCache().getChannel(channelId).filter(this::matches).isPresent();
    }

    /**
     * Returns whether a {@link Channel} matches the configuration of this dynamic channel.
     * <p>
     * More specifically, this method validates if the following conditions resolve to
     * <code>true</code>:
     * <ul>
     * <li>The channels parent channel id matches the configured parent channel id and</li>
     * <li>the channel name either matches the configured name pattern or the channel id is present
     * in the list of already managed channel ids</li>
     * </ul>
     * <p>
     * This last conditions ensures that the dynamic channel can have multiple sub-channels that do
     * not belong to the configuration which would otherwise be picked up, but also providing the
     * flexibility to pick up channels after a name pattern change.
     *
     * @param channel the channel
     * @return <code>true</code> if this channel matches the configuration of this dynamic channel,
     * otherwise <code>false</code>
     */
    private boolean matches(ChannelBase channel) {
        return channel.getParentChannelId() == config.getChannelId() &&
                channelNamePattern.matcher(channel.getName()).matches();
    }

    private void handleChanges() {
        synchronized (this) {
            resize();
            optimizeChannels();
        }
    }

    private void resize() {
        Collection<Channel> channels = bot.getCache().getChannels(channelIds);

        // The total size of each channel is not updated, so we have to fetch it
        Map<ChannelBase, Integer> map = new HashMap<>();
        for (ChannelBase channel : channels) {
            int size = bot.getCache().getTotalClients(channel.getId());
            map.put(channel, size);
        }

        int emptyChannels = (int) map.values().stream().filter(i -> i > 0).count();
        int difference = config.getAmountOfEmptyChannels() - emptyChannels;
        if (difference > 0) {
            int amount = map.size() + difference - config.getMaximumChannels();
            if (amount > 0) createChannels(amount, map.keySet());
        } else if (difference < 0) {
            int amount = map.size() - difference - config.getMaximumChannels();
            if (amount > 0) deleteChannels(amount, map.keySet());
        }
    }

    private void optimizeChannels() {
        // TODO sort by name
    }

    private void createChannels(int amount, Collection<? extends ChannelBase> present) {
        ChannelFactory factory = null;
        while (amount > 0) {
            ChannelTemplate template = factory.getNext(present);
            bot.getApiAsync().createChannel(template.getName(), template.asMap());
            amount--;
        }
    }

    private void deleteChannels(int amount, Collection<? extends ChannelBase> present) {
        present.stream().sorted(Comparator.comparing(ChannelBase::getName).reversed())
                .limit(amount).map(ChannelBase::getId).forEach(bot.getApiAsync()::deleteChannel);
    }

    public DynamicChannelConfig getConfig() {
        return config;
    }
}