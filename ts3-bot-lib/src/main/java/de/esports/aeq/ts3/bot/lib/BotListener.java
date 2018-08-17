package de.esports.aeq.ts3.bot.lib;

import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import de.esports.aeq.ts3.bot.lib.event.ClientMoveEvent;

public interface BotListener extends TS3Listener {

    void onClientMoveEvent(ClientMoveEvent event);
}
