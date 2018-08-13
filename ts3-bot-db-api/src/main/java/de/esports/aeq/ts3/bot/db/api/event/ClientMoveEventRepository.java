package de.esports.aeq.ts3.bot.db.api.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientMoveEventRepository extends JpaRepository<ClientMoveEventTa, Long> {

    @Query(nativeQuery = true,
           name = "select e.* from bot_client_move_event e inner join" +
                   "(select e2.client_unique_id, max(e2.time) as latest from bot_client_move_event e2" +
                   "where e2.bot_id = ? and e2.time < ? and e2.client_unique_id = ?) t2" +
                   "on e.client_unique_id = t2.client_unique_id and e.time = t2.latest")
    Optional<ClientMoveEventTa> findLatestByClientUId(long botId, String clientUId,
            LocalDateTime endExclusive);

    @Query(nativeQuery = true,
           name = "select e.* from bot_client_move_event e inner join" +
                   "(select e2.client_unique_id, max(e2.time) as latest from bot_client_move_event e2" +
                   "where e2.bot_id = ? and e2.time < ? group by e2.client_unique_id) t2" +
                   "on e.client_unique_id = t2.client_unique_id and e.time = t2.latest;")
    List<ClientMoveEventTa> findLatestGroupByClientUId(long botId, LocalDateTime endExclusive);
}
