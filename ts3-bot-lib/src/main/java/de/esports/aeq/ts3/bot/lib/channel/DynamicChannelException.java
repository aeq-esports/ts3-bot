package de.esports.aeq.ts3.bot.lib.channel;

public class DynamicChannelException extends RuntimeException {

    public DynamicChannelException() {
    }

    public DynamicChannelException(String message) {
        super(message);
    }

    public DynamicChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public DynamicChannelException(Throwable cause) {
        super(cause);
    }
}
