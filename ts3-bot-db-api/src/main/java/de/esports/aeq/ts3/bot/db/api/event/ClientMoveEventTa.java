package de.esports.aeq.ts3.bot.db.api.event;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table
public class ClientMoveEventTa {

    public static final int NO_CHANNEL = -1;

    private LocalDateTime time;
    private String clientUId;
    private int channelId;

    public ClientMoveEventTa() {

    }

    private ClientMoveEventTa(LocalDateTime time, String clientUId, int channelId) {
        this.time = time;
        this.clientUId = clientUId;
        this.channelId = channelId;
    }

    public static ClientMoveEventTa of(LocalDateTime time, String clientUId,
            int channelId) {
        return new ClientMoveEventTa(time, clientUId, channelId);
    }

    public static ClientMoveEventTa offline(LocalDateTime time, String clientUId) {
        return new ClientMoveEventTa(time, clientUId, NO_CHANNEL);
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getClientUId() {
        return clientUId;
    }

    public void setClientUId(String clientUId) {
        this.clientUId = clientUId;
    }

    public boolean isLeaveEvent() {
        return channelId == NO_CHANNEL;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }
}
