package de.esports.aeq.ts3.bot.api.channel;

public class DynamicChannelConfig {

    private int channelId;
    private String namePattern;
    private int minimumChannels;
    private int maximumChannels;
    private int amountOfEmptyChannels;
    private int minimumClientsPerChannel;

    public DynamicChannelConfig(Builder builder) {
        // TODO
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    public int getMinimumChannels() {
        return minimumChannels;
    }

    public void setMinimumChannels(int minimumChannels) {
        this.minimumChannels = minimumChannels;
    }

    public int getMaximumChannels() {
        return maximumChannels;
    }

    public void setMaximumChannels(int maximumChannels) {
        this.maximumChannels = maximumChannels;
    }

    public int getAmountOfEmptyChannels() {
        return amountOfEmptyChannels;
    }

    public void setAmountOfEmptyChannels(int amountOfEmptyChannels) {
        this.amountOfEmptyChannels = amountOfEmptyChannels;
    }

    public int getMinimumClientsPerChannel() {
        return minimumClientsPerChannel;
    }

    public void setMinimumClientsPerChannel(int minimumClientsPerChannel) {
        this.minimumClientsPerChannel = minimumClientsPerChannel;
    }

    public static class Builder {

        private int channelId;
        private String namePattern;
        private int minimumChannels;
        private int maximumChannels;
        private int amountOfEmptyChannels;
        private int minimumClientsPerChannel;

        public Builder(int channelId) {
            this.channelId = channelId;
        }

        public Builder namePattern(String namePattern) {
            this.namePattern = namePattern;
            return this;
        }

        public Builder minimumChannels(int minimumChannels) {
            this.minimumChannels = minimumChannels;
            return this;
        }

        public Builder maximumChannels(int maximumChannels) {
            this.maximumChannels = maximumChannels;
            return this;
        }

        public Builder amountOfEmptyChannels(int amountOfEmptyChannels) {
            this.amountOfEmptyChannels = amountOfEmptyChannels;
            return this;
        }

        public Builder minimumClientsPerChannel(int minimumClientsPerChannel) {
            this.minimumClientsPerChannel = minimumClientsPerChannel;
            return this;
        }

        public DynamicChannelConfig build() {
            return new DynamicChannelConfig(this);
        }
    }
}
