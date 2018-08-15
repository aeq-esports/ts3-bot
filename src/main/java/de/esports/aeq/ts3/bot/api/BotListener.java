package de.esports.aeq.ts3.bot.api;

import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import de.esports.aeq.ts3.bot.api.event.ClientMoveEvent;

public interface BotListener extends TS3Listener {

    void onClientMoveEvent(ClientMoveEvent event);
}
