package de.esports.aeq.ts3.bot.api.channel;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelBase;
import de.esports.aeq.ts3.bot.util.StringTransformer;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DynamicChannel extends TS3EventAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicChannel.class);

    @Language("RegExp")
    private static final String COMMAND_REGEX = "\\{.*}";

    private TS3ApiAsync apiAsync;
    private DynamicChannelConfig config;

    private Map<Integer, ChannelBase> channels = new HashMap<>();
    private Map<Integer, Integer> channelSizeMap = new HashMap<>();

    private Pattern channelNamePattern;

    public DynamicChannel(TS3ApiAsync apiAsync, DynamicChannelConfig config) {
        this.apiAsync = apiAsync;
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

    /**
     * Initializes this dynamic channel with the provided collection of channels.
     * <p>
     * Before updating, the provided collection will be filtered using {@link
     * #matchesChannel(ChannelBase)} to guarantee that each channel is part is this dynamic
     * channel.
     *
     * @param channels a {@link Collection} of channels to be updated
     */
    public void initialize(Collection<? extends Channel> channels) {
        Objects.requireNonNull(channels);
        channels.stream().filter(this::matchesChannel).forEach(this::registerChannel);
        handleChanges();
    }

    private void handleChanges() {
        resize();
        optimizeChannels();
    }

    private void resize() {
        int difference = config.getAmountOfEmptyChannels() - getAmountOfEmptyChannels();
        if (difference > 0) {
            int amount = channels.size() + difference - config.getMaximumChannels();
            if (amount > 0) createChannels(amount);
        } else if (difference < 0) {
            int amount = channels.size() - difference - config.getMaximumChannels();
            if (amount > 0) deleteChannels(amount);
        }
    }

    private void optimizeChannels() {
        List<ChannelBase> sortedChannels = channels.values().stream()
                .sorted(Comparator.comparing(ChannelBase::getName))
                .collect(Collectors.toList());
        // TODO
    }

    private int getAmountOfEmptyChannels() {
        return (int) channelSizeMap.values().stream().filter(i -> i > 0).count();
    }

    private void createChannels(int amount) {
        ChannelFactory factory = null;
        while (amount > 0) {
            createChannel(factory.getNext());
            amount--;
        }
    }

    private void deleteChannels(int amount) {
        channels.values().stream().sorted(Comparator.comparing(ChannelBase::getName)).limit(amount)
                .forEach(this::deleteChannel);
    }

    private void createChannel(ChannelTemplate template) {
        apiAsync.createChannel(template.getName(), template.asMap())
                .onSuccess(id -> {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Dynamic channel with id {} has been added from configuration {}",
                                id, config.toString());
                    }
                })
                .onFailure(e -> LOG
                        .error("Cannot create channel with configuration {}", template.toString
                                (), e));
    }

    /**
     * Updates a channel of this dynamic channel.
     * <p>
     * This method, or any other update method, should be called whenever the totals amount of
     * clients of any channel that belongs to this dynamic channel have changed.
     * <p>
     * In addition, this method also updates the internal snapshot of the specified channel. Use
     * this method whenever the original channel has been modified.
     *
     * @param channel the channel
     * @throws DynamicChannelException if the dynamic channel cannot be updated
     */
    public void update(Channel channel) throws DynamicChannelException {
        Objects.requireNonNull(channel);
        if (!matchesChannel(channel)) {
            throw new DynamicChannelException("ChannelTemplate does not match the dynamic channel" +
                    " " +
                    "configuration");
        }
        registerChannel(channel);
        handleChanges();
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
    public boolean matchesChannel(ChannelBase channel) {
        return channel.getParentChannelId() != config.getChannelId() &&
                (channelNamePattern.matcher(channel.getName()).matches() || matches(channel.getId
                        ()));
    }

    private void registerChannel(Channel channel) {
        this.channels.put(channel.getId(), channel);
        channelSizeMap.put(channel.getId(), channel.getTotalClients());
    }

    private boolean matches(int channelId) {
        // TODO return config.getChannelIds().contains(channelId);
        return false;
    }

    /**
     * Updates a channel of this dynamic channel that matches the given channel id.
     * <p>
     * This method, or any other update method, should be called whenever the totals amount of
     * clients of any channel that belongs to this dynamic channel have changed.
     * <p>
     * Please note that assumes that there have been <b>no changes</b> made to the original channel.
     * Otherwise, this method may produce unexpected results.
     *
     * @param channelId    the channel id
     * @param totalClients the new total amount of clients
     */
    public void update(int channelId, int totalClients) {
        channelSizeMap.put(channelId, totalClients);
        handleChanges();
    }

    private void deleteChannel(ChannelBase channel) {

    }

    private void editChannel(int channelId, Map<ChannelProperty, String> properties) {
        apiAsync.editChannel(channelId, properties)
                .onSuccess(
                        result -> LOG.info("Successfully edited channel with id {} using " +
                                        "properties {}",
                                channelId, properties.toString()))
                .onFailure(e -> LOG.error("Cannot edit channel with id {} using properties {}",
                        channelId, properties.toString()));
    }

    public DynamicChannelConfig getConfig() {
        return config;
    }
}
