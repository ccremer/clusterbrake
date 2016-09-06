package net.chrigel.clusterbrake.statemachine.trigger;

import net.chrigel.clusterbrake.statemachine.Trigger;

/**
 *
 */
public class MessageTrigger
        implements Trigger {

    private final String message;

    public MessageTrigger(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
