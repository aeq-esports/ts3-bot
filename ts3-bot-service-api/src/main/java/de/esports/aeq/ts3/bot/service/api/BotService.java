package de.esports.aeq.ts3.bot.service.api;

import de.esports.aeq.ts3.bot.db.api.event.ClientMoveEventTa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BotService {

    List<ClientMoveEventTa> createMoveEvent(ClientMoveEventTa event);

    Optional<ClientMoveEventTa> findLastMoveEvent(String clientUId, LocalDateTime endExclusive);
}
