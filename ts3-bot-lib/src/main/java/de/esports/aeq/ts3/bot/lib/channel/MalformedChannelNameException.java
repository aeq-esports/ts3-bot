package de.esports.aeq.ts3.bot.lib.channel;

/**
 * @author Lukas Kannenberg
 */
public class MalformedChannelNameException extends Exception{
    public MalformedChannelNameException() {
        super();
    }

    public MalformedChannelNameException(String message) {
        super(message);
    }

    public MalformedChannelNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedChannelNameException(Throwable cause) {
        super(cause);
    }
}
