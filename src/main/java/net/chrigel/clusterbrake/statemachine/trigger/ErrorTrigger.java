package net.chrigel.clusterbrake.statemachine.trigger;

import net.chrigel.clusterbrake.statemachine.Trigger;

/**
 *
 */
public class ErrorTrigger
        implements Trigger {

    private final String message;
    private final Throwable ex;

    public ErrorTrigger() {
        this(null, null);
    }

    public ErrorTrigger(Throwable ex) {
        this(null, ex);
    }

    public ErrorTrigger(String message) {
        this(message, null);
    }

    public ErrorTrigger(String message, Throwable ex) {
        this.message = message;
        this.ex = ex;
    }

    public Throwable getThrowable() {
        return ex;
    }

    public String getMessage() {
        return message;
    }
}
