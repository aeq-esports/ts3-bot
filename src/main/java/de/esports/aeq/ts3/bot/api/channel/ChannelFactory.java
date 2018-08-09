package de.esports.aeq.ts3.bot.api.channel;

import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelBase;

import java.util.Collection;

public interface ChannelFactory {

    ChannelTemplate getNext(Collection<? extends ChannelBase> current);
}
