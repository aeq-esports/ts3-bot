package de.esports.aeq.ts3.bot.api;

import de.esports.aeq.ts3.bot.service.api.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotFactory {

    @Autowired
    private BotService service;

    public TS3Bot createInstance(BotConfig config) {
        return new TS3Bot(config, service);
    }
}
