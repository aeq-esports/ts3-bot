package de.esports.aeq.ts3.bot.lib.event;

import java.time.LocalDateTime;

public class ClientMoveEvent {

    public static final int NO_CHANNEL = -1;

    private LocalDateTime time;
    private String clientUId;
    private int channelId;

    public ClientMoveEvent() {

    }

    private ClientMoveEvent(LocalDateTime time, String clientUId, int channelId) {
        this.time = time;
        this.clientUId = clientUId;
        this.channelId = channelId;
    }

    public static ClientMoveEvent of(LocalDateTime time, String clientUId,
            int channelId) {
        return new ClientMoveEvent(time, clientUId, channelId);
    }

    public static ClientMoveEvent offline(LocalDateTime time, String clientUId) {
        return new ClientMoveEvent(time, clientUId, NO_CHANNEL);
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
