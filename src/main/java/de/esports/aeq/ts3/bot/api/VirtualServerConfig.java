package de.esports.aeq.ts3.bot.api;

/**
 * @author Lukas Kannenberg
 */
public class VirtualServerConfig {

    private final int virtualServerId;
    private final int virtualServerPort;

    private final String username;
    private final String password;

    private String nickname;

    private VirtualServerConfig(Builder builder) {
        this.virtualServerId = builder.virtualServerId;
        this.virtualServerPort = builder.virtualServerPort;
        this.username = builder.username;
        this.password = builder.password;
        this.nickname = builder.nickname;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getVirtualServerId() {
        return virtualServerId;
    }

    public int getVirtualServerPort() {
        return virtualServerPort;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public static class Builder {

        private int virtualServerId;
        private int virtualServerPort;

        private String username;
        private String password;

        private String nickname;

        private Builder() {

        }

        public Builder virtualServerId(int virtualServerId) {
            this.virtualServerId = virtualServerId;
            return this;
        }

        public Builder virtualServerPort(int virtualServerPort) {
            this.virtualServerPort = virtualServerPort;
            return this;
        }

        public Builder login(String username, String password) {
            this.username = username;
            this.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public VirtualServerConfig build() {
            return new VirtualServerConfig(this);
        }
    }
}
