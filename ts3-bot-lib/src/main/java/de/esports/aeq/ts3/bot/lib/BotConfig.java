package de.esports.aeq.ts3.bot.lib;

import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;

import java.util.Objects;

/**
 * Core configuration of the bot.
 * <p>
 * This class should be immutable.
 *
 * @since 1.0
 */
public final class BotConfig {

    private final long id;
    private final String hostname;
    private final int queryPort;

    private final TS3Query.FloodRate floodRate;
    private final ReconnectStrategy reconnectStrategy;

    private VirtualServerConfig virtualServerConfig;

    private BotConfig(Builder builder) {
        this.id = builder.id;
        this.hostname = builder.hostname;
        this.queryPort = builder.queryPort;
        this.floodRate = builder.floodRate;
        this.reconnectStrategy = builder.reconnectStrategy;
        this.virtualServerConfig = builder.virtualServerConfig;
    }

    public static Builder builder(long id) {
        return new Builder(id);
    }

    public long getId() {
        return id;
    }

    public String getHostname() {
        return hostname;
    }

    public int getQueryPort() {
        return queryPort;
    }

    public TS3Query.FloodRate getFloodRate() {
        return floodRate;
    }

    public ReconnectStrategy getReconnectStrategy() {
        return reconnectStrategy;
    }

    public VirtualServerConfig getVirtualServerConfig() {
        return virtualServerConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BotConfig)) return false;
        BotConfig botConfig = (BotConfig) o;
        return id == botConfig.id &&
                queryPort == botConfig.queryPort &&
                Objects.equals(hostname, botConfig.hostname) &&
                Objects.equals(floodRate, botConfig.floodRate) &&
                Objects.equals(reconnectStrategy, botConfig.reconnectStrategy) &&
                Objects.equals(virtualServerConfig, botConfig.virtualServerConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hostname, queryPort, floodRate, reconnectStrategy,
                virtualServerConfig);
    }

    @Override
    public String toString() {
        return "BotConfig{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", queryPort=" + queryPort +
                ", floodRate=" + floodRate +
                ", reconnectStrategy=" + reconnectStrategy +
                ", virtualServerConfig=" + virtualServerConfig +
                '}';
    }

    public static class Builder {

        private long id;
        private String hostname;
        private int queryPort = 10011;

        private TS3Query.FloodRate floodRate = TS3Query.FloodRate.DEFAULT;
        private ReconnectStrategy reconnectStrategy = ReconnectStrategy.exponentialBackoff();

        private VirtualServerConfig virtualServerConfig;

        private Builder(long id) {
            this.id = id;
        }

        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder queryPort(int queryPort) {
            this.queryPort = queryPort;
            return this;
        }

        public Builder floodRate(TS3Query.FloodRate floodRate) {
            this.floodRate = floodRate;
            return this;
        }

        public Builder reconnectStrategy(ReconnectStrategy reconnectStrategy) {
            this.reconnectStrategy = reconnectStrategy;
            return this;
        }

        public Builder virtualServerConfig(VirtualServerConfig config) {
            this.virtualServerConfig = config;
            return this;
        }

        public BotConfig build() {
            return new BotConfig(this);
        }
    }
}
